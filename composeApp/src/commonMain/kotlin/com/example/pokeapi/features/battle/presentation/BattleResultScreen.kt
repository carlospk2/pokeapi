package com.example.pokeapi.features.battle.presentation

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pokeapi.shared.database.dao.BattleRecordDao
import com.example.pokeapi.shared.database.entity.BattleRecordEntity
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.components.RetroButton
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.HpHigh
import com.example.pokeapi.ui.theme.HpLow
import com.example.pokeapi.ui.theme.Surface
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

@Composable
fun BattleResultScreen(
    battleRecordId: Long,
    onReturnToMenu: () -> Unit
) {
    val battleRecordDao: BattleRecordDao = koinInject()
    var record by remember { mutableStateOf<BattleRecordEntity?>(null) }

    LaunchedEffect(battleRecordId) {
        record = battleRecordDao.getAll().first()
            .firstOrNull { it.id == battleRecordId }
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
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            val isWin = record?.result == "win"
            Text(
                text = if (isWin) "VICTORY!" else "DEFEATED...",
                style = MaterialTheme.typography.displayLarge,
                color = if (isWin) HpHigh else HpLow,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))

            record?.let { rec ->
                RetroBox(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Surface
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text("Result: ${rec.result.uppercase()}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(4.dp))
                        Text("Turns: ${rec.turnsCount}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.height(4.dp))
                        Text("Pokemon remaining: ${rec.pokemonRemaining}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
            RetroButton(
                text = "Return to Menu",
                onClick = onReturnToMenu
            )
        }
    }
}
