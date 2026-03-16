package com.example.pokeapi.shared.features.battle.engine

import kotlin.math.floor

object DamageCalculator {
    /**
     * Damage formula:
     * floor((floor(2*level/5+2) * power * (atk/def)) / 50 + 2) * stab * typeEffectiveness * random
     */
    fun calculate(
        level: Int,
        power: Int,
        attack: Int,
        defense: Int,
        stab: Boolean,
        typeEffectiveness: Float,
        random: Float
    ): Int {
        if (typeEffectiveness == 0.0f) return 0

        val levelMod = floor(2.0 * level / 5.0 + 2).toInt()
        val baseDamage = floor(
            (levelMod.toDouble() * power.toDouble() * (attack.toDouble() / defense.toDouble())) / 50.0 + 2.0
        ).toInt()
        val stabMultiplier = if (stab) 1.5f else 1.0f
        return floor(baseDamage.toDouble() * stabMultiplier * typeEffectiveness * random).toInt()
    }
}
