package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.BuildGradleFileTemplate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList

class UiModuleTemplate: ModuleTemplate {

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

    override val buildGradleFile: FileTemplate = BuildGradleFileTemplate(
        content = """
            plugins {
                kotlin("jvm")
                kotlin("plugin.allopen")
            }
            
            kotlin {
                sourceSets.commonMain.dependencies {
                    implementation(compose.ui)
                }
            }
        """.trimIndent()
    )

}