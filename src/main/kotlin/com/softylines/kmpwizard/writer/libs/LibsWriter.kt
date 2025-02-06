package com.softylines.kmpwizard.writer.libs

import com.softylines.kmpwizard.core.libs.*
import com.softylines.kmpwizard.core.libs.LibsUtils
import kotlin.io.path.*

object LibsWriter {

    fun writeVersion(
        path: String,
        version: LibsLine.Version
    ) {
        val lines = Path(path).readLines()

        val newLines = writeVersion(
            version = version,
            lines = lines,
        )

        Path(path).writeLines(newLines)
    }

    fun writeVersion(
        version: LibsLine.Version,
        lines: List<String>,
    ) : List<String> {
        return writeBlockLine(
            lineBlockName = LibsUtils.VersionsName,
            line = version.toLineString(),
            lines = lines
        )
    }

    fun writeLibrary(
        path: String,
        library: LibsLine.Library
    ) {
        val lines = Path(path).readLines()

        val newLines = writeLibrary(
            library = library,
            lines = lines,
        )

        Path(path).writeLines(newLines)
    }

    fun writeLibrary(
        library: LibsLine.Library,
        lines: List<String>,
    ) : List<String> {
        return writeBlockLine(
            lineBlockName = LibsUtils.LibrariesName,
            line = library.toLineString(),
            lines = lines
        )
    }

    fun writePlugin(
        path: String,
        plugin: LibsLine.Plugin
    ) {
        val lines = Path(path).readLines()

        val newLines = writePlugin(
            plugin = plugin,
            lines = lines,
        )

        Path(path).writeLines(newLines)
    }

    fun writePlugin(
        plugin: LibsLine.Plugin,
        lines: List<String>,
    ) : List<String> {
        return writeBlockLine(
            lineBlockName = LibsUtils.PluginsName,
            line = plugin.toLineString(),
            lines = lines
        )
    }



    fun writeBundle(
        path: String,
        bundle: LibsLine.Bundle
    ) {
        val lines = Path(path).readLines()

        val newLines = writeBundle(
            bundle = bundle,
            lines = lines,
        )

        Path(path).writeLines(newLines)
    }

    fun writeBundle(
        bundle: LibsLine.Bundle,
        lines: List<String>,
    ) : List<String> {
        return writeBlockLine(
            lineBlockName = LibsUtils.BundlesName,
            line = bundle.toLineString(),
            lines = lines
        )
    }

    fun writeBlockLine(
        lineBlockName: String,
        line: String,
        lines: List<String>,
    ) : List<String> {
        var blockName: String? = null
        var lastNonEmptyLine: Int = -1
        var toAddLineIndex: Int = -1

        for (i in lines.indices) {
            // Remove all whitespaces
            val line = lines[i]
                .replace(" ", "")
                .replace("\"", "")
                .replace("'", "")

            // Skip empty lines
            if (line.isBlank() || LibsUtils.isComment(line = line))
                continue

            // Check if the line is a block type
            val currentBlockName = LibsUtils.getBlockName(line = line)

            // Set the last non-empty line
            if (currentBlockName == null)
                lastNonEmptyLine = i

            // If the block type is not null, we add the block lines to the group map
            if (currentBlockName != null || i == lines.lastIndex) {
                if (blockName == lineBlockName) {
                    toAddLineIndex = lastNonEmptyLine + 1
                    break
                }

                lastNonEmptyLine = i
                blockName = currentBlockName
            }
        }

        val newLines = lines.toMutableList()

        if (toAddLineIndex == -1) {
            newLines.add("[$lineBlockName]")
            toAddLineIndex = newLines.size
        }

        newLines.add(toAddLineIndex, line)

        return newLines.toList()
    }

}