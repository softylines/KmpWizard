package com.softylines.kmpwizard.ui.modulemaker

import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate

sealed interface ModuleMakerEvent {
    data class OnToggleLayer(val layer: ModuleTemplate) : ModuleMakerEvent

    data object OnCreateModule : ModuleMakerEvent

    data object HasConventionPlugin: ModuleMakerEvent

    data object InitConventionPlugin: ModuleMakerEvent
}