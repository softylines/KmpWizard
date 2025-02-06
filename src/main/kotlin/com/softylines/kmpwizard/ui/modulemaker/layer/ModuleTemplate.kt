package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate

interface ModuleTemplate {

    val name: String
    val files: List<IFileTemplate>

    val buildGradleFile: FileTemplate

    companion object {
        val Ui = UiModuleTemplate()

        val Data = DataModuleTemplate()

        val Domain = DomainModuleTemplate()
    }

}