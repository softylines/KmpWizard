package com.softylines.kmpwizard.ui.modulemaker.layer

import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys

// Todo
// profile
// select: ui
// :ui:profile
// :ui:fsdf

interface ModuleTemplate {

    val name: String
    val files: List<IFileTemplate>
    val parent: String
    val iconKey: IconKey get() = AllIconsKeys.Nodes.Folder

    val buildGradleFile: FileTemplate

    companion object {
        val Ui = UiModuleTemplate()

        val Data = DataModuleTemplate()

        val Domain = DomainModuleTemplate()

        val Empty = EmptyModuleTemplate()
    }

}