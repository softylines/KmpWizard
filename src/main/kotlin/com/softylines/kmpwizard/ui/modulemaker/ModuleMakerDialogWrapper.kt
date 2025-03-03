package com.softylines.kmpwizard.ui.modulemaker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.intellij.openapi.application.EDT
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import com.intellij.openapi.externalSystem.service.execution.ProgressExecutionMode
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.JBDimension
import com.softylines.kmpwizard.core.utils.State
import com.softylines.kmpwizard.parser.conventionplugins.ConventionPluginParser
import com.softylines.kmpwizard.parser.libs.LibsParser
import com.softylines.kmpwizard.writer.conventionplugins.ConventionPluginWriter
import com.softylines.kmpwizard.writer.module.ModuleWriter
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.enableNewSwingCompositing
import java.awt.event.ActionEvent
import java.io.File
import java.nio.file.Path
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JComponent
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.Throws
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

private const val WINDOW_WIDTH = 840
private const val WINDOW_HEIGHT = 600

class ModuleMakerDialogWrapper(
    private val project: Project,
    private val startingLocation: VirtualFile?,
) : DialogWrapper(project), CoroutineScope {

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.EDT + CoroutineName("ComposeWizard")

    private val createAction = CreateAction()
    private val cancelAction = CancelAction()

    init {
        title = "Module Maker"
        init()
    }

    private var state by mutableStateOf(ModuleMakerState())

    private fun onEvent(event: ModuleMakerEvent) {
        when (event) {
            is ModuleMakerEvent.OnToggleLayer ->
                onToggleLayer(event)

            is ModuleMakerEvent.OnCreateModule ->
                onCreateModule()

            is ModuleMakerEvent.HasConventionPlugin ->
                hasConventionPlugin()

            is ModuleMakerEvent.InitConventionPlugin ->
                initConventionPlugin()
        }
    }

    @OptIn(ExperimentalJewelApi::class)
    override fun createCenterPanel(): JComponent {
        enableNewSwingCompositing()

        return JewelComposePanel {
            ModuleMakerDialogContent(
                state = state,
                onEvent = ::onEvent,
            )
        }
            .apply { minimumSize = JBDimension(WINDOW_WIDTH, WINDOW_HEIGHT) }
    }

    override fun createActions(): Array<out Action?> {
        return arrayOf(createAction, cancelAction)
    }

    private inner class CreateAction : AbstractAction("Create") {
        override fun actionPerformed(e: ActionEvent?) {
            onEvent(ModuleMakerEvent.OnCreateModule)
        }
    }

    private inner class CancelAction : DialogWrapperAction("Cancel") {
        override fun doAction(e: ActionEvent?) {
            doCancelAction()
        }
    }

    private fun getCurrentlySelectedFile(): File {
        return File(rootDirectoryStringDropLast() + File.separator)
    }

    private fun rootDirectoryString(): String {
        return project.basePath!!
    }

    private fun rootDirectoryStringDropLast(): String {
        // rootDirectoryString() gives us back something like /Users/user/path/to/project
        // the first path element in the tree node starts with 'project' (last folder above)
        // so we remove it and join the nodes of the tree by our file separator
        return project.basePath!!.split(File.separator).dropLast(1).joinToString(File.separator)
    }

    private fun getSettingsGradleFile(): File? {
        val settingsGradleKtsCurrentlySelectedRoot =
            Path.of(getCurrentlySelectedFile().absolutePath, "settings.gradle.kts").toFile()
        val settingsGradleKtsPath = Path.of(rootDirectoryString(), "settings.gradle.kts").toFile()

        return listOf(
            settingsGradleKtsCurrentlySelectedRoot,
            settingsGradleKtsPath,
        ).firstOrNull {
            it.exists()
        } ?: run {
            null
        }
    }

    private fun syncProject() {
        ExternalSystemUtil.refreshProject(
            project,
            ProjectSystemId("GRADLE"),
            rootDirectoryString(),
            false,
            ProgressExecutionMode.START_IN_FOREGROUND_ASYNC
        )
    }

    /**
     * Refresh the settings gradle file and the root file
     */
    private fun refreshFileSystem(
        settingsGradleFile: File,
        currentlySelectedFile: File,
    ) {
        VfsUtil.markDirtyAndRefresh(
            false,
            true,
            true,
            settingsGradleFile,
            currentlySelectedFile
        )
    }

    private fun onToggleLayer(event: ModuleMakerEvent.OnToggleLayer) {
        val isSelected = event.layer in state.moduleTemplateList

        val moduleTemplateList =
            when {
                isSelected && state.moduleTemplateList.size == 1 ->
                    state.moduleTemplateList

                isSelected ->
                    state.moduleTemplateList - event.layer

                else ->
                    state.moduleTemplateList + event.layer
            }

        state = state.copy(moduleTemplateList = moduleTemplateList)
    }

    private fun onCreateModule() {
        // Update Gradle settings file
        val settingsGradleFile = getSettingsGradleFile()

        if (settingsGradleFile == null) {
            // Todo: Show error message (Notification, ui message, etc)
            return
        }

        val rootDirectory = File(rootDirectoryString())

        if (!rootDirectory.exists())
        // Todo: Show error message (Notification, ui message, etc)
            return

        // Create module directory
        // Todo: Support nested modules
        // Todo: Support layers
        val moduleDirectoryList = ModuleWriter.createModuleList(
            state = state,
            parentDirectory = rootDirectory,
            settingsGradleFile = settingsGradleFile,
        )

        // Refresh and Sync Project
        moduleDirectoryList.forEach { moduleDirectory ->
            refreshFileSystem(
                settingsGradleFile = settingsGradleFile,
                currentlySelectedFile = moduleDirectory,
            )
        }
//        if (preferenceService.preferenceState.refreshOnModuleAdd) {
        syncProject()
//        }
    }

    private fun hasConventionPlugin() {
        state = state.copy(
            conventionPlugins = State.loading()
        )

        runCatching {
            val rootDirectory = File(rootDirectoryString())

            if (!rootDirectory.exists()) {
                state = state.copy(
                    conventionPlugins = State.failure(
                        message = "Root directory does not exist"
                    )
                )
                return
            }

            val conventionPluginsDirectory = File(rootDirectory, "convention-plugins")

            if (!conventionPluginsDirectory.exists()) {
                state = state.copy(
                    conventionPlugins = State.failure(
                        message = "Convention plugins directory does not exist"
                    )
                )
                return
            }

            ConventionPluginParser.listConventionPlugins(conventionPluginsDirectory)
        }
            .onSuccess { conventionPlugins ->
                state = state.copy(
                    conventionPlugins = State.success(conventionPlugins)
                )
            }
            .onFailure { 
                state = state.copy(
                    conventionPlugins = State.failure(
                        message = it.message
                    )
                )
            }
    }

    private fun initConventionPlugin() {
        state = state.copy(
            conventionPlugins = State.loading()
        )

        runCatching {
            val rootDirectory = File(rootDirectoryString())

            if (!rootDirectory.exists()) {
                state = state.copy(
                    conventionPlugins = State.failure(
                        message = "Root directory does not exist"
                    )
                )
                return
            }

            val conventionPluginsDirectory = File(rootDirectory, "convention-plugins")
            val libsVersionsFile = File(rootDirectory, "gradle/libs.versions.toml")

            if (!libsVersionsFile.exists()) {
                state = state.copy(
                    conventionPlugins = State.failure(
                        message = "libs.versions.toml file does not exist"
                    )
                )
                return
            }

            val libsFile = LibsParser.parse(libsVersionsFile.absolutePath)

            ConventionPluginWriter.initConventionPluginModule(
                libsFile = libsFile,
                conventionPluginDirectory = conventionPluginsDirectory
            )

            val conventionPlugins = ConventionPluginParser.listConventionPlugins(conventionPluginsDirectory)

            refreshFileSystem(
                settingsGradleFile = getSettingsGradleFile() ?: return,
                currentlySelectedFile = conventionPluginsDirectory
            )

            conventionPlugins
        }
            .onSuccess { conventionPlugins ->
                state = state.copy(
                    conventionPlugins = State.success(conventionPlugins)
                )
            }
            .onFailure {
                state = state.copy(
                    conventionPlugins = State.failure(
                        message = it.message
                    )
                )
            }
    }
}
