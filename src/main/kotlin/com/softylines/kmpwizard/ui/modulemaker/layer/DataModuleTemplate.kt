package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.BuildGradleFileTemplate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icon.IntelliJIconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import kotlin.io.path.Path

class DataModuleTemplate: ModuleTemplate {

    override val name: String = "Data"
    override val parent: String = ":data"
    override val iconKey: IconKey = AllIconsKeys.Nodes.Folder

    /**
     * data
     *  - $ModuleName$
     *   - repository
     *    - $ModuleName$Repository.kt
     */
    override val files: List<IFileTemplate> = buildIFileTemplateList {
        addFolderTemplate(name = "data") {
            addFolderTemplate(name = IFileTemplate.ModuleNameKeyDollar) {
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
                id("data.layer")
            }
            
            kotlin {
                sourceSets.commonMain.dependencies {
                    
                }
            }
        """.trimIndent()
    )

}