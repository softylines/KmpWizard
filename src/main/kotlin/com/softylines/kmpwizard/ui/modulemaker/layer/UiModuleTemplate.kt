package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.BuildGradleFileTemplate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import kotlin.io.path.Path

class UiModuleTemplate: ModuleTemplate {

    override val name: String = "UI"
    override val parent: String = ":ui"
    override val iconKey: IconKey = AllIconsKeys.Ide.Pipette

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
                id("compose.mp")
            }
            
            kotlin {
                sourceSets.commonMain.dependencies {
                    implementation(compose.ui)
                }
            }
        """.trimIndent()
    )

}