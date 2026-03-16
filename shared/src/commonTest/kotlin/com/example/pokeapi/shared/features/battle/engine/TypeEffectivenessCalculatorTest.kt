package com.example.pokeapi.shared.features.battle.engine

import kotlin.test.Test
import kotlin.test.assertEquals

class TypeEffectivenessCalculatorTest {

    private val typeChart: Map<Pair<String, String>, Float> = mapOf(
        ("fire" to "grass") to 2.0f,
        ("fire" to "water") to 0.5f,
        ("fire" to "fire") to 0.5f,
        ("water" to "fire") to 2.0f,
        ("water" to "grass") to 0.5f,
        ("electric" to "water") to 2.0f,
        ("electric" to "ground") to 0.0f,
        ("grass" to "water") to 2.0f,
        ("normal" to "ghost") to 0.0f,
        ("fighting" to "ghost") to 0.0f,
        ("ghost" to "normal") to 0.0f,
        ("psychic" to "dark") to 0.5f,
        ("ice" to "water") to 0.5f,
        ("rock" to "fire") to 2.0f,
        ("dragon" to "dragon") to 2.0f,
        ("dark" to "psychic") to 2.0f,
    )

    @Test
    fun calculate_superEffective_returns2() {
        val result = TypeEffectivenessCalculator.calculate("fire", listOf("grass"), typeChart)
        assertEquals(2.0f, result)
    }

    @Test
    fun calculate_notVeryEffective_returns0point5() {
        val result = TypeEffectivenessCalculator.calculate("fire", listOf("water"), typeChart)
        assertEquals(0.5f, result)
    }

    @Test
    fun calculate_immune_returns0() {
        val result = TypeEffectivenessCalculator.calculate("electric", listOf("ground"), typeChart)
        assertEquals(0.0f, result)
    }

    @Test
    fun calculate_neutral_returns1WhenNotInChart() {
        val result = TypeEffectivenessCalculator.calculate("normal", listOf("normal"), typeChart)
        assertEquals(1.0f, result)
    }

    @Test
    fun calculate_dualType_superEffectiveBoth_returns4() {
        // fire vs grass/ice: fire vs grass=2, fire vs ice not in chart=1, total=2
        // Use electric vs water/flying: water=2, flying not in chart=1 -> 2
        // For 4x: need two types each weak to same move
        // grass/ice not in our chart. Let's use a chart entry that gives 4x
        val doubleWeakChart = typeChart + mapOf(
            ("fire" to "ice") to 2.0f
        )
        val result = TypeEffectivenessCalculator.calculate("fire", listOf("grass", "ice"), doubleWeakChart)
        assertEquals(4.0f, result)
    }

    @Test
    fun calculate_dualType_oneImmune_returns0() {
        // Normal vs Ghost/Normal: ghost=0, so total = 0
        val result = TypeEffectivenessCalculator.calculate("normal", listOf("ghost", "normal"), typeChart)
        // normal vs ghost = 0.0, normal vs normal not in chart = 1.0 -> 0.0
        assertEquals(0.0f, result)
    }

    @Test
    fun calculate_dualType_superAndResisted_returns1() {
        // fire vs grass/water: 2.0 * 0.5 = 1.0
        val result = TypeEffectivenessCalculator.calculate("fire", listOf("grass", "water"), typeChart)
        assertEquals(1.0f, result)
    }

    @Test
    fun calculate_singleType_resistedBoth_returns0point25() {
        // fire vs fire/water would be 0.5 * 0.5 = 0.25
        val result = TypeEffectivenessCalculator.calculate("fire", listOf("fire", "water"), typeChart)
        assertEquals(0.25f, result)
    }
}
