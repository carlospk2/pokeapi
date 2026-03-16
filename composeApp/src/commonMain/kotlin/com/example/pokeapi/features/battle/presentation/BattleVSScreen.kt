package com.example.pokeapi.features.battle.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Primary
import kotlinx.coroutines.delay

@Composable
fun BattleVSScreen(
    trainerId: Int,
    playerTeamId: Long,
    onVSComplete: () -> Unit
) {
    var showVs by remember { mutableStateOf(false) }
    val vsAlpha by animateFloatAsState(
        targetValue = if (showVs) 1f else 0f,
        animationSpec = tween(500),
        label = "vsAlpha"
    )

    LaunchedEffect(Unit) {
        delay(300)
        showVs = true
        delay(1500)
        onVSComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .alpha(vsAlpha)
        ) {
            Text(
                text = "BATTLE!",
                style = MaterialTheme.typography.displayLarge,
                color = Primary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "YOU",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "VS",
                    style = MaterialTheme.typography.displayMedium,
                    color = Primary
                )
                Text(
                    text = "TRAINER\n#$trainerId",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
