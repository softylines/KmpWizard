package com.softylines.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.LibsLine
import kotlin.text.startsWith

// Todo: Sepereate parsing blocks and blocks state
sealed interface LibsBlock<out T : LibsLine> {

    val lines: List<T>

    class Versions(
        initialLines: List<String>,
    ) : LibsBlock<LibsLine.Version> {

        private var _lines: List<LibsLine.Version> = emptyList()
        override val lines: List<LibsLine.Version> get() = _lines

        init {
            _lines =
                initialLines.mapNotNull { line ->
                    val parts = line.split("=")
                    if (parts.size == 2) {
                        val name = parts[0].trim()
                        val version = parts[1].trim()

                        LibsLine.Version(
                            name = name,
                            version = version
                        )
                    } else {
                        null
                    }
                }
        }

    }

    class Libraries(
        initialLines: List<String>,
    ) : LibsBlock<LibsLine.Library> {

        private var _lines: List<LibsLine.Library> = emptyList()
        override val lines: List<LibsLine.Library> get() = _lines

        init {
            _lines =
                initialLines.mapNotNull { line ->
                    val parts = line.split("=", limit = 2)
                    if (parts.size == 2) {
                        val name = parts[0].trim()
                        val mapString = parts[1].trim().substringAfter("{").substringBefore("}")
                        val map = parserLibraryMap(mapString)

                        if (!isLibraryMapValid(map))
                            return@mapNotNull null

                        val group = map["group"]
                        val moduleName = map["name"]
                        val module = map["module"]

                        val versionType = parseVersionType(map)

                        if (group != null && moduleName != null)
                            LibsLine.Library(
                                name = name,
                                group = group,
                                moduleName = moduleName,
                                versionType = versionType,
                            )
                        else if (module != null)
                            LibsLine.Library(
                                name = name,
                                module = module,
                                versionType = versionType,
                            )
                        else
                            null
                    } else {
                        null
                    }
                }
        }

        fun isLibraryMapValid(map: Map<String, String>): Boolean {
            val isGroupMap = "group" in map.keys && "name" in map.keys
            val isModuleMap = "module" in map.keys

            return isGroupMap || isModuleMap
        }

    }

    class Plugins(
        initialLines: List<String>,
    ) : LibsBlock<LibsLine.Plugin> {

        private var _lines: List<LibsLine.Plugin> = emptyList()
        override val lines: List<LibsLine.Plugin> get() = _lines

        init {
            _lines =
                initialLines.mapNotNull { line ->
                    val parts = line.split("=", limit = 2)
                    if (parts.size == 2) {
                        val name = parts[0].trim()
                        val mapString = parts[1].trim().substringAfter("{").substringBefore("}")
                        val map = parserLibraryMap(mapString)

                        if (!isPluginMapValid(map))
                            return@mapNotNull null

                        val id = map["id"]

                        val versionType = parseVersionType(map)

                        if (id != null)
                            LibsLine.Plugin(
                                name = name,
                                id = id,
                                versionType = versionType
                            )
                        else
                            null
                    } else {
                        null
                    }
                }
        }

        fun isPluginMapValid(map: Map<String, String>): Boolean {
            return "id" in map.keys
        }
    }

    class Bundles(
        initialLines: List<String>,
    ) : LibsBlock<LibsLine.Bundle> {

        private var _lines: List<LibsLine.Bundle> = emptyList()
        override val lines: List<LibsLine.Bundle> get() = _lines

        init {
            // Step 1: Group lines by bundle
            val groupedLines = mutableListOf<String>()

            var currentGroup = ""

            initialLines.forEach { line ->
                currentGroup += line.trim()

                if (line.endsWith(']')) {
                    groupedLines.add(currentGroup)
                    currentGroup = ""
                }
            }

            // Step 2: Parse each bundle
            _lines = groupedLines.mapNotNull { group ->
                val parts = group.split("=")
                if (parts.size == 2) {
                    val name = parts[0].trim()
                    val libraries = parts[1]
                        .trim()
                        .substringAfter("[")
                        .substringBefore("]")
                        .split(",")
                        .map { it.trim() }

                    LibsLine.Bundle(
                        name = name,
                        libraries = libraries
                    )
                } else {
                    null
                }
            }
        }

    }

    fun parserLibraryMap(map: String): Map<String, String> {
        println("map: $map")
        return map.split(",").associate {
            val (key, value) = it.split("=")
            key.trim() to value.trim()
        }
    }

    fun parseVersionType(map: Map<String, String>): LibsLine.VersionType? =
            map.keys
                .firstOrNull { it.startsWith("version") }
                ?.let {
                    val key = it
                    val value = map[it] ?: return@let null

                    when (key) {
                        "version" ->
                            LibsLine.VersionType.Version(value)

                        "version.ref" ->
                            LibsLine.VersionType.VersionRef(value)

                        else ->
                            null
                    }
                }

}