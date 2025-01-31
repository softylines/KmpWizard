package com.softylines.kmpwizard.core.libs

import com.softylines.kmpwizard.writer.libs.LibsWriter

data class LibsFile(
    val path: String,
    var versionsBlock: LibsBlock.Versions? = null,
    var librariesBlock: LibsBlock.Libraries? = null,
    var pluginsBlock: LibsBlock.Plugins? = null,
    var bundlesBlock: LibsBlock.Bundles? = null,
)

fun LibsFile.getVersionOrCreate(
    name: String,
    defaultValue: String,
): LibsLine.Version {
    val versionLine = versionsBlock?.lines?.find { it.name == name }

    if (versionLine != null)
        return versionLine

    val newVersionLine = LibsLine.Version(
        name = name,
        version = defaultValue
    )

    LibsWriter.writeVersion(
        path = path,
        version = newVersionLine
    )

    versionsBlock = versionsBlock?.let {
        it.copy(lines = it.lines + newVersionLine)
    }

    return newVersionLine
}

fun LibsFile.getLibraryOrCreate(
    name: String,
    module: String,
    versionType: LibsLine.VersionType?,
): LibsLine.Library {
    val libraryLine = librariesBlock?.lines?.find { it.module == module }

    if (libraryLine != null)
        return libraryLine

    var newLibraryName = name

    // check in existing libraries that this name doesn't exist
    // otherwise add number and keep incrementing and searching
    var i = 1
    while (librariesBlock?.lines?.any { it.name == newLibraryName } == true) {
        newLibraryName = "$name$i"
        i++
    }

    val newLibraryLine = LibsLine.Library(
        name = newLibraryName,
        module = module,
        versionType = versionType,
    )

    LibsWriter.writeLibrary(
        path = path,
        library = newLibraryLine
    )

    librariesBlock = librariesBlock?.let {
        it.copy(lines = it.lines + newLibraryLine)
    }

    return newLibraryLine
}

fun LibsFile.getPluginOrCreate(
    name: String,
    id: String,
    versionType: LibsLine.VersionType?,
): LibsLine.Plugin {
    val pluginLine = pluginsBlock?.lines?.find { it.id == id }

    if (pluginLine != null)
        return pluginLine

    var newPluginName = name

    // check in existing libraries that this name doesn't exist
    // otherwise add number and keep incrementing and searching
    var i = 1
    while (librariesBlock?.lines?.any { it.name == newPluginName } == true) {
        newPluginName = "$name$i"
        i++
    }

    val newPluginLine = LibsLine.Plugin(
        name = newPluginName,
        id = id,
        versionType = versionType,
    )

    LibsWriter.writePlugin(
        path = path,
        plugin = newPluginLine
    )

    pluginsBlock = pluginsBlock?.let {
        it.copy(lines = it.lines + newPluginLine)
    }

    return newPluginLine
}

