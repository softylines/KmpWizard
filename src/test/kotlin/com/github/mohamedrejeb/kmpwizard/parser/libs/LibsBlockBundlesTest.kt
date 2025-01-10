package com.github.mohamedrejeb.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.LibsLine
import com.softylines.kmpwizard.parser.libs.LibsParser
import org.junit.Test

class LibsBlockBundlesTest {

    @Test
    fun `test parse bundles blocks`() {
        val lines = listOf(
            "ktor = [",
            "core,",
            "client,",
            "engine-cio",
            "]",
            "coil=[core,compose]"
        )

        val block = LibsParser.parseBundles(lines)

        val expectedLines = listOf(
            LibsLine.Bundle(
                name = "ktor",
                libraries = listOf("core", "client", "engine-cio")
            ),
            LibsLine.Bundle(
                name = "coil",
                libraries = listOf("core", "compose")
            )
        )

        assert(block.lines == expectedLines)
    }

}