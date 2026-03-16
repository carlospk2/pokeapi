package com.example.pokeapi.features.pokedex.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.pokeapi.features.pokedex.domain.model.Pokemon
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.components.TypeBadge
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.BorderHighlight
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import com.example.pokeapi.ui.theme.typeColor
import org.koin.compose.viewmodel.koinViewModel

private val ALL_TYPES = listOf(
    "normal", "fire", "water", "grass", "electric", "ice",
    "fighting", "poison", "ground", "flying", "psychic", "bug",
    "rock", "ghost", "dragon", "dark", "steel", "fairy"
)

@Composable
fun PokedexScreen(
    onPokemonClick: (Int) -> Unit,
    viewModel: PokedexViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header
        RetroBox(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Surface
        ) {
            Text(
                text = "POKEDEX",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(4.dp)
            )
        }

        // Search bar
        RetroBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            backgroundColor = SurfaceVariant
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "> ",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                BasicTextField(
                    value = state.searchQuery,
                    onValueChange = { viewModel.onEvent(PokedexEvent.SearchQueryChanged(it)) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    decorationBox = { inner ->
                        if (state.searchQuery.isEmpty()) {
                            Text(
                                "Search pokemon...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        inner()
                    }
                )
            }
        }

        // Type filter row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                TypeFilterChip(
                    label = "ALL",
                    selected = state.selectedType == null && state.selectedGeneration == null && state.searchQuery.isEmpty(),
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        viewModel.onEvent(PokedexEvent.TypeFilterSelected(null))
                        viewModel.onEvent(PokedexEvent.GenerationFilterSelected(null))
                    }
                )
            }
            items(listOf(1, 2, 3)) { gen ->
                TypeFilterChip(
                    label = "GEN $gen",
                    selected = state.selectedGeneration == gen,
                    color = MaterialTheme.colorScheme.primary,
                    onClick = { viewModel.onEvent(PokedexEvent.GenerationFilterSelected(if (state.selectedGeneration == gen) null else gen)) }
                )
            }
            items(ALL_TYPES) { type ->
                TypeFilterChip(
                    label = type.uppercase(),
                    selected = state.selectedType == type,
                    color = typeColor(type),
                    onClick = { viewModel.onEvent(PokedexEvent.TypeFilterSelected(if (state.selectedType == type) null else type)) }
                )
            }
        }

        // Pokemon Grid
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } else if (state.pokemon.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No Pokemon found", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onBackground)
                    Text("Try sync first", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.pokemon) { pokemon ->
                    PokemonGridItem(
                        pokemon = pokemon,
                        onClick = { onPokemonClick(pokemon.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TypeFilterChip(
    label: String,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(if (selected) color else SurfaceVariant)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            fontSize = 6.sp
        )
    }
}

@Composable
private fun PokemonGridItem(
    pokemon: Pokemon,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Surface)
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sprite
        val spriteSource = pokemon.spriteLocalPath ?: pokemon.spriteUrl
        AsyncImage(
            model = spriteSource,
            contentDescription = pokemon.name,
            modifier = Modifier.size(48.dp),
            filterQuality = FilterQuality.None,
            contentScale = ContentScale.Fit
        )
        // Number
        Text(
            text = "#${pokemon.id.toString().padStart(3, '0')}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 6.sp
        )
        // Name
        Text(
            text = pokemon.name.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 6.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(2.dp))
        // Types
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            TypeBadge(type = pokemon.typePrimary)
            pokemon.typeSecondary?.let { TypeBadge(type = it) }
        }
    }
}
