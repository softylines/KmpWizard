package com.softylines.kmpwizard.core.template

import androidx.compose.runtime.snapshots.SnapshotStateList

data class FolderTemplate(
    override val name: String,
    override val parent: FolderTemplate? = null,
    val files: SnapshotStateList<IFileTemplate>,
): IFileTemplate