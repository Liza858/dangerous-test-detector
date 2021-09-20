package com.github.liza858.dangeroustestdetector.ui

import com.intellij.psi.PsiFile
import com.jetbrains.python.psi.PyFunction
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class DetectorToolWindowPanel : JPanel(GridBagLayout()) {
    companion object {
        private const val STATISTICS_HEADER = "Total number of dangerous methods in file "
    }

    private val statisticsLabel = JLabel()

    init {
        val constraints = GridBagConstraints()
        constraints.weightx = 1.0
        constraints.weighty = 1.0
        constraints.anchor = GridBagConstraints.NORTHWEST
        this.border = EmptyBorder(15, 15, 15, 15)
        this.add(statisticsLabel, constraints)
    }

    fun setStatistics(file: PsiFile, dangerousMethods: List<PyFunction>) {
        statisticsLabel.text = STATISTICS_HEADER + "${file.name}: ${dangerousMethods.size}"
    }
}