package com.github.liza858.dangeroustestdetector

import com.github.liza858.dangeroustestdetector.ui.DetectorToolWindowPanel
import com.intellij.codeInspection.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiFile
import com.intellij.util.IncorrectOperationException
import com.jetbrains.python.psi.PyFunction


class DangerousTestInspection : LocalInspectionTool() {
    companion object {
        private val dangerousMethodQuickFixes = arrayOf(DangerousMethodQuickFix())
        private val LOG = Logger.getInstance(DangerousTestInspection::class.java)
    }

    /* check file and highlights Unittest dangerous methods */
    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        val dangerousMethods = DangerousTestDetector.getDangerousTestMethods(file)
        ApplicationManager.getApplication()
            .invokeLater { updateDetectorToolWindow(file, dangerousMethods) } // updates ui in ui thread
        return dangerousMethods.map {
            manager.createProblemDescriptor(
                it,
                MyBundle.message("inspection.error.message"),
                dangerousMethodQuickFixes,
                ProblemHighlightType.WARNING,
                isOnTheFly,
                false
            )
        }.toTypedArray()
    }

    /* write statistics to the tool window field: file name and total number of dangerous methods */
    private fun updateDetectorToolWindow(file: PsiFile, dangerousMethods: List<PyFunction>) {
        val detectorToolWindow = ToolWindowManager
            .getInstance(file.project)
            .getToolWindow("Dangerous test detector")
        val contents = detectorToolWindow?.contentManager?.contents
        val detectorToolWindowPanel = contents?.getOrNull(0)?.component as? DetectorToolWindowPanel
        detectorToolWindowPanel?.setStatistics(file, dangerousMethods)
    }

    /* delete all 'c' in Unittest dangerous methods */
    private class DangerousMethodQuickFix : LocalQuickFix {
        override fun getFamilyName() = MyBundle.message("inspection.quick.fix.message")

        override fun applyFix(project: Project, problemDescriptor: ProblemDescriptor) {
            val dangerousMethod = problemDescriptor.psiElement as? PyFunction
            try {
                dangerousMethod?.let { it.setName(it.name?.replace("c", "") ?: "") }
            } catch (e: IncorrectOperationException) {
                LOG.error(e)
            }
        }
    }
}