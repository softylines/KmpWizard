package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.IFileTemplate

interface ModuleTemplate {

    val name: String
    val files: List<IFileTemplate>

    companion object {
        val Ui = UiMLayer()

        val Data = DataMLayer()

        val Domain = DomainMLayer()
    }

}