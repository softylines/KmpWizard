package test.kmpwizard.writer.libs

import com.softylines.kmpwizard.core.libs.LibsLine
import com.softylines.kmpwizard.writer.libs.LibsWriter
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class LibsWriterTest {

    @Test
    fun `test write version line 1`() {
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

        val expectedNewLine = "compose = \"1.7.3\""

        val newLines = LibsWriter.writeVersion(
            version = LibsLine.Version("compose", "1.7.3"),
            lines = lines
        )
        val expectedNewLines = lines.toMutableList().also {
            it.add(4, expectedNewLine)
        }

        assertEquals(expectedNewLines, newLines)
    }

    @Test
    fun `test write version line 2`() {
        val lines = listOf(
            "[versions]",
            "",
            "",
            "[libraries]",
            "kotlin = { module = 'org.jetbrains.kotlin:kotlin-stdlib-jdk8', version.ref = 'kotlin' }",
            "",
            "[plugins]",
            "kotlin = { id = 'org.jetbrains.kotlin.jvm', version.ref = 'kotlin' }"
        )

        val expectedNewLine = "compose = \"1.7.3\""

        val newLines = LibsWriter.writeVersion(
            version = LibsLine.Version("compose", "1.7.3"),
            lines = lines
        )
        val expectedNewLines = lines.toMutableList().also {
            it.add(1, expectedNewLine)
        }

        assertEquals(expectedNewLines, newLines)
    }

    @Test
    fun `test write version line 3`() {
        val lines = listOf(
            "[libraries]",
            "kotlin = { module = 'org.jetbrains.kotlin:kotlin-stdlib-jdk8', version.ref = 'kotlin' }",
            "",
            "[versions]",
            "",
            "",
            "[plugins]",
            "kotlin = { id = 'org.jetbrains.kotlin.jvm', version.ref = 'kotlin' }"
        )

        val expectedNewLine = "compose = \"1.7.3\""

        val newLines = LibsWriter.writeVersion(
            version = LibsLine.Version("compose", "1.7.3"),
            lines = lines
        )
        val expectedNewLines = lines.toMutableList().also {
            it.add(4, expectedNewLine)
        }

        assertEquals(expectedNewLines, newLines)
    }

}