package com.softylines.kmpwizard.ui.modulemaker

import com.softylines.kmpwizard.ui.modulemaker.layer.MLayer

data class ModuleMakerState(
    val moduleName: String = "profile",
    val packageName: String = "",
    val moduleLayer: MLayer = MLayer.Ui,
)
