package com.softylines.kmpwizard.ui.modulemaker

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import org.jetbrains.jewel.foundation.modifier.onActivated
import org.jetbrains.jewel.foundation.modifier.trackActivation
import org.jetbrains.jewel.ui.component.*

@Composable
fun ModuleMakerDialogContent(
    state: ModuleMakerState,
    onEvent: (ModuleMakerEvent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .trackActivation()
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
                ModuleTemplate.Ui,
                ModuleTemplate.Data,
                ModuleTemplate.Domain,
            )
        }

        Dropdown(
            menuContent = {
                options.forEach { layer ->
                    selectableItem(
                        selected = state.moduleLayer == layer,
                        onClick = {
                            onEvent(ModuleMakerEvent.OnLayerSelected(layer))
                        }
                    ) {
                        Text(
                            text = layer.name,
                        )
                    }
                }
            }
        ) {
            Text(state.moduleLayer.name)
        }

//        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
//            var clicks1 by remember { mutableIntStateOf(0) }
//            OutlinedButton({ clicks1++ }) { Text("Outlined: $clicks1") }
//            OutlinedButton({}, enabled = false) { Text("Outlined") }
//
//            var clicks2 by remember { mutableIntStateOf(0) }
//            DefaultButton({ clicks2++ }) { Text("Default: $clicks2") }
//            DefaultButton({}, enabled = false) { Text("Default") }
//        }

//        val state = rememberTextFieldState("")
//        TextField(
//            state = state,
//            modifier =
//                Modifier.width(200.dp).provideData {
//                    set(ActionSystemTestAction.COMPONENT_DATA_KEY.name, "TextField")
//                    lazy(ActionSystemTestAction.COMPONENT_DATA_KEY.name) { Math.random().toString() }
//                },
//            placeholder = { Text("Write something...") },
//        )
//
//        var checked by remember { mutableStateOf(false) }
//        var validated by remember { mutableStateOf(false) }
//        val outline = if (validated) Outline.Error else Outline.None
//
//        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//            CheckboxRow(
//                checked = checked,
//                onCheckedChange = { checked = it },
//                outline = outline,
//                modifier = Modifier.provideData { set(ActionSystemTestAction.COMPONENT_DATA_KEY.name, "Checkbox") },
//            ) {
//                Text("Hello, I am a themed checkbox")
//            }
//
//            CheckboxRow(checked = validated, onCheckedChange = { validated = it }) { Text("Show validation") }
//        }
//
//        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//            var index by remember { mutableIntStateOf(0) }
//            RadioButtonRow(selected = index == 0, onClick = { index = 0 }, outline = outline) {
//                Text("I am number one")
//            }
//            RadioButtonRow(selected = index == 1, onClick = { index = 1 }, outline = outline) { Text("Sad second") }
//        }

//        IconsShowcase()

//        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Text("Circular progress small:")
//            CircularProgressIndicator()
//        }
//
//        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            Text("Circular progress big:")
//            CircularProgressIndicatorBig()
//        }
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Tooltip(
//                tooltip = {
//                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Icon(key = AllIconsKeys.General.ShowInfos, contentDescription = null)
//                        Text("This is a tooltip")
//                    }
//                }
//            ) {
//                Text(
//                    modifier = Modifier.border(1.dp, JewelTheme.globalColors.borders.normal).padding(12.dp, 8.dp),
//                    text = "Hover Me!",
//                )
//            }
//        }
//
//        var sliderValue by remember { mutableFloatStateOf(.15f) }
//        Slider(sliderValue, { sliderValue = it }, steps = 5)
    }
}