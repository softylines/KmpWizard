@file:Suppress("UnstableApiUsage")

package com.softylines.kmpwizard.ui.modulemaker.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.FolderTemplate
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerEvent
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState
import org.jetbrains.jewel.ui.Orientation
import org.jetbrains.jewel.ui.component.*
import org.jetbrains.jewel.ui.component.Typography

@Composable
fun TemplatesSection(
    state: ModuleMakerState,
    onEvent: (ModuleMakerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        // Title
        Text(
            text = "User Templates",
            style = Typography.h3TextStyle(),
        )

        // Template list
        if (state.userTemplates.isEmpty() && !state.isEditingTemplate) {
            Text("No user templates yet. Create your first template!")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(state.userTemplates) { template ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Icon removed for now due to compatibility issues

                        Text(template.name, modifier = Modifier.weight(1f))

                        OutlinedButton(
                            onClick = { onEvent(ModuleMakerEvent.OnSelectTemplateForEdit(template)) }
                        ) {
                            Text("Edit")
                        }

                        OutlinedButton(
                            onClick = { onEvent(ModuleMakerEvent.OnDeleteTemplate(template)) }
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }

        // Template editor
        if (state.isEditingTemplate) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Divider(Orientation.Horizontal)

                Text(
                    text = if (state.selectedUserTemplate == null) "Create New Template" else "Edit Template",
                    style = Typography.h4TextStyle(),
                )

                TextField(
                    state = state.templateNameState,
                    placeholder = { Text("Enter template name") },
                    modifier = Modifier.fillMaxWidth(),
                )

                TextField(
                    state = state.templateParentState,
                    placeholder = { Text("Enter parent module (e.g. :ui)") },
                    modifier = Modifier.fillMaxWidth(),
                )

                // TODO: Add fields for template files and build.gradle content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(state.templateFiles) { fileTemplate ->
                        IFileTemplateComponent(
                            fileTemplate = fileTemplate,
                            depth = 0,
                            onDoubleClick = {
                                // Open file
                                // files only
                                if (fileTemplate is FileTemplate)
                                    onEvent(ModuleMakerEvent.OnSelectFileTemplateForEdit(fileTemplate))
                            },
                            onDelete = { fileTemplate ->
                                // Todo: Add dialog to confirm

                                onEvent(ModuleMakerEvent.OnDeleteTemplateFile(fileTemplate))
                            },
                            onCreateFileTemplate = { newFileTemplate ->
                                onEvent(ModuleMakerEvent.OnAddFileTemplate(newFileTemplate))
                            }
                        )
                    }

                    item {
                        AddFileFolderComponent(
                            onCreateFile = { newFileName ->
                                val newFileTemplate = FileTemplate(
                                    name = newFileName,
                                    parent = null,
                                )
                                onEvent(ModuleMakerEvent.OnAddFileTemplate(newFileTemplate))
                            },
                            onCreateFolder = { newFolderName ->
                                val newFolderTemplate = FolderTemplate(
                                    name = newFolderName,
                                    parent = null,
                                    files = mutableStateListOf(),
                                )
                                onEvent(ModuleMakerEvent.OnAddFileTemplate(newFolderTemplate))
                            }
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onEvent(ModuleMakerEvent.OnCancelTemplateEdit) }
                    ) {
                        Text("Cancel")
                    }

                    OutlinedButton(
                        onClick = { onEvent(ModuleMakerEvent.OnSaveTemplate) }
                    ) {
                        Text("Save")
                    }
                }
            }
        } else {
            // Create template button
            OutlinedButton(
                onClick = { onEvent(ModuleMakerEvent.OnStartCreateTemplate) }
            ) {
                Text("Create New Template")
            }
        }
    }
}
