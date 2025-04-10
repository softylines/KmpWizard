@file:Suppress("UnstableApiUsage")

package com.softylines.kmpwizard.ui.modulemaker.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.FolderTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import org.jetbrains.jewel.foundation.modifier.onHover
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ColumnScope.IFileTemplateComponent(
    fileTemplate: IFileTemplate,
    depth: Int,
    modifier: Modifier = Modifier,
    onDoubleClick: (fileTemplate: IFileTemplate) -> Unit,
    onDelete: (fileTemplate: IFileTemplate) -> Unit,
    onCreateFileTemplate: (fileTemplate: IFileTemplate) -> Unit,
) {
    when (fileTemplate) {
        is FileTemplate ->
            FileTemplateComponent(
                fileTemplate = fileTemplate,
                depth = depth,
                modifier = modifier,
                onDoubleClick = onDoubleClick,
                onDelete = onDelete,
            )

        is FolderTemplate ->
            FolderTemplateComponent(
                folderTemplate = fileTemplate,
                depth = depth,
                modifier = modifier,
                onDoubleClick = onDoubleClick,
                onDelete = onDelete,
                onCreateFileTemplate = onCreateFileTemplate,
            )
    }
}

@Composable
fun ColumnScope.FileTemplateComponent(
    fileTemplate: FileTemplate,
    depth: Int,
    modifier: Modifier = Modifier,
    onDoubleClick: (fileTemplate: IFileTemplate) -> Unit,
    onDelete: (fileTemplate: IFileTemplate) -> Unit,
) {
    IFileHeaderWrapper(
        fileTemplate = fileTemplate,
        depth = depth,
        onClick = onDoubleClick,
        onDelete = onDelete,
        modifier = modifier,
    ) {
        Icon(
            key = AllIconsKeys.Actions.AddFile,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )

        Text(
            text = fileTemplate.name,
        )
    }
}

@Composable
fun ColumnScope.FolderTemplateComponent(
    folderTemplate: FolderTemplate,
    depth: Int,
    modifier: Modifier = Modifier,
    onDoubleClick: (fileTemplate: IFileTemplate) -> Unit,
    onDelete: (fileTemplate: IFileTemplate) -> Unit,
    onCreateFileTemplate: (fileTemplate: IFileTemplate) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    IFileHeaderWrapper(
        fileTemplate = folderTemplate,
        depth = depth,
        onClick = onDoubleClick,
        onDelete = onDelete,
        modifier = modifier,
    ) {
        IconButton(
            onClick = {
                isExpanded = !isExpanded
            }
        ) {
            Icon(
                key = with(AllIconsKeys.General) {
                    if (isExpanded)
                        ChevronDown
                    else
                        ChevronRight
                },
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
        }

        Icon(
            key = AllIconsKeys.Actions.NewFolder,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )

        Text(
            text = folderTemplate.name,
        )
    }

    if (isExpanded) {
        folderTemplate.files.forEach { childFileTemplate ->
            IFileTemplateComponent(
                fileTemplate = childFileTemplate,
                depth = depth + 1,
                onDoubleClick = onDoubleClick,
                onDelete = onDelete,
                onCreateFileTemplate = onCreateFileTemplate,
            )
        }

        AddFileFolderComponent(
            onCreateFile = { newFileName ->
                println("add file: $newFileName for folder ${folderTemplate.name}")

                val newFileTemplate = FileTemplate(
                    name = newFileName,
                    parent = folderTemplate,
                )
                onCreateFileTemplate(newFileTemplate)
            },
            onCreateFolder = { newFolderName ->
                val newFolderTemplate = FolderTemplate(
                    name = newFolderName,
                    parent = folderTemplate,
                    files = mutableStateListOf(),
                )
                onCreateFileTemplate(newFolderTemplate)
            },
            modifier = Modifier
                .padding(start = FileDepthPadding * depth)
        )
    }
}

@Composable
private fun IFileHeaderWrapper(
    fileTemplate: IFileTemplate,
    depth: Int,
    modifier: Modifier = Modifier,
    onClick: (fileTemplate: IFileTemplate) -> Unit = {},
    onDelete: (fileTemplate: IFileTemplate) -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    var isHovered by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .onHover {
                isHovered = it
            }
            .iFileTemplateRowModifier(
                depth = depth,
                fileTemplate = fileTemplate,
                onClick = onClick,
            )
            .padding(4.dp)
            .widthIn(min = 200.dp)
    ) {
        content()

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {
                onDelete(fileTemplate)
            },
            enabled = isHovered,
            modifier = Modifier
                .graphicsLayer {
                    alpha = if (isHovered) 1f else 0f
                }
        ) {
            Icon(
                key = AllIconsKeys.General.Delete,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

private fun Modifier.iFileTemplateRowModifier(
    depth: Int,
    fileTemplate: IFileTemplate,
    onClick: (fileTemplate: IFileTemplate) -> Unit = {},
) =
    this
        .combinedClickable(
            onDoubleClick = {
                onClick(fileTemplate)
            }
        ) {
            // TODO: Add selection
        }
        .padding(start = FileDepthPadding * depth * if (fileTemplate is FolderTemplate) 0.5f else 1f)

private val FileDepthPadding = 32.dp