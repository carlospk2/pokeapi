package com.example.pokeapi.features.teams.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import com.example.pokeapi.ui.components.RetroButton
import com.example.pokeapi.ui.components.TypeBadge
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Primary
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MoveSelectorScreen(
    teamId: Long,
    slot: Int,
    pokemonId: Int,
    onSaved: () -> Unit,
    viewModel: MoveSelectorViewModel = koinViewModel(
        key = "move_selector_${teamId}_${slot}_${pokemonId}",
        parameters = { parametersOf(teamId, slot, pokemonId) }
    )
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("SELECT MOVES", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                    Text("${state.selectedMoveIds.size}/4 selected", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }
                RetroButton(
                    text = "Save",
                    onClick = {
                        viewModel.saveMoves()
                        onSaved()
                    }
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.availableMoves) { move ->
                val isSelected = move.id in state.selectedMoveIds
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSelected) Primary.copy(alpha = 0.3f) else SurfaceVariant)
                        .clickable { viewModel.toggleMove(move.id) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSelected) "✓" else "○",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isSelected) Primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.width(20.dp)
                    )
                    TypeBadge(type = move.type, modifier = Modifier.width(56.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        move.name.uppercase().replace("-", " "),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "PWR:${move.power ?: "-"}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "PP:${move.pp}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
