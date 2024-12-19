package com.softylines.kmpwizard.parser.libs

import kotlin.io.path.Path
import kotlin.io.path.readLines

class LibsParser(private val path: String) {

    fun parse() {
        val lines = Path(path).readLines()
        parse(lines)
    }

    /**
     * Parse the lines and group them by block type
     *
     * @param lines the lines to parse
     */
    fun parse(lines: List<String>): List<LibsBlock<LibsLine>> {
        val blockList = mutableListOf<LibsBlock<LibsLine>>()

        var blockName: String? = null
        val blockLines = mutableListOf<String>()

        lines.forEachIndexed { i, line ->
            // Remove all whitespaces
            val line = line
                .replace(" ", "")
                .replace("\"", "")
                .replace("'", "")

            // Skip empty lines
            if (line.isBlank() || isComment(line))
                return@forEachIndexed

            // Check if the line is a block type
            val currentBlockName = getBlockName(line)

            // Add the line to the block lines
            if (currentBlockName == null) {
                blockLines.add(line)
            }

            // If the block type is not null, we add the block lines to the group map
            if (currentBlockName != null || i == lines.lastIndex) {
                if (blockName != null) {
                    val libsBlock = LibsBlock.create(blockName, blockLines.toList())

                    if (libsBlock != null)
                        blockList.add(libsBlock)
                }

                blockLines.clear()
                blockName = currentBlockName
            }
        }

        return blockList.toList()
    }

    /**
     * Get the block type from the line
     *
     * @param line the line to check
     * @return the block type or null if the line is not a block type
     */
    fun getBlockName(line: String): String? {
        if (line.length < 3 || line.first() != '[' || line.last() != ']')
            return null

        val blockName = line.substring(1, line.length - 1)

        return if (blockName in LibsBlock.BlockTypes)
            blockName
        else
            null
    }

    /**
     * Check if the line is a comment
     *
     * @param line the line to check
     * @return true if the line is a comment, false otherwise
     */
    fun isComment(line: String): Boolean {
        return line.trim().startsWith("#")
    }

}