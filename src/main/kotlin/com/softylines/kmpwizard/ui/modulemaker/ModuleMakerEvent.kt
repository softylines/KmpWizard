package com.softylines.kmpwizard.ui.modulemaker

import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import com.softylines.kmpwizard.ui.modulemaker.layer.UserModuleTemplate

sealed interface ModuleMakerEvent {
    data class OnToggleLayer(val layer: ModuleTemplate) : ModuleMakerEvent

    data object OnCreateModule : ModuleMakerEvent

    data object HasConventionPlugin: ModuleMakerEvent

    data object InitConventionPlugin: ModuleMakerEvent

    // User template events
    data object OnStartCreateTemplate : ModuleMakerEvent

    data class OnSelectTemplateForEdit(val template: UserModuleTemplate) : ModuleMakerEvent

    data object OnCancelTemplateEdit : ModuleMakerEvent

    data object OnSaveTemplate : ModuleMakerEvent

    data class OnDeleteTemplate(val template: ModuleTemplate) : ModuleMakerEvent

    data class OnSelectFileTemplateForEdit(val fileTemplate: FileTemplate): ModuleMakerEvent

    data class OnAddFileTemplate(val fileTemplate: IFileTemplate): ModuleMakerEvent

    data class OnDeleteTemplateFile(val fileTemplate: IFileTemplate): ModuleMakerEvent
}
