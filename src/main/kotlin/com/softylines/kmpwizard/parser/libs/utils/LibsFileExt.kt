package com.softylines.kmpwizard.parser.libs.utils

import com.softylines.kmpwizard.parser.libs.*

/**
 * Check if the libs file has a version with the given name
 *
 * @param ref the version ref to check
 * @return true if the libs file has a version with the given name, false otherwise
 */
fun LibsFile.hasVersion(ref: String): Boolean {
    if (versionsBlock == null)
        return false

    return versionsBlock.lines.any { it.name == ref }
}

/**
 * Get the version with the given type
 *
 * @param versionType the version type to get
 * @return the version with the given name or null
 */
fun LibsFile.getVersion(versionType: LibsLine.VersionType?): String? {
    return when (versionType) {
        is LibsLine.VersionType.Version ->
            versionType.version

        is LibsLine.VersionType.VersionRef ->
            versionsBlock?.lines.orEmpty().firstOrNull { it.name == versionType.ref }?.version

        null ->
            null
    }
}

/**
 * Check if the libs file has a library with the given module
 *
 * @param module the module to check
 * @return true if the libs file has a library with the given module, false otherwise
 */
fun LibsFile.hasLibrary(module: String): Boolean {
    if (librariesBlock == null)
        return false

    return librariesBlock.lines.any { it.module == module }
}

/**
 * Get the library with the given module
 *
 * @param module the module to get
 * @return the library with the given module or null
 */
fun LibsFile.getLibrary(module: String): LibsLine.Library? {
    if (librariesBlock == null)
        return null

    return librariesBlock.lines.firstOrNull { it.module == module }
}

/**
 * Check if the libs file has a plugin with the given id
 *
 * @param pluginId the plugin id to check
 * @return true if the libs file has a plugin with the given id, false otherwise
 */
fun LibsFile.hasPlugin(pluginId: String): Boolean {
    if (pluginsBlock == null)
        return false

    return pluginsBlock.lines.any { it.id == pluginId }
}

/**
 * Get the plugin with the given id
 *
 * @param pluginId the plugin id to get
 * @return the plugin with the given id or null
 */
fun LibsFile.getPlugin(pluginId: String): LibsLine.Plugin? {
    if (pluginsBlock == null)
        return null

    return pluginsBlock.lines.firstOrNull { it.id == pluginId }
}
