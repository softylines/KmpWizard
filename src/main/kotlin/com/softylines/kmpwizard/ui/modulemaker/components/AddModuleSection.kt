package com.softylines.kmpwizard.ui.modulemaker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerEvent
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import org.jetbrains.jewel.foundation.modifier.onActivated
import org.jetbrains.jewel.foundation.modifier.trackActivation
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.component.Typography

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddModuleSection(
    state: ModuleMakerState,
    onEvent: (ModuleMakerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        var activated by remember { mutableStateOf(false) }

        Text(
            text = "Create new module:",
            style = Typography.h3TextStyle(),
            modifier = Modifier.onActivated { activated = it },
        )

        TextField(
            state = state.moduleNameState,
            placeholder = { Text("Enter module name") },
            modifier = Modifier.width(200.dp),
        )

        TextField(
            state = state.packageNameState,
            placeholder = { Text("Enter package name") },
            modifier = Modifier.width(200.dp),
        )

        val options = remember {
            listOf(
                ModuleTemplate.Empty,
                ModuleTemplate.Ui,
                ModuleTemplate.Data,
                ModuleTemplate.Domain,
            )
        }

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
        ) {
            options.forEach { template ->
                val selected by remember(
                    template,
                    state.moduleTemplateList,
                ) {
                    mutableStateOf(template in state.moduleTemplateList)
                }

                val enabled by remember(
                    template,
                    state.moduleTemplateList,
                ) {
                    mutableStateOf(
                        state.moduleTemplateList.none {
                            it != template && it.parent == template.parent
                        }
                    )
                }

                ModuleTemplateChip(
                    moduleTemplate = template,
                    checked = selected,
                    enabled = enabled,
                    onCheckedChange = {
                        onEvent(ModuleMakerEvent.OnToggleLayer(template))
                    },
                )
            }
        }
    }
}