package test.kmpwizard.core.template

import androidx.compose.foundation.text.input.TextFieldState
import com.softylines.kmpwizard.core.template.FileTemplate
import com.softylines.kmpwizard.core.template.FolderTemplate
import com.softylines.kmpwizard.core.template.IFileTemplate
import com.softylines.kmpwizard.core.template.formatModuleName
import com.softylines.kmpwizard.core.template.parseContent
import com.softylines.kmpwizard.core.template.parseName
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState
import kotlin.test.Test
import kotlin.test.assertEquals

class IFileTemplateTest {

    @Test
    fun testParseFileNameWithNoArgument() {
        val state = ModuleMakerState(moduleNameState = TextFieldState("MyModule"))
        val file = FileTemplate(name = "build.gradle")

        val result = file.parseName(state)

        assertEquals("build.gradle", result)
    }

    @Test
    fun testParseFileNameWithArgument() {
        val moduleName = "MyModule"
        val state = ModuleMakerState(moduleNameState = TextFieldState(moduleName))
        val file = FileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}Repository")

        val result = file.parseName(state)

        assertEquals("MymoduleRepository", result)
    }

    @Test
    fun testParseFolderNameWithNoArgument() {
        val state = ModuleMakerState(moduleNameState = TextFieldState("MyModule"))
        val folder = FolderTemplate(name = "data", files = mutableListOf())

        val result = folder.parseName(state)

        assertEquals("data", result)
    }

    @Test
    fun testParseFolderNameWithArgument() {
        val moduleName = "MyModule"
        val state = ModuleMakerState(moduleNameState = TextFieldState(moduleName))
        val folder = FolderTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}", files = mutableListOf())

        val result = folder.parseName(state)

        assertEquals("mymodule", result)
    }

    @Test
    fun testFormatModuleNameForFile() {
        val moduleName = "my-module"
        val file = FileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}Repository")

        assertEquals("MyModule", file.formatModuleName(moduleName))
    }

    @Test
    fun testFormatModuleNameForFileWithLowercaseFlag() {
        val moduleName = "my-module"
        val file = FileTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}Repository")

        assertEquals("mymodule", file.formatModuleName(moduleName, isLowercase = true))
    }

    @Test
    fun testFormatModuleNameForFolder() {
        val moduleName = "my-module"
        val folder = FolderTemplate(name = "${IFileTemplate.ModuleNameKeyDollar}", files = mutableListOf())

        assertEquals("mymodule", folder.formatModuleName(moduleName))
    }

    @Test
    fun testFormatFileContent() {
        val moduleName = "profile"
        val state = ModuleMakerState(moduleNameState = TextFieldState(moduleName))
        val file = FileTemplate(
            name = "${IFileTemplate.ModuleNameKeyDollar}Repository.kt",
            content =
                """                            
                interface ${IFileTemplate.ModuleNameKeyDollar}Repository {
                }
                """.trimIndent()
        )

        val result = file.parseContent(ModuleMakerState(moduleNameState = TextFieldState(moduleName)))

        assertEquals(
            """
            interface ProfileRepository {
            }
            """.trimIndent(), result
        )
    }

}