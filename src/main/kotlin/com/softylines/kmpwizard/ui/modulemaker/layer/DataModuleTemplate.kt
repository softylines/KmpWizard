package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.BuildGradleFileTemplate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList

class DataModuleTemplate: ModuleTemplate {

    override val name: String = "Data"

    /**
     * data
     *  - $ModuleName$
     *   - repository
     *    - $ModuleName$Repository.kt
     */
    override val files: List<IFileTemplate> = buildIFileTemplateList {
        addFolderTemplate(name = "data") {
            addFolderTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}") {
                // di
                addFolderTemplate(name = "di") {
                    addFileTemplate(
                        name = "${IFileTemplate.ModuleNameKeyDollar}DataModule.kt",
                        content = """
                            // Add your data module here
                        """.trimIndent()
                    )
                }

                // remote data source
                addFolderTemplate(name = "remote") {
                    addFileTemplate(
                        name = "${IFileTemplate.ModuleNameKeyDollar}RemoteDataSource.kt",
                        content = """
                            class ${IFileTemplate.ModuleNameKeyDollar}RemoteDataSource {
                                // Add your remote data source methods here
                            }
                        """.trimIndent()
                    )
                }

                // Repository
                addFolderTemplate(name = "repository") {
                    addFileTemplate(
                        name = "${IFileTemplate.ModuleNameKeyDollar}RepositoryImpl.kt",
                        content = """
                            import domain.${IFileTemplate.ModuleNameKeyDollar}.repository.${IFileTemplate.ModuleNameKeyDollar}Repository
                            import data.${IFileTemplate.ModuleNameKeyDollar}.remote.${IFileTemplate.ModuleNameKeyDollar}RemoteDataSource
                            
                            class ${IFileTemplate.ModuleNameKeyDollar}RepositoryImpl(
                                private val remoteDataSource: ${IFileTemplate.ModuleNameKeyDollar}RemoteDataSource
                            ): ${IFileTemplate.ModuleNameKeyDollar}Repository {
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