package com.example.pokeapi.features.records.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.HpHigh
import com.example.pokeapi.ui.theme.HpLow
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RecordsScreen(
    onTrainerDetail: (Int) -> Unit,
    viewModel: RecordsViewModel = koinViewModel()
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
                "RECORDS",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Stats overview
        RetroBox(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            backgroundColor = Surface
        ) {
            val stats = state.playerStats
            Column {
                Text("TRAINER CARD", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatBox(label = "Wins", value = stats.totalWins.toString(), color = HpHigh)
                    StatBox(label = "Losses", value = stats.totalLosses.toString(), color = HpLow)
                    StatBox(label = "Total", value = stats.totalBattles.toString(), color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    StatBox(label = "Streak", value = stats.currentStreak.toString(), color = MaterialTheme.colorScheme.onSurface)
                    StatBox(label = "Best", value = stats.bestStreak.toString(), color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Recent battles
        Text(
            "RECENT BATTLES",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        if (state.recentBattles.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No battles recorded yet!", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                items(state.recentBattles) { battle ->
                    BattleHistoryRow(
                        battle = battle,
                        onClick = { onTrainerDetail(battle.trainerId) }
                    )
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    }
}

@Composable
private fun BattleHistoryRow(battle: BattleHistoryItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariant)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (battle.result == "win") "WIN" else "LOSS",
            style = MaterialTheme.typography.bodyLarge,
            color = if (battle.result == "win") HpHigh else HpLow,
            modifier = Modifier.weight(0.2f)
        )
        Column(modifier = Modifier.weight(0.6f)) {
            Text("vs Trainer #${battle.trainerId}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text("${battle.turnsCount} turns", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
        Text(">", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
    }
}
