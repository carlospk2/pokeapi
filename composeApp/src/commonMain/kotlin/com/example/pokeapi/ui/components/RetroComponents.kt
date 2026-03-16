package com.example.pokeapi.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokeapi.ui.theme.BorderHighlight
import com.example.pokeapi.ui.theme.BorderShadow
import com.example.pokeapi.ui.theme.HpHigh
import com.example.pokeapi.ui.theme.HpLow
import com.example.pokeapi.ui.theme.HpMedium
import com.example.pokeapi.ui.theme.Surface
import com.example.pokeapi.ui.theme.SurfaceVariant
import com.example.pokeapi.ui.theme.typeColor

@Composable
fun RetroBox(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Surface,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .border(2.dp, BorderHighlight)
            .padding(1.dp)
            .border(1.dp, BorderShadow)
            .padding(8.dp),
        content = content
    )
}

@Composable
fun RetroButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SurfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .border(2.dp, BorderHighlight)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TypeBadge(
    type: String,
    modifier: Modifier = Modifier
) {
    val color = typeColor(type)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(color)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = type.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontSize = 6.sp
        )
    }
}

@Composable
fun HpBar(
    currentHp: Int,
    maxHp: Int,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    showNumbers: Boolean = false
) {
    val ratio = if (maxHp > 0) currentHp.toFloat() / maxHp.toFloat() else 0f
    val barColor = when {
        ratio > 0.5f -> HpHigh
        ratio > 0.25f -> HpMedium
        else -> HpLow
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showNumbers) {
            Text(
                text = "$currentHp/$maxHp",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(4.dp))
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(height)
                .background(BorderShadow)
                .border(1.dp, BorderHighlight)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .height(height)
                    .background(barColor)
            )
        }
    }
}

@Composable
fun RetroText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style
    )
}
