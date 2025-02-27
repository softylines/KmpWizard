package com.softylines.kmpwizard.writer.conventionplugins

import com.softylines.kmpwizard.core.deps.DepsConstants
import com.softylines.kmpwizard.core.libs.LibsFile
import com.softylines.kmpwizard.core.libs.LibsLine
import com.softylines.kmpwizard.core.libs.getLibraryOrCreate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.parser.libs.LibsParser
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.pathString

object ConventionPluginWriter {

    fun writeConventionPlugin(
        conventionPluginDirectory: File,
        conventionPluginFileTemplates: List<FileTemplate>,
    ) {
        val conventionPluginFilesDirectory = Path(
            conventionPluginDirectory.absolutePath,
            "src",
            "main",
            "kotlin",
        )

        conventionPluginFileTemplates.forEach { fileTemplate ->
            val conventionPluginFile = Path(
                conventionPluginFilesDirectory.pathString,
                fileTemplate.name,
            )

            conventionPluginFile.toFile().writeText(fileTemplate.content)
        }
    }

    fun initConventionPluginModule(
        libsFile: LibsFile,
        conventionPluginDirectory: File,
    ) {
        conventionPluginDirectory.mkdirs()

        createBuildGradleFile(
            libsFile = libsFile,
            conventionPluginDirectory = conventionPluginDirectory,
        )

        createSettingsGradleFile(
            libsFile = libsFile,
            conventionPluginDirectory = conventionPluginDirectory,
        )

        val conventionPluginFilesDirectory = Path(
            conventionPluginDirectory.absolutePath,
            "src",
            "main",
            "kotlin",
        )

        conventionPluginFilesDirectory.toFile().mkdirs()

        writeConventionPlugin(
            conventionPluginDirectory = conventionPluginDirectory,
            conventionPluginFileTemplates = kotlinConventionPluginFileTemplates
        )

        writeConventionPlugin(
            conventionPluginDirectory = conventionPluginDirectory,
            conventionPluginFileTemplates = composeConventionPluginFileTemplates
        )
    }

    fun createBuildGradleFile(
        libsFile: LibsFile,
        conventionPluginDirectory: File,
    ) {
        // Todo: Make get version type more intelligent, try to get plugin version if exists
        // Todo: Make this check earlier and ask the user to enter the version if we can't find it
        val kotlinLibrary = libsFile.getLibraryOrCreate(
            name = "gradlePlugin-kotlin",
            module = DepsConstants.KotlinGradlePluginModule,
            versionType = LibsLine.VersionType.Version("2.0.20"),
        )

        val composeCompilerLibrary = libsFile.getLibraryOrCreate(
            name = "gradlePlugin-composeCompiler",
            module = DepsConstants.ComposeCompilerGradlePluginModule,
            versionType = LibsLine.VersionType.Version("2.0.20"),
        )

        val composeLibrary = libsFile.getLibraryOrCreate(
            name = "gradlePlugin-compose",
            module = DepsConstants.ComposeGradlePluginModule,
            versionType = LibsLine.VersionType.Version("1.7.3"),
        )

        // Create convention plugin build.gradle.kts file
        val buildGradleFile = Path(
            conventionPluginDirectory.absolutePath,
            "build.gradle.kts",
        ).toFile()

        buildGradleFile.createNewFile()

        buildGradleFile.writeText(
            """
            plugins {
                `kotlin-dsl`
            }
            
            dependencies {
                implementation(libs.${LibsLine.libsLineNameToVariableName(kotlinLibrary.name)})
                implementation(libs.${LibsLine.libsLineNameToVariableName(composeCompilerLibrary.name)})
                implementation(libs.${LibsLine.libsLineNameToVariableName(composeLibrary.name)})
            
                // hack to access version catalogue https://github.com/gradle/gradle/issues/15383
                compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
            }
            """.trimIndent()
        )
    }

    fun createSettingsGradleFile(
        libsFile: LibsFile,
        conventionPluginDirectory: File,
    ) {
        // Create convention plugin settings.gradle.kts file
        val settingsGradleFile = Path(
            conventionPluginDirectory.absolutePath,
            "settings.gradle.kts",
        ).toFile()

        settingsGradleFile.createNewFile()

        // TODO: Make get libs versions toml file path more intelligent, not static
        settingsGradleFile.writeText(
            """            
            dependencyResolutionManagement {
                versionCatalogs {
                    create("libs") {
                        from(files("../gradle/libs.versions.toml"))
                    }
                }
            
                @Suppress("UnstableApiUsage")
                repositories {
                    google()
                    gradlePluginPortal()
                    mavenCentral()
                }
            }
            """.trimIndent()
        )
    }

    private val kotlinConventionPluginFileTemplates = listOf(
        FileTemplate(
            name = "kotlin.mp.gradle.kts",
            content = """
                plugins {
                    `kotlin-multiplatform`
                    id("android.lib")
                }
                
                kotlin {
                    applyHierarchyTemplate()
                    applyTargets()
                }
            """.trimIndent()
        ),
        FileTemplate(
            name = "Hirearchy.kt",
            content = """
                import org.gradle.api.NamedDomainObjectContainer
                import org.gradle.api.NamedDomainObjectProvider
                import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
                import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
                import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                fun KotlinMultiplatformExtension.applyHierarchyTemplate() {
                    applyDefaultHierarchyTemplate {
                        common {
                            group("skiko") {
                                withJvm()
                                withIos()
                            }

                            group("nonDesktop") {
                                withIos()
                            }
                        }
                    }
                }
            """.trimIndent()
        ),
        FileTemplate(
            name = "Targets.kt",
            content = """
                import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

                fun KotlinMultiplatformExtension.applyTargets() {
                    targets {
                        jvm("desktop")
                        ios()
                    }
                }
            """.trimIndent()
        ),
        FileTemplate(
            name = "Android.kt",
            content = """
                import com.android.build.api.dsl.LibraryExtension
                import org.gradle.accessors.dm.LibrariesForLibs
                import org.gradle.api.JavaVersion
                import org.gradle.api.Project
                import org.gradle.kotlin.dsl.configure
                import org.gradle.kotlin.dsl.get
                import org.gradle.kotlin.dsl.the

                fun Project.androidLibrarySetup() {
                    val libs = the<LibrariesForLibs>()

                    extensions.configure<LibraryExtension> {
                        namespace = group.toString() + path.replace("-", "").split(":").joinToString(".")
                        compileSdk = libs.versions.android.compileSdk.get().toInt()

                        sourceSets["main"].res.srcDirs("src/androidMain/res")
                        sourceSets["main"].resources.srcDirs("src/commonMain/resources")

                        defaultConfig {
                            minSdk = libs.versions.android.minSdk.get().toInt()
                        }
                        compileOptions {
                            sourceCompatibility = JavaVersion.VERSION_11
                            targetCompatibility = JavaVersion.VERSION_11
                        }
                    }
                }   
            """.trimIndent()
        ),
        FileTemplate(
            name = "DataLayer.kt",
            content = """
                import org.gradle.accessors.dm.LibrariesForLibs
                import org.gradle.api.Project
                import org.gradle.kotlin.dsl.configure
                import org.gradle.kotlin.dsl.the
                import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

                fun Project.setupDataLayer() {
                    val libs = the<LibrariesForLibs>()

                    extensions.configure<KotlinMultiplatformExtension> {
                        sourceSets.commonMain.dependencies {
                            // Ktor Bundle
                            implementation(libs.bundles.ktor)
                        }
                    }
                }
            """.trimIndent()
        ),
        FileTemplate(
            name = "data.layer.gradle.kts",
            content = """
                plugins {
                    id("kotlin.mp")
                    id("koin")
                    id("org.jetbrains.kotlin.plugin.serialization")
                }

                setupDataLayer()
            """.trimIndent()
        ),
        FileTemplate(
            name = "android.lib.gradle.kts",
            content = """
                plugins {
                    `android-library`
                }

                androidLibrarySetup()
            """.trimIndent()
        )
    )

    private val composeConventionPluginFileTemplates = listOf(
        FileTemplate(
            name = "compose.mp.gradle.kts",
            content = """
                plugins {
                    id("kotlin.mp")
                    id("org.jetbrains.compose")
                    `kotlin-composecompiler`
                }
            """.trimIndent()
        )
    )

}