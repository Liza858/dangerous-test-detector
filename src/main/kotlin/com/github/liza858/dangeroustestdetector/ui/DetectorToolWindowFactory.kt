package com.github.liza858.dangeroustestdetector.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class DetectorToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = DetectorToolWindowPanel()
        val contentManager = toolWindow.contentManager
        val toolContent = contentManager.factory.createContent(panel, "", false)
        contentManager.addContent(toolContent)
    }
}