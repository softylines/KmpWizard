package com.softylines.kmpwizard.writer.module

import com.softylines.kmpwizard.template.buildGradleFileTemplate
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import java.io.File
import java.nio.file.Files
import kotlin.text.isNotBlank

object ModuleWriter {

    /**
     * Create a new module
     *
     * @param name The name of the module
     * @param parentDirectory The parent directory of the module
     * @param settingsGradleFile The settings.gradle.kts file
     * @return The module directory
     */
    fun createModule(
        state: ModuleMakerState,
        parentDirectory: File,
        settingsGradleFile: File,
    ): File? {
        val name = state.moduleNameState.text.toString()

        // Create the module directory
        val moduleDirectory = getModuleDirectory(
            name = name,
            parentDirectory = parentDirectory,
        ) ?: return null

        // Add module to settings.gradle.kts
        addModuleToSettingsGradle(
            moduleName = name,
            settingsGradleFile = settingsGradleFile,
        )

        // Add build.gradle.kts file
        addModuleBuildGradleFile(moduleDirectory)

        // Add src/commonMain/kotlin directory
        val srcCommonMainKotlinDir = addModuleSrcCommonMainKotlin(moduleDirectory)

        // Create files
        createTemplateFiles(
            dir = srcCommonMainKotlinDir,
            template = state.moduleLayer,
        )

        println("Info: Module $name created successfully")

        return moduleDirectory
    }

    fun getModuleDirectory(
        name: String,
        parentDirectory: File,
    ): File? {
        val moduleDirectory = File(parentDirectory, name)

        // Todo: Check whether the module already exists
        if (moduleDirectory.exists())
            return null.also {
                println("Info: Module $name already exists")
            }

        // Create the module directory
        moduleDirectory.mkdirs()

        return moduleDirectory
    }

    fun addModuleToSettingsGradle(
        moduleName: String,
        settingsGradleFile: File,
    ) {
        val lines = settingsGradleFile.readLines().toMutableList()

        var lastIncludeStartLine = -1
        var lastIncludeEndLine = -1
        var lastNonEmptyLine = -1

        lines.forEachIndexed { i, line ->
            val line = line.trim().replace(" ", "")

            if (line.startsWith("include("))
                lastIncludeStartLine = i

            if (lastIncludeEndLine < lastIncludeStartLine && line.endsWith(")"))
                lastIncludeEndLine = i

            if (line.isNotBlank())
                lastNonEmptyLine = i
        }

        val lastIncludeIndex =
            if (lastIncludeStartLine != -1 && lastIncludeEndLine != -1)
                lastIncludeEndLine
            else if (lastNonEmptyLine != -1)
                lastNonEmptyLine
            else
                lines.lastIndex

        val newIncludeLineIndex = lastIncludeIndex + 1

        lines.add(newIncludeLineIndex, "include(\":${moduleName}\")")

        Files.write(
            settingsGradleFile.toPath(),
            lines
        )
    }

    fun addModuleBuildGradleFile(
        moduleDirectory: File,
    ) {
        val buildGradleFile = File(moduleDirectory, "build.gradle.kts")

        // Create the build.gradle.kts file
        buildGradleFile.createNewFile()

        // Write the build.gradle.kts file
        buildGradleFile.writeText(buildGradleFileTemplate())
    }

    fun addModuleSrcCommonMainKotlin(
        moduleDirectory: File,
    ): File {
        val srcCommonMainKotlinDir = File(moduleDirectory, "src/commonMain/kotlin")

        // Create the src/commonMain/kotlin directory
        srcCommonMainKotlinDir.mkdirs()

        return srcCommonMainKotlinDir
    }

    fun createTemplateFiles(
        dir: File,
        template: ModuleTemplate,
    ) {
        template.files
    }

}