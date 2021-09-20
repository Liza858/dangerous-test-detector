package com.github.liza858.dangeroustestdetector

import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyClass
import com.jetbrains.python.psi.PyFunction


class DangerousTestDetector {
    companion object {

        fun getDangerousTestMethods(file: PsiFile): List<PyFunction> {
            val p = getUnitTestClasses(file)
            return getUnitTestClasses(file)
                    .flatMap { getUnitTestMethods(it) }
                    .filter { isDangerousMethod(it) }
        }

        private fun isDangerousMethod(method: PyFunction) = method.name?.contains('c') ?: false

        private fun getUnitTestClasses(file: PsiFile): List<PyClass> {
            return file.children
                    .filterIsInstance<PyClass>()
                    .filter { isUnitTestClass(it) }
        }

        private fun isUnitTestClass(cls: PyClass): Boolean {
            val supers = cls.getSuperClasses(null)
            for (superCls in supers) {
                if (superCls.name == "TestCase") {
                    return true
                }
                if (isUnitTestClass(superCls)) {
                    return true
                }
            }
            return false
        }

        private fun getUnitTestMethods(cls: PyClass): List<PyFunction> {
            return cls.statementList.statements
                    .filterIsInstance<PyFunction>()
                    .filter { it.name?.startsWith("test") ?: false }
        }
    }
}