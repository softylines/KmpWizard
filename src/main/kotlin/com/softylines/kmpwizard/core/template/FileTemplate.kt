package com.softylines.kmpwizard.core.template

data class FileTemplate(
    override val name: String,
    override val parent: FolderTemplate? = null,
    val content: String = "",
): IFileTemplate