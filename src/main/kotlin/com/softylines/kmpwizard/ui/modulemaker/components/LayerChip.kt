package com.softylines.kmpwizard.ui.modulemaker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.softylines.kmpwizard.ui.modulemaker.layer.ModuleTemplate
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.ToggleableChip
import org.jetbrains.jewel.ui.painter.hints.Selected

@Composable
fun ModuleTemplateChip(
    moduleTemplate: ModuleTemplate,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    CheckboxRow(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(moduleTemplate.iconKey, moduleTemplate.name, hint = Selected(checked))

        Spacer(Modifier.width(5.dp))

        Text(moduleTemplate.name)
    }
}