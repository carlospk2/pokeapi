package com.example.pokeapi.features.trainers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Primary
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TrainerSelectScreen(
    onSelectTrainer: (Int) -> Unit,
    viewModel: TrainerSelectViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        RetroBox(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Surface
        ) {
            Text(
                "SELECT TRAINER",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (state.trainers.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No trainers loaded!", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                    Text("Sync data first", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                // Group by tier
                listOf(1, 2, 3, 4).forEach { tier ->
                    val tierTrainers = state.trainers.filter { it.difficultyTier == tier }
                    if (tierTrainers.isNotEmpty()) {
                        item {
                            val tierLabel = when (tier) {
                                1 -> "TIER 1 — NOVATO"
                                2 -> "TIER 2 — INTERMEDIO"
                                3 -> "TIER 3 — AVANZADO"
                                4 -> "TIER 4 — ELITE"
                                else -> "TIER $tier"
                            }
                            Text(
                                tierLabel,
                                style = MaterialTheme.typography.titleMedium,
                                color = tierColor(tier),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(tierTrainers) { trainer ->
                            TrainerRow(
                                trainer = trainer,
                                onClick = { onSelectTrainer(trainer.id) }
                            )
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun tierColor(tier: Int): Color = when (tier) {
    1 -> Color(0xFF4CAF50)
    2 -> Color(0xFFFFEB3B)
    3 -> Color(0xFFFF9800)
    4 -> Color(0xFFF44336)
    else -> Color.White
}

@Composable
private fun TrainerRow(trainer: TrainerDisplay, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariant)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(trainer.name, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Row {
                repeat(trainer.difficultyTier) {
                    Text("★", style = MaterialTheme.typography.labelSmall, color = tierColor(trainer.difficultyTier))
                }
                Spacer(Modifier.width(4.dp))
                Text(trainer.aiStrategy.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
        Text(">", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
    }
}
