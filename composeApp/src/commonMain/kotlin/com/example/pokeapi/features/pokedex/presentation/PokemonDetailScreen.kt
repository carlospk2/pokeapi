package com.example.pokeapi.features.pokedex.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.pokeapi.features.pokedex.domain.model.PokemonDetail
import com.example.pokeapi.features.pokedex.domain.model.PokemonMove
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.components.TypeBadge
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import com.example.pokeapi.ui.theme.typeColor
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    viewModel: PokemonDetailViewModel = koinViewModel(parameters = { parametersOf(pokemonId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                }
            }
            state.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.error}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                }
            }
            state.pokemon != null -> {
                PokemonDetailContent(pokemon = state.pokemon!!)
            }
        }
    }
}

@Composable
private fun PokemonDetailContent(pokemon: PokemonDetail) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // Header
            RetroBox(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Surface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sprite
                    val spriteSource = pokemon.spriteLocalPath ?: pokemon.spriteUrl
                    AsyncImage(
                        model = spriteSource,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(128.dp),
                        filterQuality = FilterQuality.None,
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "#${pokemon.id.toString().padStart(3, '0')}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = pokemon.name.uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            TypeBadge(type = pokemon.typePrimary)
                            pokemon.typeSecondary?.let { TypeBadge(type = it) }
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "GEN ${pokemon.generation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        item {
            // Stats
            RetroBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                backgroundColor = SurfaceVariant
            ) {
                Column {
                    Text(
                        text = "BASE STATS",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    StatRow("HP", pokemon.baseHp, 255)
                    StatRow("ATK", pokemon.baseAttack, 255)
                    StatRow("DEF", pokemon.baseDefense, 255)
                    StatRow("SP.ATK", pokemon.baseSpAttack, 255)
                    StatRow("SP.DEF", pokemon.baseSpDefense, 255)
                    StatRow("SPD", pokemon.baseSpeed, 255)
                }
            }
        }

        item {
            // Moves header
            RetroBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                backgroundColor = SurfaceVariant
            ) {
                Text(
                    text = "MOVES (${pokemon.moves.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        items(pokemon.moves) { move ->
            MoveRow(move = move)
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int, max: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.width(52.dp)
        )
        Text(
            text = value.toString().padStart(3),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(24.dp)
        )
        Spacer(Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value.toFloat() / max.toFloat())
                    .height(6.dp)
                    .background(
                        when {
                            value >= 100 -> MaterialTheme.colorScheme.primary
                            value >= 60 -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
            )
        }
    }
}

@Composable
private fun MoveRow(move: PokemonMove) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariant)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TypeBadge(type = move.type, modifier = Modifier.width(56.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            text = move.name.uppercase().replace("-", " "),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "PWR:${move.power ?: "-"}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "PP:${move.pp}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
