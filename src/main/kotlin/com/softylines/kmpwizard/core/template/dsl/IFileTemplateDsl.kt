package com.softylines.kmpwizard.core.template.dsl

import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.FolderTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.packageName

fun buildIFileTemplateList(
    parent: FolderTemplate? = null,
    invoke: IFileTemplateDsl.() -> Unit,
): List<IFileTemplate> {
    val dsl = IFileTemplateDslImpl(parent)
    dsl.invoke()

    return dsl.build()
}

interface IFileTemplateDsl {
    val packageName: String?

    fun addFileTemplate(
        name: String,
        content: String = "",
        addPackage: Boolean = true,
    )

    fun addFolderTemplate(
        name: String,
        invoke: IFileTemplateDsl.() -> Unit
    )

    fun build(): List<IFileTemplate>
}

class IFileTemplateDslImpl(
    val parent: FolderTemplate? = null
): IFileTemplateDsl {
    override val packageName: String? = parent?.packageName?.let {
        if (it.isBlank())
            parent.name
        else
            "$it.${parent.name}"
    }

    private val fileTemplates = mutableListOf<IFileTemplate>()

    override fun addFileTemplate(
        name: String,
        content: String,
        addPackage: Boolean,
    ) {
        val contentWithPackage =
            if (addPackage && packageName != null && !content.trim().startsWith("package "))
                "package $packageName\n\n$content"
            else
                content

        fileTemplates.add(
            FileTemplate(
                name = name,
                parent = parent,
                content = contentWithPackage,
            )
        )
    }

    override fun addFolderTemplate(
        name: String,
        invoke: IFileTemplateDsl.() -> Unit
    ) {
        val folderTemplate = FolderTemplate(
            name = name,
            parent = parent,
            files = mutableListOf()
        )

        val files = buildIFileTemplateList(folderTemplate, invoke)

        folderTemplate.files.addAll(files)

        fileTemplates.add(folderTemplate)
    }

    override fun build(): List<IFileTemplate> {
        return fileTemplates.toList()
    }
}