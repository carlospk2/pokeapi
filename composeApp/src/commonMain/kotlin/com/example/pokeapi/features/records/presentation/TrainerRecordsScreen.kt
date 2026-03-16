package com.example.pokeapi.features.records.presentation

import androidx.compose.foundation.background
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokeapi.shared.database.dao.BattleRecordDao
import com.example.pokeapi.shared.database.dao.TrainerDao
import com.example.pokeapi.shared.database.entity.BattleRecordEntity
import com.example.pokeapi.shared.database.entity.TrainerEntity
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.HpHigh
import com.example.pokeapi.ui.theme.HpLow
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

@Composable
fun TrainerRecordsScreen(trainerId: Int) {
    val battleRecordDao: BattleRecordDao = koinInject()
    val trainerDao: TrainerDao = koinInject()

    var records by remember { mutableStateOf<List<BattleRecordEntity>>(emptyList()) }
    var trainer by remember { mutableStateOf<TrainerEntity?>(null) }

    LaunchedEffect(trainerId) {
        trainer = trainerDao.getById(trainerId)
        records = battleRecordDao.getByTrainer(trainerId).first()
    }

    val wins = records.count { it.result == "win" }
    val losses = records.count { it.result == "loss" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        RetroBox(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Surface
        ) {
            Column {
                Text(
                    trainer?.name?.uppercase() ?: "TRAINER #$trainerId",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Row {
                    Text("Wins: $wins", style = MaterialTheme.typography.bodyLarge, color = HpHigh)
                    Text("  |  ", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                    Text("Losses: $losses", style = MaterialTheme.typography.bodyLarge, color = HpLow)
                }
            }
        }

        if (records.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Never battled this trainer!", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            }
        } else {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(records) { record ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceVariant)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (record.result == "win") "WIN" else "LOSS",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (record.result == "win") HpHigh else HpLow,
                            modifier = Modifier.weight(0.3f)
                        )
                        Text(
                            "${record.turnsCount} turns",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(0.4f)
                        )
                        Text(
                            "${record.pokemonRemaining} left",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.weight(0.3f)
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}
