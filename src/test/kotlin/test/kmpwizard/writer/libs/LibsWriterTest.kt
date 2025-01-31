package test.kmpwizard.writer.libs

import com.softylines.kmpwizard.core.libs.LibsLine
import com.softylines.kmpwizard.writer.libs.LibsWriter
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun `test write version and versions block`() {
        val lines = listOf(
            "[libraries]",
            "kotlin = { module = 'org.jetbrains.kotlin:kotlin-stdlib-jdk8', version = '1.0.0' }",
            "",
            "[plugins]",
            "kotlin = { id = 'org.jetbrains.kotlin.jvm', version = '1.0.0' }"
        )

        val expectedNewLine = "compose = \"1.7.3\""

        val newLines = LibsWriter.writeVersion(
            version = LibsLine.Version("compose", "1.7.3"),
            lines = lines
        )

        assertEquals("[versions]", newLines[newLines.lastIndex - 1])
        assertEquals(expectedNewLine, newLines[newLines.lastIndex])
        assertEquals(lines.size + 2, newLines.size)
        assertEquals(lines, newLines.dropLast(2))
    }


    @Test
    fun `test to line string version`() {
        val version = LibsLine.Version("compose", "1.7.3")

        val expectedLine = "compose = \"1.7.3\""

        assertEquals(expectedLine, version.toLineString())
    }

    @Test
    fun `test to line string library with version type`() {
        val library = LibsLine.Library(
            name = "gradlePlugin-android",
            module = "com.android.tools.build:gradle",
            versionType = LibsLine.VersionType.Version("7.0.0")
        )

        val expectedLine = "gradlePlugin-android = { module = \"com.android.tools.build:gradle\", version = \"7.0.0\" }"

        assertEquals(expectedLine, library.toLineString())
    }

    @Test
    fun `test to line string plugin with version type`() {
        val plugin = LibsLine.Plugin(
            name = "compose-gralde-plugin",
            id = "org.jetbrains.compose:compose-gradle-plugin",
            versionType = LibsLine.VersionType.Version("1.0.0")
        )

        val expectedLine = "compose-gralde-plugin = { id = \"org.jetbrains.compose:compose-gradle-plugin\", version = \"1.0.0\" }"

        assertEquals(expectedLine, plugin.toLineString())
    }

    @Test
    fun `test to line string library with version ref`() {
        val library = LibsLine.Library(
            name = "kotlin-test",
            module = "org.jetbrains.kotlin:kotlin-test",
            versionType = LibsLine.VersionType.VersionRef("kotlin")
        )

        val expectedLine = "kotlin-test = { module = \"org.jetbrains.kotlin:kotlin-test\", version.ref = \"kotlin\" }"

        assertEquals(expectedLine, library.toLineString())
    }

    @Test
    fun `test to line string bundle`() {
        val bundle = LibsLine.Bundle(
            name = "kotlinBundle",
            libraries = listOf("kotlin", "kotlinx")
        )

        val expectedLine = "kotlinBundle = [ \"kotlin\", \"kotlinx\" ]"

        assertEquals(expectedLine, bundle.toLineString())
    }




}