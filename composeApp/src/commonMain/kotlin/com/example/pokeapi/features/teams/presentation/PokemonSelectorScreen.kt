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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
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
import com.example.pokeapi.ui.components.RetroBox
import com.example.pokeapi.ui.components.TypeBadge
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PokemonSelectorScreen(
    teamId: Long,
    slot: Int,
    onSelected: () -> Unit,
    viewModel: PokemonSelectorViewModel = koinViewModel(parameters = { parametersOf(teamId, slot) })
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
                "SELECT POKEMON (Slot $slot)",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Search
        RetroBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            backgroundColor = SurfaceVariant
        ) {
            BasicTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.search(it) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { inner ->
                    if (state.searchQuery.isEmpty()) {
                        Text("Search...", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                    inner()
                }
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.pokemon) { pokemon ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceVariant)
                        .clickable {
                            viewModel.selectPokemon(pokemon.id)
                            onSelected()
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val src = pokemon.spriteLocalPath ?: pokemon.spriteUrl
                    AsyncImage(
                        model = src,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(40.dp),
                        filterQuality = FilterQuality.None,
                        contentScale = ContentScale.Fit
                    )
                    Spacer(Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "#${pokemon.id.toString().padStart(3, '0')} ${pokemon.name.uppercase()}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row {
                            TypeBadge(type = pokemon.typePrimary)
                            pokemon.typeSecondary?.let {
                                Spacer(Modifier.width(4.dp))
                                TypeBadge(type = it)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}
