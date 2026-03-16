package com.example.pokeapi.shared.features.battle.engine

object TypeEffectivenessCalculator {
    /**
     * Calculate type effectiveness multiplier for a move against a defender.
     * chart: Map of "attackingType to defendingType" -> multiplier
     */
    fun calculate(
        moveType: String,
        defenderTypes: List<String>,
        chart: Map<Pair<String, String>, Float>
    ): Float {
        var multiplier = 1.0f
        for (defType in defenderTypes) {
            multiplier *= chart[moveType to defType] ?: 1.0f
        }
        return multiplier
    }
}
