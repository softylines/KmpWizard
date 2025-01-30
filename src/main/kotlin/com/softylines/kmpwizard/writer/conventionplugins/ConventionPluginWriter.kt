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

    fun hasConventionPlugin(
        conventionPluginDirectory: File,
    ): Boolean {
        return conventionPluginDirectory.exists()
    }

    fun listConventionPlugins(
        conventionPluginDirectory: File,
    ): List<String> {
        val conventionPluginFilesDirectory = Path(
            conventionPluginDirectory.absolutePath,
            "src",
            "main",
            "kotlin",
        )

        return conventionPluginFilesDirectory
            .toFile()
            .listFiles()
            .orEmpty()
            .filter {
                println("it.extension: ${it.extension}")
                it.isFile && it.name.endsWith(".gradle.kts")
            }
            .map { it.nameWithoutExtension }
    }

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

        // Todo: Create convention plugin files

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

}