package test.kmpwizard.core.template.dsl

import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.FolderTemplate
import com.softylines.kmpwizard.core.template.dsl.buildIFileTemplateList
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class IFileTemplateDslTest {

    @Test
    fun testAddFileTemplate() {
        val list = buildIFileTemplateList {
            addFileTemplate(name = "file1")
            addFileTemplate(name = "file2")
        }

        assertEquals(2, list.size)
        assertEquals("file1", list[0].name)
        assertEquals("file2", list[1].name)
        assertIs<FileTemplate>(list[0])
        assertIs<FileTemplate>(list[1])
    }

    @Test
    fun testAddFolderTemplate() {
        val list = buildIFileTemplateList {
            addFolderTemplate(name = "folder1") {
                addFileTemplate(name = "file1")
                addFileTemplate(name = "file2")
            }
            addFolderTemplate(name = "folder2") {
                addFileTemplate(name = "file3")
                addFileTemplate(name = "file4")
            }
        }

        assertEquals(2, list.size)
        val folder1 = assertIs<FolderTemplate>(list[0])
        val folder2 = assertIs<FolderTemplate>(list[1])
        assertEquals("folder1", folder1.name)
        assertEquals("folder2", folder2.name)

        assertEquals(2, folder1.files.size)
        assertEquals(2, folder2.files.size)

        folder1.files.forEach {
            assertIs<FileTemplate>(it)
        }

        folder2.files.forEach {
            assertIs<FileTemplate>(it)
        }
    }

}