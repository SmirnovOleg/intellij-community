// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.siyeh.ig.javadoc;

import com.intellij.codeInsight.javadoc.JavaDocUtil;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ui.SingleCheckboxOptionsPanel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocToken;
import com.intellij.psi.templateLanguages.TemplateLanguageUtil;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.siyeh.InspectionGadgetsBundle;
import com.siyeh.ig.BaseInspection;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.InspectionGadgetsFix;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Bas Leijdekkers
 */
public class DanglingJavadocInspection extends BaseInspection {

  public boolean ignoreCopyright = true;

  @NotNull
  @Override
  protected String buildErrorString(Object... infos) {
    return InspectionGadgetsBundle.message("dangling.javadoc.problem.descriptor");
  }

  @Override
  public @Nullable JComponent createOptionsPanel() {
    return new SingleCheckboxOptionsPanel(InspectionGadgetsBundle.message("dangling.javadoc.ignore.copyright.option"), this,
                                          "ignoreCopyright");
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  protected InspectionGadgetsFix @NotNull [] buildFixes(Object... infos) {
    return new InspectionGadgetsFix[] {
      new DeleteCommentFix(),
      new ConvertCommentFix()
    };
  }

  private static class ConvertCommentFix extends InspectionGadgetsFix {
    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
      return InspectionGadgetsBundle.message("dangling.javadoc.convert.quickfix");
    }

    @Override
    protected void doFix(Project project, ProblemDescriptor descriptor) {
      final PsiElement element = descriptor.getPsiElement();
      final PsiElement docComment = element.getParent();
      final StringBuilder newCommentText = new StringBuilder();
      for (PsiElement child = docComment.getFirstChild(); child != null; child = child.getNextSibling()) {
        if (child instanceof PsiDocToken) {
          final PsiDocToken docToken = (PsiDocToken)child;
          final IElementType tokenType = docToken.getTokenType();
          if (JavaDocTokenType.DOC_COMMENT_START.equals(tokenType)) {
            newCommentText.append("/*");
          }
          else if (!JavaDocTokenType.DOC_COMMENT_LEADING_ASTERISKS.equals(tokenType)) {
            newCommentText.append(child.getText());
          }
        }
        else {
          newCommentText.append(child.getText());
        }
      }
      final PsiElementFactory factory = JavaPsiFacade.getElementFactory(project);
      final PsiComment newComment = factory.createCommentFromText(newCommentText.toString(), element);
      docComment.replace(newComment);
    }
  }

  private static class DeleteCommentFix extends InspectionGadgetsFix {

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
      return InspectionGadgetsBundle.message("dangling.javadoc.delete.quickfix");
    }

    @Override
    protected void doFix(Project project, ProblemDescriptor descriptor) {
      final PsiElement element = descriptor.getPsiElement();
      element.getParent().delete();
    }
  }

  @Override
  public BaseInspectionVisitor buildVisitor() {
    return new DanglingJavadocVisitor();
  }

  private class DanglingJavadocVisitor extends BaseInspectionVisitor {

    @Override
    public void visitDocComment(PsiDocComment comment) {
      super.visitDocComment(comment);
      if (comment.getOwner() != null || TemplateLanguageUtil.isInsideTemplateFile(comment)) {
        return;
      }
      if (JavaDocUtil.isInsidePackageInfo(comment) &&
          PsiTreeUtil.skipWhitespacesAndCommentsForward(comment) instanceof PsiPackageStatement &&
          "package-info.java".equals(comment.getContainingFile().getName())) {
        return;
      }
      if (ignoreCopyright && comment.getPrevSibling() == null && comment.getParent() instanceof PsiFile) {
        return;
      }
      registerError(comment.getFirstChild());
    }
  }
}
