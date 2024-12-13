package com.softylines.kmpwizard.ui.modulemaker.layer

interface MLayer {

    val name: String

    companion object {
        val Ui = UiMLayer()

        val Data = DataMLayer()

        val Domain = DomainMLayer()
    }

}