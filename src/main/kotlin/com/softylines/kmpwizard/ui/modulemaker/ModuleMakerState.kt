package com.softylines.kmpwizard.ui.modulemaker

import com.softylines.kmpwizard.core.utils.State
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import com.softylines.kmpwizard.ui.modulemaker.layer.UserModuleTemplate

data class ModuleMakerState(
    val moduleNameState: TextFieldState = TextFieldState(),
    val packageNameState: TextFieldState = TextFieldState(),
    val moduleTemplateList: Set<ModuleTemplate> = setOf(ModuleTemplate.Empty),
    val conventionPlugins: State<List<String>> = State(),
    val initConventionPlugin: State<Unit> = State(),

    // User-defined templates
    val userTemplates: List<UserModuleTemplate> = emptyList(),
    val selectedUserTemplate: UserModuleTemplate? = null,
    val isEditingTemplate: Boolean = false,

    // Template creation/editing fields
    val templateNameState: TextFieldState = TextFieldState(),
    val templateParentState: TextFieldState = TextFieldState(),
    val templateFiles: SnapshotStateList<IFileTemplate> = mutableStateListOf(),
    val templateGradleFile: FileTemplate? = null,
)
