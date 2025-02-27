package com.softylines.kmpwizard.writer.module

import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.FolderTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.parseContent
import com.softylines.kmpwizard.core.template.parseName
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.text.isNotBlank

object ModuleWriter {

    /**
     * Create a new module
     *
     * @param state The module maker state
     * @param parentDirectory The parent directory of the module
     * @param settingsGradleFile The settings.gradle.kts file
     * @return The module directory
     */
    fun createModuleList(
        state: ModuleMakerState,
        parentDirectory: File,
        settingsGradleFile: File,
    ): List<File> {
        return state.moduleTemplateList.mapNotNull { moduleTemplate ->
            createModuleList(
                state = state,
                moduleTemplate = moduleTemplate,
                parentDirectory = parentDirectory,
                settingsGradleFile = settingsGradleFile,
            )
        }
    }

    fun createModuleList(
        state: ModuleMakerState,
        moduleTemplate: ModuleTemplate,
        parentDirectory: File,
        settingsGradleFile: File,
    ): File? {
        val name = getModuleName(
            state = state,
            moduleTemplate = moduleTemplate,
        )

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
        addModuleBuildGradleFile(
            moduleDirectory = moduleDirectory,
            state = state,
            moduleTemplate = moduleTemplate,
        )

        // Add src/commonMain/kotlin directory
        val srcCommonMainKotlinDir = addModuleSrcCommonMainKotlin(moduleDirectory)

        // Create files
        createTemplateFiles(
            dir = srcCommonMainKotlinDir,
            state = state,
            moduleTemplate = moduleTemplate,
        )

        println("Info: Module $name created successfully")

        return moduleDirectory
    }

    fun getModuleName(
        state: ModuleMakerState,
        moduleTemplate: ModuleTemplate
    ): String {
        val name = (moduleTemplate.parent + ":" + state.moduleNameState.text)
            .split(":")
            .filter { it.isNotBlank() }
            .joinToString(
                separator = ":",
                prefix = ":"
            )

        return name
    }

    fun getModuleDirectory(
        name: String,
        parentDirectory: File,
    ): File? {

        val pathParts = name
            .split(":")
            .filter { it.isNotBlank() }

        val moduleDirectory =
            Path(parentDirectory.absolutePath, *pathParts.toTypedArray())
                .toFile()

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

        lines.add(newIncludeLineIndex, "include(\":${moduleName.removePrefix(":")}\")")

        Files.write(
            settingsGradleFile.toPath(),
            lines
        )
    }

    fun addModuleBuildGradleFile(
        moduleDirectory: File,
        state: ModuleMakerState,
        moduleTemplate: ModuleTemplate,
    ) {
        val buildGradleFile = File(moduleDirectory, "build.gradle.kts")

        // Create the build.gradle.kts file
        buildGradleFile.createNewFile()

        // Write the build.gradle.kts file
        buildGradleFile.writeText(
            text = moduleTemplate.buildGradleFile.parseContent(state)
        )
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
        state: ModuleMakerState,
        moduleTemplate: ModuleTemplate,
    ) {
        createTemplateFiles(
            dir = dir,
            files = moduleTemplate.files,
            state = state,
        )
    }

    fun createTemplateFiles(
        dir: File,
        files: List<IFileTemplate>,
        state: ModuleMakerState,
    ) {
        files.forEach { fileTemplate ->
            val file = File(dir, fileTemplate.parseName(state))

            if (fileTemplate is FileTemplate) {
                // Create the file
                if (!file.exists())
                    file.createNewFile()

                // Write the file
                file.writeText(
                    text = fileTemplate.parseContent(state)
                )
            }

            if (fileTemplate is FolderTemplate) {
                // Create the directory
                file.mkdirs()

                // Create the files in the directory
                createTemplateFiles(
                    dir = file,
                    files = fileTemplate.files,
                    state = state,
                )
            }
        }
    }

}