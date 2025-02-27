package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.BuildGradleFileTemplate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import kotlin.io.path.Path

class DomainModuleTemplate : ModuleTemplate {

    override val name: String = "Domain"
    override val parent: String = ":domain"
    override val iconKey: IconKey = AllIconsKeys.Nodes.Interface

    override val files: List<IFileTemplate> = buildIFileTemplateList {
        addFolderTemplate(name = "domain") {
            addFolderTemplate(name = IFileTemplate.ModuleNameKeyDollar) {
                // Model
                addFolderTemplate(name = "model") {
                    addFileTemplate(
                        name = "${IFileTemplate.ModuleNameKeyDollar}Model.kt",
                        content =
                            """                            
                            // Add your model classes here
                            """.trimIndent()
                    )
                }

                // Repository
                addFolderTemplate(name = "repository") {
                    addFileTemplate(
                        name = "${IFileTemplate.ModuleNameKeyDollar}Repository.kt",
                        content =
                            """                            
                            interface ${IFileTemplate.ModuleNameKeyDollar}Repository {
                                // Add your repository methods here
                            }
                            """.trimIndent()
                    )
                }
            }
        }
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