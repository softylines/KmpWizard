package com.softylines.kmpwizard.parser.conventionplugins

import java.io.File
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.orEmpty
import kotlin.io.extension
import kotlin.io.nameWithoutExtension
import kotlin.io.path.Path

object ConventionPluginParser {

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
                it != null && it.isFile && it.name.endsWith(".gradle.kts")
            }
            .map {
                it.name.removeSuffix(".gradle.kts")
            }
    }

}