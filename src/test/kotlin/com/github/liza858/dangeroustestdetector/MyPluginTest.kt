package com.github.liza858.dangeroustestdetector

import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.python.PythonFileType
import com.jetbrains.python.psi.PyClass
import junit.framework.TestCase
import java.io.File
import java.nio.file.Path


class MyPluginTest : BasePlatformTestCase() {

    private val testDataPath = File(Path.of("./src/test/testData").toUri())

    fun testSimpleTestCase() {
        val testFile = File(testDataPath, "simple_test_case.py")
        val psiFile = myFixture.configureByText(PythonFileType.INSTANCE, testFile.readText())

        val dangerousMethods = psiFile.children
            .filterIsInstance<PyClass>()
            .filter { it.name == "MyTest" }
            .flatMap { DangerousTestDetector.getUnitTestMethods(it) }
            .filter { DangerousTestDetector.isDangerousMethod(it) }

        val dangerousMethodsNames = dangerousMethods.map { it.name ?: "" }

        assertEquals(5, dangerousMethods.size)
        TestCase.assertTrue(dangerousMethodsNames.contains("test_c_one"))
        TestCase.assertTrue(dangerousMethodsNames.contains("testc"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_aaaaccccbbbb"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_A_B_C"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_ccc"))
    }

    fun testCaseWithSimpleInherit() {
        val testFile = File(testDataPath, "test_case_with_simple_inherit.py")
        val psiFile = myFixture.configureByText(PythonFileType.INSTANCE, testFile.readText())

        val dangerousMethods = psiFile.children
            .filterIsInstance<PyClass>()
            .filter { it.name == "MyTestBase" || it.name == "DerivedTest" }
            .flatMap { DangerousTestDetector.getUnitTestMethods(it) }
            .filter { DangerousTestDetector.isDangerousMethod(it) }

        val dangerousMethodsNames = dangerousMethods.map { it.name ?: "" }

        assertEquals(3, dangerousMethods.size)
        TestCase.assertTrue(dangerousMethodsNames.contains("test_c"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_acb"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_w4eolcefvnkm3"))
    }

    fun testCaseWithComplexInherit() {
        val testFile = File(testDataPath, "test_case_with_complex_inherit.py")
        val psiFile = myFixture.configureByText(PythonFileType.INSTANCE, testFile.readText())

        val dangerousMethods = psiFile.children
            .filterIsInstance<PyClass>()
            .filter { it.name == "MyTestBase" || it.name == "DerivedTest" || it.name == "A" }
            .flatMap { DangerousTestDetector.getUnitTestMethods(it) }
            .filter { DangerousTestDetector.isDangerousMethod(it) }

        val dangerousMethodsNames = dangerousMethods.map { it.name ?: "" }

        assertEquals(6, dangerousMethods.size)
        TestCase.assertTrue(dangerousMethodsNames.contains("test_c_one"))
        TestCase.assertTrue(dangerousMethodsNames.contains("testc"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_aaaaccccbbbb"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_A_B_C"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_ccc"))
        TestCase.assertTrue(dangerousMethodsNames.contains("test_w4eolcefvnkm3"))
    }
}