package com.softylines.kmpwizard.ui.modulemaker

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Service(Service.Level.PROJECT)
private class ProjectScopeProviderService(val scope: CoroutineScope)

class ModuleMakerAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = checkNotNull(event.project) { "Project not available" }
        val scope = project.service<ProjectScopeProviderService>().scope

        val startingLocation: VirtualFile? = event.getData(CommonDataKeys.VIRTUAL_FILE)

        // we only want to use a starting location if it's coming from a directory
        val shouldUseStartingLocation = startingLocation != null && startingLocation.isDirectory

        scope.launch(Dispatchers.EDT) {
            ModuleMakerDialogWrapper(
                project = project,
                startingLocation = if (shouldUseStartingLocation) startingLocation else null
            ).show()
        }
    }

}