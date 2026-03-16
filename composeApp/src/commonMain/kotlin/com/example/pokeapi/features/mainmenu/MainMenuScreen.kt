package com.example.pokeapi.features.mainmenu

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pokeapi.ui.theme.Background
import com.example.pokeapi.ui.theme.Primary
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant

data class MenuOption(val label: String, val description: String)

@Composable
fun MainMenuScreen(
    onPokedex: () -> Unit,
    onTeams: () -> Unit,
    onBattle: () -> Unit,
    onRecords: () -> Unit
) {
    val menuOptions = listOf(
        MenuOption("BATTLE", "Fight trainers"),
        MenuOption("POKEDEX", "View pokemon"),
        MenuOption("MY TEAMS", "Manage teams"),
        MenuOption("RECORDS", "View history")
    )
    val actions = listOf(onBattle, onPokedex, onTeams, onRecords)

    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Game title
            Text(
                text = "POKEAPI",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = "BATTLE GAME",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(48.dp))

            // Menu options
            menuOptions.forEachIndexed { index, option ->
                MenuItemRow(
                    option = option,
                    isSelected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                        actions[index]()
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MenuItemRow(
    option: MenuOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Primary else Surface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cursor indicator
        Text(
            text = if (isSelected) ">" else " ",
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = option.label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
