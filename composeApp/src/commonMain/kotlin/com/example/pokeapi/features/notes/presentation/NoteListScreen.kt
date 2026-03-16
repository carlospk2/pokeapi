package com.example.pokeapi.features.notes.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoteListScreen(
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    viewModel: NoteListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is NoteListEffect.NavigateToDetail -> onNavigateToDetail(effect.id)
                NoteListEffect.NavigateToCreate -> onNavigateToCreate()
                is NoteListEffect.ShowSnackbar -> { /* TODO: show snackbar */ }
            }
        }
    }

    NoteListContent(state = state, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteListContent(
    state: NoteListState,
    onEvent: (NoteListEvent) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Notes") }) }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text(state.error)
                else -> LazyColumn {
                    items(state.notes, key = { it.id }) { note ->
                        Text(text = note.title)
                    }
                }
            }
        }
    }
}
