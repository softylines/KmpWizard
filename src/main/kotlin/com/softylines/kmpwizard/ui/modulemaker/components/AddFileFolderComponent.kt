@file:Suppress("UnstableApiUsage")

package com.softylines.kmpwizard.ui.modulemaker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun AddFileFolderComponent(
    modifier: Modifier = Modifier,
    onCreateFile: (String) -> Unit,
    onCreateFolder: (String) -> Unit,
) {
    var isCreateNewFileDialogExpanded by remember { mutableStateOf(false) }
    var isCreateNewFolderDialogExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        OutlinedButton(
            onClick = {
                isCreateNewFileDialogExpanded = true
            }
        ) {
            Icon(
                key = AllIconsKeys.Actions.AddFile,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )

            Text("New file")
        }

        Spacer(
            modifier = Modifier
                .width(8.dp)
        )

        OutlinedButton(
            onClick = {
                isCreateNewFolderDialogExpanded = true
            }
        ) {
            Icon(
                key = AllIconsKeys.Actions.NewFolder,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )

            Text("New folder")
        }
    }

    AddFileFolderDialog(
        title = "Create new file",
        isExpanded = isCreateNewFileDialogExpanded,
        onDismissRequest = {
            isCreateNewFileDialogExpanded = false
        },
        onCreate = { fileName ->
            onCreateFile(fileName)
            isCreateNewFileDialogExpanded = false
        }
    )

    AddFileFolderDialog(
        title = "Create new folder",
        isExpanded = isCreateNewFolderDialogExpanded,
        onDismissRequest = {
            isCreateNewFolderDialogExpanded = false
        },
        onCreate = { folderName ->
            onCreateFolder(folderName)
            isCreateNewFolderDialogExpanded = false
        }
    )
}

@Composable
private fun AddFileFolderDialog(
    title: String,
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    onCreate: (String) -> Unit,
) {
    if (isExpanded) {
        Dialog(
            onDismissRequest = onDismissRequest,
        ) {
            val textFieldState = rememberTextFieldState()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .width(300.dp)
            ) {
                Text(
                    text = title,
                )

                TextField(
                    state = textFieldState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPreviewKeyEvent {
                            if (
                                it.key == Key.Enter &&
                                it.type == KeyEventType.KeyDown
                            ) {
                                onCreate(textFieldState.text.toString())
                                onDismissRequest()
                                true
                            } else {
                                false
                            }
                        }
                )
            }
        }
    }
}