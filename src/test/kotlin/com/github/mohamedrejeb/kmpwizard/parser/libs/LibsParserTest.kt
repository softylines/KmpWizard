package com.github.mohamedrejeb.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.LibsUtils
import com.softylines.kmpwizard.parser.libs.LibsBlock
import com.softylines.kmpwizard.parser.libs.LibsParser
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class LibsParserTest {

    @Test
    fun `test get 'versions' block type`() {
        val line = "[versions]"
        val blockType = LibsUtils.getBlockName(line)

        assertEquals(blockType, LibsUtils.VersionsName)
    }

    @Test
    fun `test get wrong block type`() {
        val line = "versions]"
        val blockType = LibsUtils.getBlockName(line)

        assertNull(blockType)
    }

    @Test
    fun `test get comment`() {
        val line = "# Kotlin"
        val blockType = LibsUtils.getBlockName(line)

        assertNull(blockType)
    }

    @Test
    fun `test is comment success`() {
        val line = "# Kotlin"
        val isComment = LibsUtils.isComment(line)

        assert(isComment)
    }

    @Test
    fun `test is comment failure`() {
        val line = "kotlin = '1.5.21'"
        val isComment = LibsUtils.isComment(line)

        assert(!isComment)
    }

    @Test
    fun testParseLines() {
        val lines = listOf(
            "[versions]",
            "# Kotlin",
            "kotlin = '1.5.21'",
            "kotlinx = '1.5.0'",
            "",
            "[libraries]",
            "kotlin = { module = 'org.jetbrains.kotlin:kotlin-stdlib-jdk8', version.ref = 'kotlin' }",
            "",
            "[plugins]",
            "kotlin = { id = 'org.jetbrains.kotlin.jvm', version.ref = 'kotlin' }"
        )

        val libsFile = LibsParser.parse(lines)

        val expectedVersionsBlock = LibsParser.parseVersions(
            lines
                .subList(2, 4)
                .map { removeWhitespaces(it) }
        )
        val expectedLibrariesBlock = LibsParser.parseLibraries(
            lines
                .subList(6, 7)
                .map { removeWhitespaces(it) }
        )
        val expectedPluginsBlock = LibsParser.parsePlugins(
            lines
                .subList(9, 10)
                .map { removeWhitespaces(it) }
        )

        assertNotNull(libsFile.versionsBlock)
        assertNotNull(libsFile.librariesBlock)
        assertNotNull(libsFile.pluginsBlock)
        assertNull(libsFile.bundlesBlock)

        assert(libsFile.versionsBlock.lines == expectedVersionsBlock.lines)
        assert(libsFile.librariesBlock.lines == expectedLibrariesBlock.lines)
        assert(libsFile.pluginsBlock.lines == expectedPluginsBlock.lines)
    }

    fun removeWhitespaces(line: String): String {
        return line
            .replace(" ", "")
            .replace("\"", "")
            .replace("'", "")
    }

}