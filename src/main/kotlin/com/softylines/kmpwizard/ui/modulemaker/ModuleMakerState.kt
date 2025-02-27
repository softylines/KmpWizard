package com.softylines.kmpwizard.ui.modulemaker

import com.softylines.kmpwizard.core.utils.State
import androidx.compose.foundation.text.input.TextFieldState
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate

data class ModuleMakerState(
    val moduleNameState: TextFieldState = TextFieldState(),
    val packageNameState: TextFieldState = TextFieldState(),
    val moduleTemplateList: Set<ModuleTemplate> = setOf(ModuleTemplate.Empty),
    val conventionPlugins: State<List<String>> = State(),
    val initConventionPlugin: State<Unit> = State(),
)
