package com.example.pokeapi.features.sync.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokeapi.shared.features.sync.domain.model.SyncPhase
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.components.RetroButton
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.BorderShadow
import com.example.pokeapi.ui.theme.HpHigh
import com.example.pokeapi.ui.theme.Surface
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SyncScreen(
    onSyncComplete: () -> Unit,
    viewModel: SyncViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            onSyncComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Title
            Text(
                text = "POKEAPI",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "BATTLE GAME",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(48.dp))

            if (state.error != null) {
                // Error state
                RetroBox(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Surface
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SYNC FAILED!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = state.error ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(16.dp))
                        RetroButton(
                            text = "Retry",
                            onClick = { viewModel.onEvent(SyncEvent.RetrySync) }
                        )
                    }
                }
            } else {
                // Progress state
                RetroBox(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Surface
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val phaseText = when (state.phase) {
                            SyncPhase.TYPES -> "Downloading type data..."
                            SyncPhase.POKEMON -> "Downloading Pokemon ${state.current}/${state.total}..."
                            SyncPhase.MOVES -> "Downloading moves ${state.current}/${state.total}..."
                            SyncPhase.SPRITES -> "Processing Pokemon moves..."
                            SyncPhase.TRAINERS -> "Seeding trainers..."
                            null -> "Preparing sync..."
                        }
                        Text(
                            text = phaseText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))

                        // Progress bar
                        val ratio = if (state.total > 0) state.current.toFloat() / state.total.toFloat() else 0f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(BorderShadow)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(ratio.coerceIn(0f, 1f))
                                    .height(16.dp)
                                    .background(HpHigh)
                            )
                            // Percentage text
                            Text(
                                text = "${(ratio * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}
