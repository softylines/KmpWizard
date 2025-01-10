package com.github.mohamedrejeb.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.LibsLine
import com.softylines.kmpwizard.parser.libs.LibsParser
import org.junit.Test

class LibsBlockLibrariesTest {

    @Test
    fun `test parse libraries blocks`() {
        val lines = listOf(
            "compose={ group=androidx.compose, name=compose-ui, version=1.0.0 }",
            "compose-material={ module=androidx.compose:compose-material, version.ref=material }",
        )

        val block = LibsParser.parseLibraries(lines)

        val expectedLines = listOf(
            LibsLine.Library(
                name = "compose",
                group = "androidx.compose",
                moduleName = "compose-ui",
                versionType = LibsLine.VersionType.Version("1.0.0")
            ),
            LibsLine.Library(
                name = "compose-material",
                module = "androidx.compose:compose-material",
                versionType = LibsLine.VersionType.VersionRef(ref = "material")
            )
        )

        assert(block.lines == expectedLines)
    }

}