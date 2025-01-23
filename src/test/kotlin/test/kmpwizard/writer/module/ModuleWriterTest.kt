package test.kmpwizard.writer.module

import com.softylines.kmpwizard.writer.module.ModuleWriter
import org.junit.After
import org.junit.Before
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ModuleWriterTest {

    private val testDirectory = File("./test")
    private val settingsGradleFile = File(testDirectory, "settings.gradle.kts")

    @Before
    fun setup() {
        if (testDirectory.exists()) {
            testDirectory.deleteRecursively()
        }
        testDirectory.mkdirs()
        settingsGradleFile.createNewFile()
    }

    @After
    fun tearDown() {
        if (testDirectory.exists()) {
            testDirectory.deleteRecursively()
        }
    }

    @Test
    fun `test add module build gradle file`() {
        val moduleName = "profile"
        val moduleDirectory = ModuleWriter.getModuleDirectory(
            name = moduleName,
            parentDirectory = testDirectory,
        )

        assertNotNull(moduleDirectory)

        ModuleWriter.addModuleBuildGradleFile(
            moduleDirectory = moduleDirectory
        )

        val buildGradleFile = File(moduleDirectory, "build.gradle.kts")

        assert(buildGradleFile.isFile)
        assert(buildGradleFile.exists())
    }

    @Test
    fun `test add module src commonMain kotlin`() {
        val moduleName = "profile"
        val moduleDirectory = ModuleWriter.getModuleDirectory(
            name = moduleName,
            parentDirectory = testDirectory,
        )

        assertNotNull(moduleDirectory)

        ModuleWriter.addModuleSrcCommonMainKotlin(
            moduleDirectory = moduleDirectory
        )

        val srcCommonMainKotlinDir = File(testDirectory, "$moduleName/src/commonMain/kotlin")

        assert(srcCommonMainKotlinDir.isDirectory)
        assert(srcCommonMainKotlinDir.exists())
    }

    @Test
    fun `test add include to empty settings gradle file`() {
        val moduleName = "profile"

        settingsGradleFile.writeText("")

        ModuleWriter.addModuleToSettingsGradle(
            moduleName = moduleName,
            settingsGradleFile = settingsGradleFile,
        )

        val lines = settingsGradleFile.readLines()

        assertEquals(1, lines.size)
        assertEquals(lines.first(), "include(\":$moduleName\")")
    }

    @Test
    fun `test add include to settings gradle file that contains include`() {
        val moduleName = "profile"

        settingsGradleFile.writeText(
            """
                plugins {
                    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
                }
                
                include(":common")

                rootProject.name = "KmpWizard"
            """.trimIndent()
        )

        val oldLines = settingsGradleFile.readLines()

        ModuleWriter.addModuleToSettingsGradle(
            moduleName = moduleName,
            settingsGradleFile = settingsGradleFile,
        )

        val includeIndex = 5
        val lines = settingsGradleFile.readLines()

        assertEquals(8, lines.size)
        assertEquals(oldLines, lines.filterIndexed { index, _ -> index != includeIndex })
        assertEquals(lines[includeIndex], "include(\":$moduleName\")")
    }

    @Test
    fun `test add include to settings gradle file that doesn't contain include`() {
        val moduleName = "profile"

        settingsGradleFile.writeText(
            """
                plugins {
                    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
                }
                
                rootProject.name = "KmpWizard"
            """.trimIndent()
        )

        val oldLines = settingsGradleFile.readLines()

        ModuleWriter.addModuleToSettingsGradle(
            moduleName = moduleName,
            settingsGradleFile = settingsGradleFile,
        )

        val includeIndex = 5
        val lines = settingsGradleFile.readLines()

        assertEquals(6, lines.size)
        assertEquals(oldLines, lines.filterIndexed { index, _ -> index != includeIndex })
        assertEquals(lines[includeIndex], "include(\":$moduleName\")")
    }

}