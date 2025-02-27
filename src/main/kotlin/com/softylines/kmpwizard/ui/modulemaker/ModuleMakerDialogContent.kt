package com.softylines.kmpwizard.ui.modulemaker

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.softylines.kmpwizard.ui.modulemaker.components.AddModuleSection
import com.softylines.kmpwizard.ui.modulemaker.components.ConventionPluginSection
import com.softylines.kmpwizard.ui.modulemaker.components.ModuleTemplateChip
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import org.jetbrains.jewel.foundation.modifier.onActivated
import org.jetbrains.jewel.foundation.modifier.trackActivation
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.theme.defaultTabStyle
import org.jetbrains.jewel.ui.theme.editorTabStyle
import org.jetbrains.jewel.ui.util.thenIf
import kotlin.math.max

private enum class ModuleMakerTab {
    AddModule,
    ConventionPlugins,
    Templates;

    val title: String
        get() = when (this) {
            AddModule -> "Add Module"
            ConventionPlugins -> "Convention Plugins"
            Templates -> "Templates"
        }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ModuleMakerDialogContent(
    state: ModuleMakerState,
    onEvent: (ModuleMakerEvent) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(ModuleMakerTab.AddModule) }

    val tabs =
        remember(selectedTab) {
            ModuleMakerTab.entries.map { tab ->
                TabData.Default(
                    selected = tab == selectedTab,
                    content = { tabState ->
                        SimpleTabContent(
                            state = tabState,
                            modifier = Modifier,
                            icon = {
                                Icon(
                                    key = AllIconsKeys.Actions.Find,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp).tabContentAlpha(state = tabState),
                                    tint = Color.Cyan,
                                )
                            },
                            label = { Text(tab.title) },
                        )
                    },
                    closable = false,
                    onClick = { selectedTab = tab },
                )
            }
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .trackActivation()
    ) {
        // Tabs
        TabStrip(
            tabs = tabs,
            style = JewelTheme.editorTabStyle,
            modifier = Modifier.fillMaxWidth()
        )

        when (selectedTab) {
            ModuleMakerTab.AddModule ->
                AddModuleSection(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier
                        .weight(1f)
                )

            ModuleMakerTab.ConventionPlugins ->
                ConventionPluginSection(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier
                        .weight(1f)
                )

            ModuleMakerTab.Templates ->
                Text("")
        }
    }
}