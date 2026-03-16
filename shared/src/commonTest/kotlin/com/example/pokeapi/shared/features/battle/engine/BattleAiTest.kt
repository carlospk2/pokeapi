package com.example.pokeapi.shared.features.battle.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BattleAiTest {

    private val typeChart: Map<Pair<String, String>, Float> = mapOf(
        ("fire" to "grass") to 2.0f,
        ("fire" to "water") to 0.5f,
        ("water" to "fire") to 2.0f,
        ("electric" to "water") to 2.0f,
        ("electric" to "ground") to 0.0f,
    )

    private val deterministicEngine = BattleEngine(
        typeChart = typeChart,
        random = { min, max -> (min + max) / 2.0 }
    )

    private fun makeMove(id: Int, name: String, type: String, power: Int) = BattleMove(
        id = id, name = name, type = type, category = "special",
        power = power, accuracy = 100, pp = 15
    )

    private fun makePokemon(
        name: String,
        typePrimary: String,
        moves: List<BattleMove>,
        level: Int = 50
    ): BattlePokemon = deterministicEngine.createBattlePokemon(
        id = moves.firstOrNull()?.id ?: 1, name = name,
        typePrimary = typePrimary, typeSecondary = null,
        level = level, baseHp = 70, baseAtk = 70, baseDef = 70,
        baseSpAtk = 70, baseSpDef = 70, baseSpd = 70,
        moves = moves
    )

    @Test
    fun easyStrategy_returnsValidMove() {
        val ai = BattleAi(strategy = "easy", typeChart = typeChart)
        val moveFire = makeMove(1, "ember", "fire", 40)
        val attacker = makePokemon("charmander", "fire", listOf(moveFire))
        val defender = makePokemon("bulbasaur", "grass", listOf(makeMove(2, "tackle", "normal", 40)))

        val state = BattleState(
            playerTeam = mutableListOf(defender),
            opponentTeam = mutableListOf(attacker)
        )

        val action = ai.decideAction(state, deterministicEngine)
        assertTrue(action is BattleAction.UseMove, "Easy AI should use a move")
    }

    @Test
    fun mediumStrategy_prefersSuperEffectiveMove() {
        val ai = BattleAi(strategy = "medium", typeChart = typeChart)
        val weakMove = makeMove(1, "splash", "normal", 10)
        val strongSuperEffMove = makeMove(2, "fire-blast", "fire", 110)
        val normalPowerMove = makeMove(3, "tackle", "normal", 50)

        // AI pokemon has all three moves
        val attacker = makePokemon("charizard", "fire", listOf(weakMove, strongSuperEffMove, normalPowerMove))
        // Defender is grass type (weak to fire)
        val defender = makePokemon("bulbasaur", "grass", listOf(makeMove(4, "tackle", "normal", 40)))

        val state = BattleState(
            playerTeam = mutableListOf(defender),
            opponentTeam = mutableListOf(attacker)
        )

        val action = ai.decideAction(state, deterministicEngine)
        assertTrue(action is BattleAction.UseMove)
        assertEquals(strongSuperEffMove.id, (action as BattleAction.UseMove).moveId,
            "Medium AI should prefer the strongest super effective move")
    }

    @Test
    fun mediumStrategy_noSuperEffective_usesHighestPowerMove() {
        val ai = BattleAi(strategy = "medium", typeChart = typeChart)
        val lowPower = makeMove(1, "ember", "fire", 40)
        val highPower = makeMove(2, "fire-blast", "fire", 110)

        // Defender is fire type (resists fire)
        val attacker = makePokemon("charizard", "fire", listOf(lowPower, highPower))
        val defender = makePokemon("flareon", "fire", listOf(makeMove(3, "tackle", "normal", 40)))

        val state = BattleState(
            playerTeam = mutableListOf(defender),
            opponentTeam = mutableListOf(attacker)
        )

        val action = ai.decideAction(state, deterministicEngine)
        assertTrue(action is BattleAction.UseMove)
        assertEquals(highPower.id, (action as BattleAction.UseMove).moveId,
            "Medium AI should use highest power move when no super effective moves available")
    }

    @Test
    fun hardStrategy_picksBestDamageMove() {
        // Deterministic random so no switching
        val ai = BattleAi(strategy = "hard", typeChart = typeChart, random = { _, _ -> 1.0 })
        val weakMove = makeMove(1, "splash", "normal", 1)
        val strongWaterMove = makeMove(2, "hydro-pump", "water", 110)

        // AI is water type, uses water move vs fire defender
        val attacker = makePokemon("blastoise", "water", listOf(weakMove, strongWaterMove))
        val defender = makePokemon("charizard", "fire", listOf(makeMove(3, "tackle", "normal", 40)))

        val state = BattleState(
            playerTeam = mutableListOf(defender),
            opponentTeam = mutableListOf(attacker)
        )

        val action = ai.decideAction(state, deterministicEngine)
        assertTrue(action is BattleAction.UseMove)
        assertEquals(strongWaterMove.id, (action as BattleAction.UseMove).moveId,
            "Hard AI should pick the move that deals the most damage")
    }

    @Test
    fun unknownStrategy_fallsBackToRandom() {
        val ai = BattleAi(strategy = "unknown_strategy", typeChart = typeChart)
        val move = makeMove(1, "tackle", "normal", 40)
        val attacker = makePokemon("rattata", "normal", listOf(move))
        val defender = makePokemon("rattata2", "normal", listOf(makeMove(2, "tackle", "normal", 40)))

        val state = BattleState(
            playerTeam = mutableListOf(defender),
            opponentTeam = mutableListOf(attacker)
        )

        // Should not throw
        val action = ai.decideAction(state, deterministicEngine)
        assertTrue(action is BattleAction.UseMove, "Unknown strategy should still return a valid action")
    }
}
