package com.softylines.kmpwizard.core.template

import com.softylines.kmpwizard.core.template.IFileTemplate.Companion.ModuleNameKey
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState

interface IFileTemplate {
    // user-profile
    // UserProfileRepository
    val name: String
    val parent: FolderTemplate?

    companion object {
        const val ModuleNameKey = "ModuleName"
        const val ModuleNameKeyDollar = "$$ModuleNameKey$"
    }
}

val IFileTemplate.parentList: List<FolderTemplate>
    get() {
        val parentList = mutableListOf<FolderTemplate>()
        var parent = parent

        while (parent != null) {
            parentList.add(parent)
            parent = parent.parent
        }

        return parentList.reversed()
    }

val IFileTemplate.packageName: String
    get() = parentList.joinToString(".") { it.name }

fun FileTemplate.parseContent(state: ModuleMakerState): String {
    return parseArguments(content, state)
}

fun IFileTemplate.parseName(state: ModuleMakerState): String {
    return parseArguments(name, state)
}

fun IFileTemplate.parseArguments(
    string: String,
    state: ModuleMakerState
): String {
    var dollarCount = string.count { it == '$' }

    if (dollarCount < 2)
        return string

    val stringBuilder = StringBuilder()

    var i = 0

    while (true) {
        val startDollarIndex = string.indexOf('$', i)
        val endDollarIndex = string.indexOf('$', startDollarIndex + 1)

        if (startDollarIndex == -1 || endDollarIndex == -1) {
            stringBuilder.append(string.substring(i))
            break
        }

        if (startDollarIndex > i)
            stringBuilder.append(string.substring(i, startDollarIndex))

        val variable =
            if (endDollarIndex - startDollarIndex == 1)
                ""
            else
                string.substring(startDollarIndex + 1, endDollarIndex)

        if (variable == ModuleNameKey)
            stringBuilder.append(formatModuleName(state.moduleNameState.text.toString()))
        else
            stringBuilder.append(string.substring(startDollarIndex, endDollarIndex + 1))

        i = endDollarIndex + 1
    }

    return stringBuilder.toString()
}

fun IFileTemplate.formatModuleName(moduleName: String): String {
    val isFile = this is FileTemplate
    val stringBuilder = StringBuilder()
    var currentSequence = ""

    for (i in moduleName.indices) {
        val char = moduleName[i]

        if (char.isLetter()) {
            currentSequence += char
        }

        if (
            (!char.isLetter() || i == moduleName.lastIndex) &&
            currentSequence.isNotBlank()
        ) {
            val sequence = currentSequence
                .trim()
                .lowercase()
                .replaceFirstChar {
                    if (isFile)
                        it.uppercaseChar()
                    else
                        it
                }

            stringBuilder.append(sequence)
            currentSequence = ""
        }
    }

    return stringBuilder.toString()
}
