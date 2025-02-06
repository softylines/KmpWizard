package com.softylines.kmpwizard.core.template

data class FileTemplate(
    override val name: String,
    override val parent: FolderTemplate? = null,
    val content: String = "",
): IFileTemplate

fun BuildGradleFileTemplate(
    content: String,
): FileTemplate {
    return FileTemplate(
        name = "build.gradle.kts",
        content = content,
    )
}
