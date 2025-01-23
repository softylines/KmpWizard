package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList

class DomainMLayer: ModuleTemplate {

    override val name: String = "Domain"

    override val files: List<IFileTemplate> = buildIFileTemplateList {
        addFolderTemplate(name = "domain") {
            addFolderTemplate(name = IFileTemplate.ModuleNameKeyDollar) {
                // Model
                addFolderTemplate(name = "model") {
                    addFileTemplate(
                        name = "${IFileTemplate.ModuleNameKeyDollar}Model.kt",
                        content = """                            
                            // Add your model classes here
                        """.trimIndent()
                    )
                }

                // Repository
                addFolderTemplate(name = "repository") {
                    addFileTemplate(
                        name = "${IFileTemplate.ModuleNameKeyDollar}Repository.kt",
                        content = """                            
                            interface ${IFileTemplate.ModuleNameKeyDollar}Repository {
                                // Add your repository methods here
                            }
                        """.trimIndent()
                    )
                }
            }
        }
    }

}