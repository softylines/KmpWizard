package com.softylines.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.LibsLine

object LibsBlock {

    data class Versions(
        val lines: List<LibsLine.Version>,
    )

    data class Libraries(
        val lines: List<LibsLine.Library>,
    )

    data class Plugins(
        val lines: List<LibsLine.Plugin>,
    )

    data class Bundles(
        val lines: List<LibsLine.Bundle>,
    )

}