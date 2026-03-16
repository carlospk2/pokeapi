package com.example.pokeapi.shared.features.battle.engine

import kotlin.test.Test
import kotlin.test.assertEquals

class StatsCalculatorTest {

    @Test
    fun calcHp_level50_base45_returnsCorrectValue() {
        // HP = ((2 * 45 * 50) / 100) + 50 + 10 = 45 + 50 + 10 = 105
        val result = StatsCalculator.calcHp(base = 45, level = 50)
        assertEquals(105, result)
    }

    @Test
    fun calcHp_level100_base45_returnsCorrectValue() {
        // HP = ((2 * 45 * 100) / 100) + 100 + 10 = 90 + 100 + 10 = 200
        val result = StatsCalculator.calcHp(base = 45, level = 100)
        assertEquals(200, result)
    }

    @Test
    fun calcHp_level1_base45_returnsMinimumValue() {
        // HP = ((2 * 45 * 1) / 100) + 1 + 10 = 0 + 1 + 10 = 11
        val result = StatsCalculator.calcHp(base = 45, level = 1)
        assertEquals(11, result)
    }

    @Test
    fun calcStat_level50_base80_returnsCorrectValue() {
        // Stat = ((2 * 80 * 50) / 100) + 5 = 80 + 5 = 85
        val result = StatsCalculator.calcStat(base = 80, level = 50)
        assertEquals(85, result)
    }

    @Test
    fun calcStat_level100_base100_returnsCorrectValue() {
        // Stat = ((2 * 100 * 100) / 100) + 5 = 200 + 5 = 205
        val result = StatsCalculator.calcStat(base = 100, level = 100)
        assertEquals(205, result)
    }

    @Test
    fun calcStat_level50_highBase_returnsCorrectValue() {
        // Base 150 (legendary), level 50: ((2 * 150 * 50) / 100) + 5 = 150 + 5 = 155
        val result = StatsCalculator.calcStat(base = 150, level = 50)
        assertEquals(155, result)
    }

    @Test
    fun calcHp_isAlwaysGreaterThanCalcStat_sameBaseAndLevel() {
        val hp = StatsCalculator.calcHp(base = 80, level = 50)
        val stat = StatsCalculator.calcStat(base = 80, level = 50)
        assert(hp > stat) { "HP ($hp) should always be greater than other stats ($stat) for same base/level" }
    }
}
