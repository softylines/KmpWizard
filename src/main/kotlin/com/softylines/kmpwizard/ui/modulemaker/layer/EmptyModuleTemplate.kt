package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.BuildGradleFileTemplate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys

class EmptyModuleTemplate: ModuleTemplate {

    override val name: String = "Empty"
    override val parent: String = ""
    override val iconKey: IconKey = AllIconsKeys.Nodes.Folder

    override val files: List<IFileTemplate> = buildIFileTemplateList {
        addFolderTemplate(name = IFileTemplate.ModuleNameKeyDollar) {}
    }

    override val buildGradleFile: FileTemplate = BuildGradleFileTemplate(
        content = """
            plugins {
                id("kotlin.mp")
            }
            
            kotlin {
                sourceSets.commonMain.dependencies {
                    
                }
            }
        """.trimIndent()
    )

}