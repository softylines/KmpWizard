package com.softylines.kmpwizard.ui.modulemaker

import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate

sealed interface ModuleMakerEvent {
    data class OnLayerSelected(val layer: ModuleTemplate) : ModuleMakerEvent

    data object OnCreateModule : ModuleMakerEvent
}