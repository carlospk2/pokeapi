package com.example.pokeapi.features.teams.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.components.RetroButton
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TeamEditScreen(
    teamId: Long?,
    onSelectPokemon: (teamId: Long, slot: Int) -> Unit,
    onSelectMoves: (teamId: Long, slot: Int, pokemonId: Int) -> Unit,
    onSaved: () -> Unit,
    viewModel: TeamEditViewModel = koinViewModel(parameters = { parametersOf(teamId) })
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (teamId == null) "NEW TEAM" else "EDIT TEAM",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                RetroButton(
                    text = "Save",
                    onClick = {
                        viewModel.saveTeam()
                        onSaved()
                    }
                )
            }
        }

        // Team name
        RetroBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            backgroundColor = SurfaceVariant
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Name: ", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                BasicTextField(
                    value = state.teamName,
                    onValueChange = { viewModel.updateName(it) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
        }

        // Team slots
        val teamId = state.teamId
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items((1..6).toList()) { slot ->
                val member = state.members.find { it.slot == slot }
                TeamSlotRow(
                    slot = slot,
                    member = member,
                    onSelectPokemon = {
                        if (teamId != null) onSelectPokemon(teamId, slot)
                    },
                    onSelectMoves = { pokemonId ->
                        if (teamId != null) onSelectMoves(teamId, slot, pokemonId)
                    },
                    onRemove = { viewModel.removeMember(slot) }
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun TeamSlotRow(
    slot: Int,
    member: TeamMemberDisplay?,
    onSelectPokemon: () -> Unit,
    onSelectMoves: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariant)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "[$slot]",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.width(28.dp)
        )

        if (member != null) {
            val spriteSource = member.spriteLocalPath ?: member.spriteUrl
            AsyncImage(
                model = spriteSource,
                contentDescription = member.pokemonName,
                modifier = Modifier.size(40.dp),
                filterQuality = FilterQuality.None,
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    member.pokemonName.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Lv.${member.level} | ${member.moveCount} moves",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            RetroButton(text = "Moves", onClick = { onSelectMoves(member.pokemonId) })
            Spacer(Modifier.width(4.dp))
            RetroButton(text = "X", onClick = onRemove)
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clickable(onClick = onSelectPokemon),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "-- Empty Slot --",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
