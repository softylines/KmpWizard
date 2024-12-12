package com.softylines.kmpwizard.ui.modulemaker

sealed interface ModuleMakerEvent {
    data object OnCreateModule : ModuleMakerEvent
}