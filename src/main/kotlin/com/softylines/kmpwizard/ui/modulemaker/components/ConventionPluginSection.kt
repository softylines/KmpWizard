package com.softylines.kmpwizard.ui.modulemaker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerEvent
import com.softylines.kmpwizard.ui.modulemaker.ModuleMakerState
import org.jetbrains.jewel.ui.component.*

@Composable
fun ConventionPluginSection(
    state: ModuleMakerState,
    onEvent: (ModuleMakerEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = "Convention Plugins",
            style = Typography.h3TextStyle(),
        )

        if (state.conventionPlugins.data == null && !state.conventionPlugins.isLoading) {
            LaunchedEffect(Unit) {
                onEvent(ModuleMakerEvent.HasConventionPlugin)
            }
        }

        when {
            state.conventionPlugins.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            state.conventionPlugins.isSuccess && state.conventionPlugins.data != null -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.conventionPlugins.data.isEmpty()) {
                        Text(
                            text = "No convention plugins found",
                            style = Typography.h4TextStyle(),
                            modifier = Modifier
                        )
                    }

                    state.conventionPlugins.data.forEach { plugin ->
                        Text(
                            text = plugin,
                            style = Typography.h4TextStyle(),
                            modifier = Modifier
                        )
                    }
                }
            }
            else -> {
                OutlinedButton(
                    onClick = {
                        onEvent(ModuleMakerEvent.InitConventionPlugin)
                    },
                    enabled = !state.initConventionPlugin.isLoading,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) {
                    if (state.initConventionPlugin.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        )
                    }

                    Text(
                        text = "Init Convention Plugins",
                        modifier = Modifier
                            .graphicsLayer {
                                alpha = if (state.initConventionPlugin.isLoading) 0f else 1f
                            }
                    )
                }
            }
        }
    }
}
