package com.softylines.kmpwizard.writer.libs

import com.softylines.kmpwizard.core.libs.*
import com.softylines.kmpwizard.core.libs.LibsUtils.VersionsName
import kotlin.io.path.*

class LibsWriter(private val path: String) {

    fun writeVersion(version: LibsLine.Version) {
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
                if (blockName == VersionsName) {
                    toAddLineIndex = lastNonEmptyLine + 1
                    break
                }

                lastNonEmptyLine = i
                blockName = currentBlockName
            }
        }

        val newLines = lines.toMutableList()
        newLines.add(toAddLineIndex, version.toLineString())

        return newLines.toList()
    }

}