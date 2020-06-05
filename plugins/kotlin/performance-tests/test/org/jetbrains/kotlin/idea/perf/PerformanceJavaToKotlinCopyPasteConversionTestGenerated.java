/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.perf;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.KotlinTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.jetbrains.kotlin.test.TestRoot;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/*
 * This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}.
 * DO NOT MODIFY MANUALLY.
 */
@SuppressWarnings("all")
@TestRoot("performance-tests")
@TestDataPath("$CONTENT_ROOT")
@RunWith(JUnit3RunnerWithInners.class)
@TestMetadata("../idea/testData/copyPaste/conversion")
@Ignore("[VD] temporary disable for kotlin-ide")
public class PerformanceJavaToKotlinCopyPasteConversionTestGenerated extends AbstractPerformanceJavaToKotlinCopyPasteConversionTest {
    private void runTest(String testDataFilePath) throws Exception {
        KotlinTestUtils.runTest(this::doPerfTest, this, testDataFilePath);
    }

    @TestMetadata("AddImports.java")
    public void testAddImports() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/AddImports.java");
    }

    @TestMetadata("AddImportsButNoConversion.java")
    public void testAddImportsButNoConversion() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/AddImportsButNoConversion.java");
    }

    @TestMetadata("AddImportsButNoConversion2.java")
    public void testAddImportsButNoConversion2() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/AddImportsButNoConversion2.java");
    }

    @TestMetadata("AddImportsClassInSamePackage.java")
    public void testAddImportsClassInSamePackage() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/AddImportsClassInSamePackage.java");
    }

    @TestMetadata("AddImportsDummyConflict.java")
    public void testAddImportsDummyConflict() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/AddImportsDummyConflict.java");
    }

    @TestMetadata("AddImportsWithExplicitImports.java")
    public void testAddImportsWithExplicitImports() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/AddImportsWithExplicitImports.java");
    }

    @TestMetadata("AddKClassImport.java")
    public void testAddKClassImport() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/AddKClassImport.java");
    }

    @TestMetadata("Arithmetic.java")
    public void testArithmetic() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/Arithmetic.java");
    }

    @TestMetadata("ClassWithNoDocComment.java")
    public void testClassWithNoDocComment() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/ClassWithNoDocComment.java");
    }

    @TestMetadata("ClassWithOverrides.java")
    public void testClassWithOverrides() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/ClassWithOverrides.java");
    }

    @TestMetadata("Constructor.java")
    public void testConstructor() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/Constructor.java");
    }

    @TestMetadata("ConversionInCorrectContext.java")
    public void testConversionInCorrectContext() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/ConversionInCorrectContext.java");
    }

    @TestMetadata("CopyAnnotation.java")
    public void testCopyAnnotation() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/CopyAnnotation.java");
    }

    @TestMetadata("ExtendsTypeRef.java")
    public void testExtendsTypeRef() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/ExtendsTypeRef.java");
    }

    @TestMetadata("FieldWithNoEndComment.java")
    public void testFieldWithNoEndComment() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/FieldWithNoEndComment.java");
    }

    @TestMetadata("FieldWithNoModifierAndNoSemicolon.java")
    public void testFieldWithNoModifierAndNoSemicolon() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/FieldWithNoModifierAndNoSemicolon.java");
    }

    @TestMetadata("FileWithNoPackageStatement.java")
    public void testFileWithNoPackageStatement() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/FileWithNoPackageStatement.java");
    }

    @TestMetadata("HalfTheWhiteSpace.java")
    public void testHalfTheWhiteSpace() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/HalfTheWhiteSpace.java");
    }

    @TestMetadata("ImplementsTypeRef.java")
    public void testImplementsTypeRef() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/ImplementsTypeRef.java");
    }

    @TestMetadata("Imports1.java")
    public void testImports1() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/Imports1.java");
    }

    @TestMetadata("Imports2.java")
    public void testImports2() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/Imports2.java");
    }

    @TestMetadata("Imports3.java")
    public void testImports3() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/Imports3.java");
    }

    @TestMetadata("Indentation.java")
    public void testIndentation() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/Indentation.java");
    }

    @TestMetadata("InsertIntoComment.java")
    public void testInsertIntoComment() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/InsertIntoComment.java");
    }

    @TestMetadata("InsertIntoString.java")
    public void testInsertIntoString() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/InsertIntoString.java");
    }

    @TestMetadata("Kt31848.java")
    public void testKt31848() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/Kt31848.java");
    }

    @TestMetadata("MethodDeclarationWithNoBody.java")
    public void testMethodDeclarationWithNoBody() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/MethodDeclarationWithNoBody.java");
    }

    @TestMetadata("MethodReferenceWithoutQualifier.java")
    public void testMethodReferenceWithoutQualifier() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/MethodReferenceWithoutQualifier.java");
    }

    @TestMetadata("MethodWithNoAnnotation.java")
    public void testMethodWithNoAnnotation() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/MethodWithNoAnnotation.java");
    }

    @TestMetadata("MethodWithOnlyOneAnnotation.java")
    public void testMethodWithOnlyOneAnnotation() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/MethodWithOnlyOneAnnotation.java");
    }

    @TestMetadata("OnlyClosingBrace.java")
    public void testOnlyClosingBrace() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/OnlyClosingBrace.java");
    }

    @TestMetadata("OnlyOneBraceFromBlock.java")
    public void testOnlyOneBraceFromBlock() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/OnlyOneBraceFromBlock.java");
    }

    @TestMetadata("OnlyQualifier.java")
    public void testOnlyQualifier() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/OnlyQualifier.java");
    }

    @TestMetadata("RawTypeRef.java")
    public void testRawTypeRef() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/RawTypeRef.java");
    }

    @TestMetadata("RedundantTypeCast.java")
    public void testRedundantTypeCast() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/RedundantTypeCast.java");
    }

    @TestMetadata("RedundantTypeCast2.java")
    public void testRedundantTypeCast2() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/RedundantTypeCast2.java");
    }

    @TestMetadata("SampleBlock.java")
    public void testSampleBlock() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/SampleBlock.java");
    }

    @TestMetadata("SeveralMethodsSample.java")
    public void testSeveralMethodsSample() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/SeveralMethodsSample.java");
    }

    @TestMetadata("SingleWordFromIdentifier.java")
    public void testSingleWordFromIdentifier() throws Exception {
        runTest("../idea/testData/copyPaste/conversion/SingleWordFromIdentifier.java");
    }
}
