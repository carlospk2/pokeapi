package com.example.pokeapi.shared.features.battle.engine

import kotlin.random.Random

class BattleAi(
    private val strategy: String,
    private val typeChart: Map<Pair<String, String>, Float>,
    private val random: (min: Double, max: Double) -> Double = { min, max -> Random.nextDouble(min, max) }
) {
    fun decideAction(
        state: BattleState,
        engine: BattleEngine
    ): BattleAction {
        val aiPokemon = state.opponentActive
        val playerPokemon = state.playerActive
        val availableMoves = engine.getAvailableMoves(aiPokemon)

        return when (strategy.lowercase()) {
            "easy" -> easyStrategy(availableMoves)
            "medium" -> mediumStrategy(availableMoves, aiPokemon, playerPokemon)
            "hard" -> hardStrategy(availableMoves, aiPokemon, playerPokemon, state, engine)
            else -> easyStrategy(availableMoves)
        }
    }

    /**
     * Easy: Random move from available moves
     */
    private fun easyStrategy(availableMoves: List<BattleMove>): BattleAction {
        val move = availableMoves.random()
        return BattleAction.UseMove(move.id)
    }

    /**
     * Medium: Prefer super effective moves; otherwise highest power
     */
    private fun mediumStrategy(
        availableMoves: List<BattleMove>,
        attacker: BattlePokemon,
        defender: BattlePokemon
    ): BattleAction {
        // Check for super effective moves first
        val superEffective = availableMoves.filter { move ->
            TypeEffectivenessCalculator.calculate(move.type, defender.types, typeChart) >= 2.0f
        }
        if (superEffective.isNotEmpty()) {
            return BattleAction.UseMove(superEffective.maxByOrNull { it.power }!!.id)
        }

        // Otherwise use highest power move
        val bestMove = availableMoves.maxByOrNull { it.power }!!
        return BattleAction.UseMove(bestMove.id)
    }

    /**
     * Hard: Calculate estimated damage for each move and pick best
     * Also considers switching if severely outmatched by type
     */
    private fun hardStrategy(
        availableMoves: List<BattleMove>,
        attacker: BattlePokemon,
        defender: BattlePokemon,
        state: BattleState,
        engine: BattleEngine
    ): BattleAction {
        // Check if current pokemon is severely type-disadvantaged
        val opponentAttackTypeEff = state.opponentTeam.indices
            .filter { idx -> !state.opponentTeam[idx].isFainted && idx != state.opponentActiveIndex }
            .any { idx ->
                val candidate = state.opponentTeam[idx]
                val candidateMoves = engine.getAvailableMoves(candidate)
                val bestEff = candidateMoves.maxOfOrNull { move ->
                    TypeEffectivenessCalculator.calculate(move.type, defender.types, typeChart)
                } ?: 1.0f
                bestEff > 2.0f
            }

        val currentDisadvantage = availableMoves.all { move ->
            TypeEffectivenessCalculator.calculate(move.type, defender.types, typeChart) < 0.5f
        }

        if (currentDisadvantage && opponentAttackTypeEff) {
            // Consider switching to a better pokemon
            val bestSwitchIndex = (state.opponentTeam.indices)
                .filter { idx -> !state.opponentTeam[idx].isFainted && idx != state.opponentActiveIndex }
                .maxByOrNull { idx ->
                    val candidate = state.opponentTeam[idx]
                    val candidateMoves = engine.getAvailableMoves(candidate)
                    candidateMoves.maxOfOrNull { move ->
                        TypeEffectivenessCalculator.calculate(move.type, defender.types, typeChart).toDouble()
                    } ?: 0.0
                }
            if (bestSwitchIndex != null && random(0.0, 1.0) > 0.3) {
                return BattleAction.SwitchPokemon(bestSwitchIndex)
            }
        }

        // Estimate damage for each available move and pick highest
        val bestMove = availableMoves.maxByOrNull { move ->
            val stab = move.type in attacker.types
            val typeEff = TypeEffectivenessCalculator.calculate(move.type, defender.types, typeChart)
            val attackStat = if (move.category == "physical") attacker.attack else attacker.spAttack
            val defenseStat = if (move.category == "physical") defender.defense else defender.spDefense

            DamageCalculator.calculate(
                level = attacker.level,
                power = move.power,
                attack = attackStat,
                defense = defenseStat,
                stab = stab,
                typeEffectiveness = typeEff,
                random = 1.0f
            ).toDouble()
        } ?: availableMoves.first()

        return BattleAction.UseMove(bestMove.id)
    }
}
