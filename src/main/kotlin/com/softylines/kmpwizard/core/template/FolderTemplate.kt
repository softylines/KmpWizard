package com.softylines.kmpwizard.core.template

data class FolderTemplate(
    override val name: String,
    override val parent: FolderTemplate? = null,
    val files: MutableList<IFileTemplate>,
): IFileTemplate