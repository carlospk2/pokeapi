package com.example.pokeapi.shared.features.battle.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DamageCalculatorTest {

    @Test
    fun calculate_zeroTypeEffectiveness_returnsZero() {
        val result = DamageCalculator.calculate(
            level = 50,
            power = 100,
            attack = 100,
            defense = 100,
            stab = false,
            typeEffectiveness = 0.0f,
            random = 1.0f
        )
        assertEquals(0, result)
    }

    @Test
    fun calculate_neutralEffectiveness_noStab_returnsBaseDamage() {
        // levelMod = floor(2*50/5+2) = floor(22) = 22
        // baseDamage = floor((22 * 80 * (80.0/80.0)) / 50 + 2) = floor(22*80/50 + 2) = floor(35.2+2) = floor(37.2) = 37
        // result = floor(37 * 1.0 * 1.0 * 1.0) = 37
        val result = DamageCalculator.calculate(
            level = 50,
            power = 80,
            attack = 80,
            defense = 80,
            stab = false,
            typeEffectiveness = 1.0f,
            random = 1.0f
        )
        assertEquals(37, result)
    }

    @Test
    fun calculate_stabBonus_increasesBy50Percent() {
        val withoutStab = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = false, typeEffectiveness = 1.0f, random = 1.0f
        )
        val withStab = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = true, typeEffectiveness = 1.0f, random = 1.0f
        )
        assertTrue(withStab > withoutStab, "STAB should increase damage")
        // STAB adds 50%: floor(37 * 1.5) = floor(55.5) = 55
        assertEquals(55, withStab)
    }

    @Test
    fun calculate_superEffective_doublesBaseDamage() {
        val normalDamage = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = false, typeEffectiveness = 1.0f, random = 1.0f
        )
        val superEffDamage = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = false, typeEffectiveness = 2.0f, random = 1.0f
        )
        assertEquals(normalDamage * 2, superEffDamage)
    }

    @Test
    fun calculate_notVeryEffective_halvesBaseDamage() {
        val normalDamage = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = false, typeEffectiveness = 1.0f, random = 1.0f
        )
        val notVeryEff = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = false, typeEffectiveness = 0.5f, random = 1.0f
        )
        // floor(37 * 0.5) = floor(18.5) = 18
        assertEquals(18, notVeryEff)
        assertTrue(notVeryEff < normalDamage)
    }

    @Test
    fun calculate_randomFactor_affectsResult() {
        val maxDamage = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = false, typeEffectiveness = 1.0f, random = 1.0f
        )
        val minDamage = DamageCalculator.calculate(
            level = 50, power = 80, attack = 80, defense = 80,
            stab = false, typeEffectiveness = 1.0f, random = 0.85f
        )
        assertTrue(minDamage <= maxDamage, "Random factor 0.85 should give less or equal damage than 1.0")
    }

    @Test
    fun calculate_highLevelHighPower_returnsHighDamage() {
        val result = DamageCalculator.calculate(
            level = 100, power = 120, attack = 200, defense = 50,
            stab = true, typeEffectiveness = 2.0f, random = 1.0f
        )
        assertTrue(result > 500, "High level/power/stats should produce large damage (got $result)")
    }
}
