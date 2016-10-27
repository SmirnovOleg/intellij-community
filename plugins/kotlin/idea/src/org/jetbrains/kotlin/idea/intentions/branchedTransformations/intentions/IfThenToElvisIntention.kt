/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.intentions.branchedTransformations.intentions

import com.intellij.openapi.editor.Editor
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.idea.core.replaced
import org.jetbrains.kotlin.idea.inspections.IntentionBasedInspection
import org.jetbrains.kotlin.idea.intentions.SelfTargetingOffsetIndependentIntention
import org.jetbrains.kotlin.idea.intentions.branchedTransformations.*
import org.jetbrains.kotlin.idea.intentions.getLeftMostReceiverExpression
import org.jetbrains.kotlin.idea.intentions.replaceFirstReceiver
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getType
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode.Companion.PARTIAL
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.TypeNullability
import org.jetbrains.kotlin.types.typeUtil.nullability

class IfThenToElvisInspection : IntentionBasedInspection<KtIfExpression>(IfThenToElvisIntention::class)

class IfThenToElvisIntention : SelfTargetingOffsetIndependentIntention<KtIfExpression>(
        KtIfExpression::class.java,
        "Replace 'if' expression with elvis expression"
) {

    private fun KtExpression.clausesReplaceableByElvis(firstClause: KtExpression, secondClause: KtExpression, context: BindingContext) =
            !firstClause.isNullOrBlockExpression() &&
            (secondClause.evaluatesTo(this) || secondClause.hasFirstReceiverOf(this) && !secondClause.isNullableExpression(context)) &&
            !(firstClause is KtThrowExpression && firstClause.throwsNullPointerExceptionWithNoArguments())

    private fun KtExpression.checkedExpression() = when (this) {
        is KtBinaryExpression -> expressionComparedToNull()
        is KtIsExpression -> leftHandSide
        else -> null
    }

    override fun isApplicableTo(element: KtIfExpression): Boolean {
        val condition = element.condition as? KtOperationExpression ?: return false
        val thenClause = element.then ?: return false
        val elseClause = element.`else` ?: return false

        val context = condition.analyze(PARTIAL)

        val checkedExpression = condition.checkedExpression() ?: return false
        if (!checkedExpression.isStableVariable(context)) return false

        return when (condition) {
            is KtBinaryExpression -> when (condition.operationToken) {
                KtTokens.EQEQ -> checkedExpression.clausesReplaceableByElvis(thenClause, elseClause, context)
                KtTokens.EXCLEQ -> checkedExpression.clausesReplaceableByElvis(elseClause, thenClause, context)
                else -> false
            }
            is KtIsExpression -> {
                if (!context[BindingContext.TYPE, condition.typeReference].isNotNull()) return false

                when (condition.isNegated) {
                    true -> checkedExpression.clausesReplaceableByElvis(thenClause, elseClause, context)
                    false -> checkedExpression.clausesReplaceableByElvis(elseClause, thenClause, context)
                }
            }
            else -> false
        }
    }

    private fun KtExpression.isNullOrBlockExpression(): Boolean {
        val innerExpression = this.unwrapBlockOrParenthesis()
        return innerExpression is KtBlockExpression || innerExpression.node.elementType == KtNodeTypes.NULL
    }

    private fun KtExpression.isNullableExpression(context: BindingContext) = !getType(context).isNotNull()

    private fun KotlinType?.isNotNull() = this?.nullability() == TypeNullability.NOT_NULL

    private fun KtExpression.hasFirstReceiverOf(receiver: KtExpression): Boolean {
        val actualReceiver = (unwrapBlockOrParenthesis() as? KtDotQualifiedExpression)?.getLeftMostReceiverExpression() ?: return false
        return actualReceiver.evaluatesTo(receiver)
    }

    private fun KtExpression.insertSafeCalls(factory: KtPsiFactory): KtExpression {
        if (this !is KtQualifiedExpression) return this
        if (this is KtDotQualifiedExpression) {
            operationTokenNode.psi.replace(factory.createSafeCallNode().psi)
        }
        receiverExpression.insertSafeCalls(factory)
        return this
    }

    override fun applyTo(element: KtIfExpression, editor: Editor?) {
        val condition = element.condition as KtOperationExpression

        val thenClause = element.then!!
        val elseClause = element.`else`!!
        val thenExpression = thenClause.unwrapBlockOrParenthesis()
        val elseExpression = elseClause.unwrapBlockOrParenthesis()

        val (left, right) = when (condition) {
            is KtBinaryExpression -> when (condition.operationToken) {
                KtTokens.EQEQ -> Pair(elseExpression, thenExpression)
                KtTokens.EXCLEQ -> Pair(thenExpression, elseExpression)
                else -> throw IllegalArgumentException()
            }
            is KtIsExpression -> when (condition.isNegated) {
                true -> Pair(elseExpression, thenExpression)
                false -> Pair(thenExpression, elseExpression)
            }
            else -> throw IllegalArgumentException()
        }

        val factory = KtPsiFactory(element)
        val newReceiver = (condition as? KtIsExpression)?.let {
            factory.createExpressionByPattern("$0 as? $1",
                                              (left as? KtDotQualifiedExpression)?.getLeftMostReceiverExpression() ?: left,
                                              it.typeReference!!)
        }
        val checkedExpression = condition.checkedExpression()!!
        val replacedLeft = if (left.evaluatesTo(checkedExpression)) {
            if (condition is KtIsExpression) newReceiver!! else left
        }
        else {
            if (condition is KtIsExpression) {
                (left as KtDotQualifiedExpression).replaceFirstReceiver(
                        factory, newReceiver!!, safeAccess = true)
            }
            else {
                left.insertSafeCalls(factory)
            }
        }
        val newExpr = element.replaced(factory.createExpressionByPattern("$0 ?: $1", replacedLeft, right))
        val elvis = KtPsiUtil.deparenthesize(newExpr) as KtBinaryExpression

        if (editor != null) {
            elvis.inlineLeftSideIfApplicableWithPrompt(editor)
        }
    }
}
