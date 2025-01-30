package com.softylines.kmpwizard.parser.libs

import com.softylines.kmpwizard.core.libs.*
import kotlin.io.path.*

object LibsParser {

    fun parse(path: String): LibsFile {
        val lines = Path(path).readLines()
        return parse(lines, path)
    }

    /**
     * Parse the lines and group them by block type
     *
     * @param lines the lines to parse
     */
    fun parse(
        lines: List<String>,
        libsPath: String = "libs.versions.toml",
    ): LibsFile {
        val libsFile = LibsFile(libsPath)

        var blockName: String? = null
        val blockLines = mutableListOf<String>()

        lines.forEachIndexed { i, line ->
            // Remove all whitespaces
            val line = line
                .replace(" ", "")
                .replace("\"", "")
                .replace("'", "")

            // Skip empty lines
            if (line.isBlank() || LibsUtils.isComment(line))
                return@forEachIndexed

            // Check if the line is a block type
            val currentBlockName = LibsUtils.getBlockName(line)

            // Add the line to the block lines
            if (currentBlockName == null) {
                blockLines.add(line)
            }

            // If the block type is not null, we add the block lines to the group map
            if (currentBlockName != null || i == lines.lastIndex) {
                if (blockName != null) {
                    val lines = blockLines.toList()

                    when (blockName) {
                        LibsUtils.VersionsName ->
                            libsFile.versionsBlock = parseVersions(lines)

                    LibsUtils.LibrariesName ->
                            libsFile.librariesBlock = parseLibraries(lines)

                        LibsUtils.PluginsName ->
                            libsFile.pluginsBlock = parsePlugins(lines)

                        LibsUtils.BundlesName ->
                            libsFile.bundlesBlock = parseBundles(lines)

                        else -> null
                    }
                }

                blockLines.clear()
                blockName = currentBlockName
            }
        }

        return libsFile
    }

    fun parseVersions(lines: List<String>): LibsBlock.Versions {
        val versionsBlockLines =
            lines.mapNotNull { line ->
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

        return LibsBlock.Versions(
            lines = versionsBlockLines,
        )
    }

    fun parseLibraries(lines: List<String>): LibsBlock.Libraries {
        val librariesBlockLines =
            lines.mapNotNull { line ->
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

        return LibsBlock.Libraries(
            lines = librariesBlockLines,
        )
    }

    fun parsePlugins(lines: List<String>): LibsBlock.Plugins {
        val pluginsBlockLines =
            lines.mapNotNull { line ->
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

        return LibsBlock.Plugins(
            lines = pluginsBlockLines,
        )
    }

    fun parseBundles(lines: List<String>): LibsBlock.Bundles {
        // Step 1: Group lines by bundle
        val groupedLines = mutableListOf<String>()

        var currentGroup = ""

        lines.forEach { line ->
            currentGroup += line.trim()

            if (line.endsWith(']')) {
                groupedLines.add(currentGroup)
                currentGroup = ""
            }
        }

        // Step 2: Parse each bundle
        val bundlesBlockLines = groupedLines.mapNotNull { group ->
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

        return LibsBlock.Bundles(
            lines = bundlesBlockLines,
        )
    }

    private fun isLibraryMapValid(map: Map<String, String>): Boolean {
        val isGroupMap = "group" in map.keys && "name" in map.keys
        val isModuleMap = "module" in map.keys

        return isGroupMap || isModuleMap
    }

    private fun isPluginMapValid(map: Map<String, String>): Boolean {
        return "id" in map.keys
    }

    private fun parserLibraryMap(map: String): Map<String, String> {
        return map.split(",").associate {
            val (key, value) = it.split("=")
            key.trim() to value.trim()
        }
    }

    private fun parseVersionType(map: Map<String, String>): LibsLine.VersionType? =
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