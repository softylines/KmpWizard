package com.softylines.kmpwizard.core.libs

import com.softylines.kmpwizard.parser.libs.LibsBlock

object LibsUtils {

    /**
     * Check if the line is a comment
     *
     * @param line the line to check
     * @return true if the line is a comment, false otherwise
     */
    fun isComment(line: String): Boolean {
        return line.trim().startsWith("#")
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

        return if (blockName in LibsUtils.BlockTypes)
            blockName
        else
            null
    }

    const val VersionsName = "versions"
    const val LibrariesName = "libraries"
    const val PluginsName = "plugins"
    const val BundlesName = "bundles"

    val BlockTypes = listOf(VersionsName, LibrariesName, PluginsName, BundlesName)

}
