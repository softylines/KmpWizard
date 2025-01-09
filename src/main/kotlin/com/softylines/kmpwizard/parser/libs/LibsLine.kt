package com.softylines.kmpwizard.parser.libs

sealed interface LibsLine {
    data class Version(
        val name: String,
        val version: String
    ): LibsLine

    data class Library(
        val name: String,
        val module: String,
        val versionType: VersionType?,
    ): LibsLine {

        constructor(
            name: String,
            group: String,
            moduleName: String,
            versionType: VersionType?
        ): this(
            name = name,
            module = "$group:$moduleName",
            versionType = versionType,
        )

    }

    data class Plugin(
        val name: String,
        val id: String,
        val versionType: VersionType?
    ): LibsLine

    data class Bundle(
        val name: String,
        val libraries: List<String>,
    ): LibsLine

    sealed interface VersionType {
        data class Version(
            val version: String
        ): VersionType

        data class VersionRef(
            val ref: String
        ): VersionType
    }
}
