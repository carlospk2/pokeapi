package com.example.pokeapi.shared.features.battle.engine

object StatsCalculator {
    /**
     * HP formula: ((2 * base * level) / 100) + level + 10
     */
    fun calcHp(base: Int, level: Int): Int {
        return ((2 * base * level) / 100) + level + 10
    }

    /**
     * Other stats formula: ((2 * base * level) / 100) + 5
     */
    fun calcStat(base: Int, level: Int): Int {
        return ((2 * base * level) / 100) + 5
    }
}
