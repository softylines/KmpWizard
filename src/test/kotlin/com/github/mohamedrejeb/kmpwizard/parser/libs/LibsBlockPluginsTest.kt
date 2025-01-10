package com.github.mohamedrejeb.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.LibsLine
import com.softylines.kmpwizard.parser.libs.LibsParser
import org.junit.Test

class LibsBlockPluginsTest {

    @Test
    fun `test parse plugins blocks`() {
        val lines = listOf(
            "kotlin={id=org.jetbrains.kotlin,version.ref=kotlin}",
            "compose={id=org.jetbrains.compose,version=1.7.1}",
        )

        val block = LibsParser.parsePlugins(lines)

        val expectedLines = listOf(
            LibsLine.Plugin(
                name = "kotlin",
                id = "org.jetbrains.kotlin",
                versionType = LibsLine.VersionType.VersionRef(ref = "kotlin")
            ),
            LibsLine.Plugin(
                name = "compose",
                id = "org.jetbrains.compose",
                versionType = LibsLine.VersionType.Version(version = "1.7.1")
            )
        )

        println("block.lines = ${block.lines}")
        println("expectedLines = $expectedLines")

        assert(block.lines == expectedLines)
    }

}