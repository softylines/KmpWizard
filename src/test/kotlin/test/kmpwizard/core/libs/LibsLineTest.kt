package test.kmpwizard.core.libs

import com.softylines.kmpwizard.core.libs.LibsLine
import kotlin.test.Test
import kotlin.test.assertEquals

class LibsLineTest {

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
            name = "compose-gradle-plugin",
            id = "org.jetbrains.compose:compose-gradle-plugin",
            versionType = LibsLine.VersionType.Version("1.0.0")
        )

        val expectedLine = "compose-gradle-plugin = { id = \"org.jetbrains.compose:compose-gradle-plugin\", version = \"1.0.0\" }"

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