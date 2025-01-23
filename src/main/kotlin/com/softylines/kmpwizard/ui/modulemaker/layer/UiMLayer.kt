package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList

class UiMLayer: ModuleTemplate {

    override val name: String = "UI"

    override val files: List<IFileTemplate> = buildIFileTemplateList {
        addFolderTemplate(name = "ui") {
            addFolderTemplate(name = IFileTemplate.ModuleNameKeyDollar) {
                addFileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}Screen.kt")
                addFileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}State.kt")
                addFileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}Event.kt")
                addFileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}Navigation.kt")
                addFileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}ViewModel.kt")
            }
        }
    }

}