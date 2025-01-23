package com.softylines.kmpwizard.ui.modulemaker

import androidx.compose.foundation.text.input.TextFieldState
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate

data class ModuleMakerState(
    val moduleNameState: TextFieldState = TextFieldState(),
    val packageNameState: TextFieldState = TextFieldState(),
    val moduleLayer: ModuleTemplate = ModuleTemplate.Ui,
)
