package com.github.mohamedrejeb.kmpwizard.parser.libs

import com.intellij.testFramework.assertInstanceOf
import com.softylines.kmpwizard.parser.libs.LibsBlock
import com.softylines.kmpwizard.parser.libs.LibsParser
import org.junit.Before
import org.junit.Test

class LibsParserTest {

    lateinit var parser: LibsParser

    @Before
    fun setup() {
        parser = LibsParser("build.gradle.kts")
    }

    @Test
    fun `test get 'versions' block type`() {
        val line = "[versions]"
        val blockType = parser.getBlockName(line)

        assert(blockType == LibsBlock.VersionsName)
    }

    @Test
    fun `test get wrong block type`() {
        val line = "versions]"
        val blockType = parser.getBlockName(line)

        assert(blockType == null)
    }

    @Test
    fun `test get comment`() {
        val line = "# Kotlin"
        val blockType = parser.getBlockName(line)

        assert(blockType == null)
    }

    @Test
    fun `test is comment success`() {
        val line = "# Kotlin"
        val isComment = parser.isComment(line)

        assert(isComment)
    }

    @Test
    fun `test is comment failure`() {
        val line = "kotlin = '1.5.21'"
        val isComment = parser.isComment(line)

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

        val blocks = parser.parse(lines)

        val expectedVersionsBlock = LibsBlock.Versions(
            lines
                .subList(2, 4)
                .map { removeWhitespaces(it) }
        )
        val expectedLibrariesBlock = LibsBlock.Libraries(
            lines
                .subList(6, 7)
                .map { removeWhitespaces(it) }
        )
        val expectedPluginsBlock = LibsBlock.Plugins(
            lines
                .subList(9, 10)
                .map { removeWhitespaces(it) }
        )

        assert(blocks.size == 3)

        val versionsBlock = assertInstanceOf<LibsBlock.Versions>(blocks[0])
        val librariesBlock = assertInstanceOf<LibsBlock.Libraries>(blocks[1])
        val pluginsBlock = assertInstanceOf<LibsBlock.Plugins>(blocks[2])

        assert(versionsBlock.lines == expectedVersionsBlock.lines)
        assert(librariesBlock.lines == expectedLibrariesBlock.lines)
        assert(pluginsBlock.lines == expectedPluginsBlock.lines)
    }

    fun removeWhitespaces(line: String): String {
        return line
            .replace(" ", "")
            .replace("\"", "")
            .replace("'", "")
    }

}