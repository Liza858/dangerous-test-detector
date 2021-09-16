package com.github.liza858.dangeroustestdetector.services

import com.intellij.openapi.project.Project
import com.github.liza858.dangeroustestdetector.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
