package com.softylines.kmpwizard.core.libs

sealed interface LibsLine {

    fun toLineString(): String

    data class Version(
        val name: String,
        val version: String
    ): LibsLine {

        override fun toLineString(): String {
            return "$name = \"$version\""
        }

    }

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

        override fun toLineString(): String {
            return buildString {
                append(name)
                append(" = { ")
                append("module = \"$module\"")
                versionType?.let { append(", ${it.toVersionString()}") }
                append(" }")
            }
        }

    }

    data class Plugin(
        val name: String,
        val id: String,
        val versionType: VersionType?
    ): LibsLine {

        override fun toLineString(): String {
            return buildString {
                append(name)
                append(" = { ")
                append("id = \"$id\"")
                versionType?.let { append(", ${it.toVersionString()}") }
                append(" }")
            }
        }

    }

    data class Bundle(
        val name: String,
        val libraries: List<String>,
    ): LibsLine {

        // Todo: Return formatted bundle with multiple lines
        override fun toLineString(): String {
            return buildString {
                append(name)
                append(" = [ ")
                libraries.forEachIndexed { i, library ->
                    append("\"$library\"")
                    if (i < libraries.lastIndex) append(", ")
                }
                append(" ]")
            }
        }

    }

    sealed interface VersionType {
        fun toVersionString(): String

        data class Version(
            val version: String
        ): VersionType {

            override fun toVersionString(): String {
                return "version = \"$version\""
            }

        }

        data class VersionRef(
            val ref: String
        ): VersionType {

            override fun toVersionString(): String {
                return "version.ref = \"$ref\""
            }

        }
    }
}