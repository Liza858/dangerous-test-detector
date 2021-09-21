package com.github.liza858.dangeroustestdetector

import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.PyFunction
import com.jetbrains.python.pyi.PyiFile
import java.util.*


class DangerousTestDetector {
    companion object {

        private const val DANGEROUS_CHAR = 'c'
        private const val UNIT_TEST_METHOD_BEGIN = "test"
        private const val TEST_CASE_CLS_NAME = "TestCase"
        private const val TEST_CASE_FILE_NAME_BEGIN = "case.py"
        private const val TEST_CASE_CLS_DIR = "unittest"

        fun getDangerousTestMethods(file: PsiFile): List<PyFunction> {
            return getUnitTestClasses(file)
                .flatMap { getUnitTestMethods(it) }
                .filter { isDangerousMethod(it) }
        }

        private fun isDangerousMethod(method: PyFunction) = method.name?.contains(DANGEROUS_CHAR) ?: false

        /* finds Unittest classes in the children of the psi file */
        private fun getUnitTestClasses(file: PsiFile): List<PyClass> {
            return file.children
                .filterIsInstance<PyClass>()
                .filter { isUnitTestClass(it) }
        }

        /* checks the class name, the file where it is defined,
           the directory where the file is located
        * */
        private fun isTestCaseClass(cls: PyClass): Boolean {
            val clsFile = cls.parent as? PyiFile
            val isCaseFile = clsFile?.name?.startsWith(TEST_CASE_FILE_NAME_BEGIN) ?: false
            if (cls.name == TEST_CASE_CLS_NAME && isCaseFile) {
                val clsDir = clsFile?.parent
                return clsDir?.name == TEST_CASE_CLS_DIR
            }
            return false
        }

        /* checks all parents of the class
           if it finds TestCaseClass -- return true else false
        * */
        private fun isUnitTestClass(cls: PyClass): Boolean {
            val supersQueue = LinkedList<PyClass>()
            supersQueue.addAll(cls.getSuperClasses(null))
            while (supersQueue.isNotEmpty()) {
                val superCls = supersQueue.poll()
                if (isTestCaseClass(superCls)) {
                    return true
                }
                supersQueue.addAll(superCls.getSuperClasses(null))
            }
            return false
        }

        /* get test methods from class */
        private fun getUnitTestMethods(cls: PyClass): List<PyFunction> {
            return cls.statementList.statements
                .filterIsInstance<PyFunction>()
                .filter { it.name?.startsWith(UNIT_TEST_METHOD_BEGIN) ?: false }
        }
    }
}