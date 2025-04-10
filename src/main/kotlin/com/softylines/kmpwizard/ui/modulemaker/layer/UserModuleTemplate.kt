package com.softylines.kmpwizard.ui.modulemaker.layer

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.softylines.kmpwizard.core.template.BuildGradleFileTemplate
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys

/**
 * A user-defined module template.
 */
class UserModuleTemplate(
    override val name: String,
    override val parent: String,
    override val iconKey: IconKey = AllIconsKeys.Nodes.Module,
    override val files: SnapshotStateList<IFileTemplate> = mutableStateListOf(),
    override val buildGradleFile: FileTemplate = BuildGradleFileTemplate(
        // Todo: Add this string a predefined template
        content = """
            plugins {
                id("kotlin.mp")
            }
            
            kotlin {
                sourceSets.commonMain.dependencies {
                    
                }
            }
        """.trimIndent()
    )
) : ModuleTemplate {
}