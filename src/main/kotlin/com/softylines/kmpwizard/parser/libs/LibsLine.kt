package com.softylines.kmpwizard.parser.libs

sealed interface LibsLine {
    data class Version(
        val name: String,
        val version: String
    ): LibsLine

    data class Library(
        val name: String,
        val module: String,
        val versionRef: String?
    ): LibsLine {

        constructor(
            name: String,
            group: String,
            moduleName: String,
            versionRef: String?
        ): this(
            name = name,
            module = "$group:$moduleName",
            versionRef = versionRef
        )

    }

    data class Plugin(
        val name: String,
        val id: String,
        val versionRef: String?
    ): LibsLine

    data class Bundle(
        val name: String,
        val libraries: List<String>,
    ): LibsLine
}
