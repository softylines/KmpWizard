package com.softylines.kmpwizard.template

import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState

fun generateDemoMdFileTemplate(
    state: ModuleMakerState
): String {
    return  """
        Demo Markdown File
        ==================
        
        This is a demo markdown file generated by the KMP Wizard.
        
        Module Name: ${state.moduleName}
        Package Name: ${state.packageName}
        Module Layer: ${state.moduleLayer.name}
    """.trimIndent()
}