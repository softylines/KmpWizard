package com.github.mohamedrejeb.kmpwizard.parser.libs

import com.softylines.kmpwizard.parser.libs.*
import org.junit.*

class LibsBlockVersionsTest {

    @Test
    fun `test parse versions blocks`() {
        val lines = listOf(
            "kotlin=1.5.21",
            "kotlinx.coroutines=1.5.1",
            "kotlinx-serialization=1.2.2-RC1"
        )

        val block = LibsBlock.Versions(lines)

        val expectedLines =
            listOf(
                LibsLine.Version("kotlin", "1.5.21"),
                LibsLine.Version("kotlinx.coroutines", "1.5.1"),
                LibsLine.Version("kotlinx-serialization", "1.2.2-RC1")
            )

        assert(block.lines.size == 3)
        assert(block.lines == expectedLines)
    }

}