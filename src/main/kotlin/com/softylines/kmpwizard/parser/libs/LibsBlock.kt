package com.softylines.kmpwizard.parser.libs

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

                        val version =
                            map.keys
                                .firstOrNull { it.startsWith("version") }
                                ?.let { map[it] }

                        if (group != null && moduleName != null)
                            LibsLine.Library(
                                name = name,
                                group = group,
                                moduleName = moduleName,
                                versionRef = version,
                            )
                        else if (module != null)
                            LibsLine.Library(
                                name = name,
                                module = module,
                                versionRef = version,
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

                        val version =
                            map.keys
                                .firstOrNull { it.startsWith("version") }
                                ?.let { map[it] }

                        if (id != null)
                            LibsLine.Plugin(
                                name = name,
                                id = id,
                                versionRef = version
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

    companion object {
        const val VersionsName = "versions"
        const val LibrariesName = "libraries"
        const val PluginsName = "plugins"
        const val BundlesName = "bundles"

        val BlockTypes = listOf(VersionsName, LibrariesName, PluginsName, BundlesName)

        fun create(
            name: String,
            lines: List<String>
        ): LibsBlock<LibsLine>? {
            return when (name) {
                VersionsName -> Versions(lines)
                LibrariesName -> Libraries(lines)
                PluginsName -> Plugins(lines)
                BundlesName -> Bundles(lines)
                else -> null
            }
        }
    }

}