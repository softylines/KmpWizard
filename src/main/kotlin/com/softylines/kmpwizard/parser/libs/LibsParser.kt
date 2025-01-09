package com.softylines.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.LibsUtils
import com.softylines.kmpwizard.parser.libs.LibsBlock.Bundles
import com.softylines.kmpwizard.parser.libs.LibsBlock.Libraries
import com.softylines.kmpwizard.parser.libs.LibsBlock.Plugins
import com.softylines.kmpwizard.parser.libs.LibsBlock.Versions
import kotlin.io.path.Path
import kotlin.io.path.readLines

class LibsParser(private val path: String) {

    fun parse(): LibsFile {
        val lines = Path(path).readLines()
        return parse(lines)
    }

    /**
     * Parse the lines and group them by block type
     *
     * @param lines the lines to parse
     */
    fun parse(lines: List<String>): LibsFile {
        var libsFile = LibsFile()

        var blockName: String? = null
        val blockLines = mutableListOf<String>()

        lines.forEachIndexed { i, line ->
            // Remove all whitespaces
            val line = line
                .replace(" ", "")
                .replace("\"", "")
                .replace("'", "")

            // Skip empty lines
            if (line.isBlank() || LibsUtils.isComment(line))
                return@forEachIndexed

            // Check if the line is a block type
            val currentBlockName = LibsUtils.getBlockName(line)

            // Add the line to the block lines
            if (currentBlockName == null) {
                blockLines.add(line)
            }

            // If the block type is not null, we add the block lines to the group map
            if (currentBlockName != null || i == lines.lastIndex) {
                if (blockName != null) {
                    val lines = blockLines.toList()

                    when (blockName) {
                        LibsUtils.VersionsName ->
                            libsFile = libsFile.copy(
                                versionsBlock = Versions(lines),
                            )

                        LibsUtils.LibrariesName ->
                            libsFile = libsFile.copy(
                                librariesBlock = Libraries(lines),
                            )

                        LibsUtils.PluginsName ->
                            libsFile = libsFile.copy(
                                pluginsBlock = Plugins(lines),
                            )

                        LibsUtils.BundlesName ->
                            libsFile = libsFile.copy(
                                bundlesBlock = Bundles(lines),
                            )

                        else -> null
                    }
                }

                blockLines.clear()
                blockName = currentBlockName
            }
        }

        return libsFile
    }

}