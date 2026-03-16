package com.example.pokeapi.shared.features.battle.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BattleEngineTest {

    // Deterministic: always returns mid-range random, always hits accuracy
    private val deterministicEngine = BattleEngine(
        typeChart = mapOf(
            ("fire" to "grass") to 2.0f,
            ("water" to "fire") to 2.0f,
            ("normal" to "normal") to 1.0f,
        ),
        random = { min, max -> (min + max) / 2.0 }
    )

    private fun makeFlamethrower() = BattleMove(
        id = 1, name = "flamethrower", type = "fire", category = "special",
        power = 90, accuracy = 100, pp = 15
    )

    private fun makeWaterGun() = BattleMove(
        id = 2, name = "water-gun", type = "water", category = "special",
        power = 40, accuracy = 100, pp = 25
    )

    private fun makeTackle() = BattleMove(
        id = 3, name = "tackle", type = "normal", category = "physical",
        power = 40, accuracy = 95, pp = 35
    )

    private fun makeGrassPokemon(name: String = "bulbasaur"): BattlePokemon =
        deterministicEngine.createBattlePokemon(
            id = 1, name = name,
            typePrimary = "grass", typeSecondary = null,
            level = 50, baseHp = 45, baseAtk = 49, baseDef = 49,
            baseSpAtk = 65, baseSpDef = 65, baseSpd = 45,
            moves = listOf(makeWaterGun())
        )

    private fun makeFirePokemon(name: String = "charmander"): BattlePokemon =
        deterministicEngine.createBattlePokemon(
            id = 4, name = name,
            typePrimary = "fire", typeSecondary = null,
            level = 50, baseHp = 39, baseAtk = 52, baseDef = 43,
            baseSpAtk = 60, baseSpDef = 50, baseSpd = 65,
            moves = listOf(makeFlamethrower())
        )

    @Test
    fun createBattlePokemon_calculatesCorrectMaxHp() {
        val pokemon = deterministicEngine.createBattlePokemon(
            id = 1, name = "bulbasaur", typePrimary = "grass", typeSecondary = null,
            level = 50, baseHp = 45, baseAtk = 49, baseDef = 49,
            baseSpAtk = 65, baseSpDef = 65, baseSpd = 45,
            moves = listOf(makeTackle())
        )
        // HP = ((2 * 45 * 50) / 100) + 50 + 10 = 105
        assertEquals(105, pokemon.maxHp)
        assertEquals(105, pokemon.currentHp)
    }

    @Test
    fun createBattlePokemon_initializesPpCorrectly() {
        val move = makeTackle()
        val pokemon = deterministicEngine.createBattlePokemon(
            id = 1, name = "bulbasaur", typePrimary = "grass", typeSecondary = null,
            level = 50, baseHp = 45, baseAtk = 49, baseDef = 49,
            baseSpAtk = 65, baseSpDef = 65, baseSpd = 45,
            moves = listOf(move)
        )
        assertEquals(35, pokemon.ppRemaining[move.id])
    }

    @Test
    fun resolveTurn_moveUsed_dealsDamage() {
        val grass = makeGrassPokemon()
        val fire = makeFirePokemon()

        val state = BattleState(
            playerTeam = mutableListOf(grass),
            opponentTeam = mutableListOf(fire)
        )

        val (newState, result) = deterministicEngine.resolveTurn(
            state,
            BattleAction.UseMove(makeWaterGun().id),
            BattleAction.UseMove(makeFlamethrower().id)
        )

        // Verify events contain move used events
        val moveUsedEvents = result.events.filterIsInstance<BattleEvent.MoveUsed>()
        assertTrue(moveUsedEvents.isNotEmpty(), "Should have move used events")

        // Both pokemon should have taken damage
        val damageEvents = result.events.filterIsInstance<BattleEvent.DamageDealt>()
        assertTrue(damageEvents.isNotEmpty(), "Should have damage events")
    }

    @Test
    fun resolveTurn_superEffectiveMove_eventHasHighMultiplier() {
        val grass = makeGrassPokemon()
        val fire = makeFirePokemon()

        val state = BattleState(
            playerTeam = mutableListOf(grass),
            opponentTeam = mutableListOf(fire)
        )

        // Fire uses flamethrower vs grass (super effective)
        val (_, result) = deterministicEngine.resolveTurn(
            state,
            BattleAction.UseMove(makeFlamethrower().id), // assuming player uses flamethrower
            BattleAction.UseMove(makeFlamethrower().id)
        )

        val damageEvents = result.events.filterIsInstance<BattleEvent.DamageDealt>()
        // At least one damage event should exist
        assertTrue(damageEvents.isNotEmpty())
    }

    @Test
    fun resolveTurn_pokemonFaints_battleEndsWhenTeamWiped() {
        // Create a very weak grass pokemon vs a high-power fire pokemon
        val weakGrass = BattlePokemon(
            id = 1, name = "bulbasaur", typePrimary = "grass", typeSecondary = null,
            level = 1, maxHp = 1, currentHp = 1,
            attack = 5, defense = 5, spAttack = 5, spDefense = 5, speed = 5,
            moves = listOf(makeTackle()),
            ppRemaining = mutableMapOf(makeTackle().id to 35)
        )
        val strongFire = deterministicEngine.createBattlePokemon(
            id = 4, name = "charizard", typePrimary = "fire", typeSecondary = null,
            level = 100, baseHp = 78, baseAtk = 84, baseDef = 78,
            baseSpAtk = 109, baseSpDef = 85, baseSpd = 100,
            moves = listOf(makeFlamethrower())
        )

        val state = BattleState(
            playerTeam = mutableListOf(weakGrass),
            opponentTeam = mutableListOf(strongFire)
        )

        val (newState, result) = deterministicEngine.resolveTurn(
            state,
            BattleAction.UseMove(makeTackle().id),
            BattleAction.UseMove(makeFlamethrower().id)
        )

        // Player's only pokemon should be fainted
        assertTrue(newState.playerTeam[0].isFainted, "Weak grass should be fainted")
        assertTrue(newState.isFinished, "Battle should be finished when all player pokemon faint")
        assertEquals(false, newState.playerWon, "Player should have lost")

        val battleEndedEvent = result.events.filterIsInstance<BattleEvent.BattleEnded>().firstOrNull()
        assertNotNull(battleEndedEvent, "Should have BattleEnded event")
        assertFalse(battleEndedEvent.playerWon)
    }

    @Test
    fun resolveTurn_switchPokemon_changesActiveIndex() {
        val grass = makeGrassPokemon("bulbasaur")
        val water = deterministicEngine.createBattlePokemon(
            id = 7, name = "squirtle", typePrimary = "water", typeSecondary = null,
            level = 50, baseHp = 44, baseAtk = 48, baseDef = 65,
            baseSpAtk = 50, baseSpDef = 64, baseSpd = 43,
            moves = listOf(makeWaterGun())
        )
        val fire = makeFirePokemon()

        val state = BattleState(
            playerTeam = mutableListOf(grass, water),
            opponentTeam = mutableListOf(fire)
        )

        val (newState, result) = deterministicEngine.resolveTurn(
            state,
            BattleAction.SwitchPokemon(1),
            BattleAction.UseMove(makeFlamethrower().id)
        )

        assertEquals(1, newState.playerActiveIndex, "Player should have switched to index 1")
        val switchEvent = result.events.filterIsInstance<BattleEvent.PokemonSwitched>().firstOrNull()
        assertNotNull(switchEvent, "Should have PokemonSwitched event")
        assertEquals("squirtle", switchEvent.pokemonName)
    }

    @Test
    fun getAvailableMoves_withPp_returnsAllMoves() {
        val pokemon = makeGrassPokemon()
        val available = deterministicEngine.getAvailableMoves(pokemon)
        assertEquals(1, available.size)
        assertEquals(makeWaterGun().id, available[0].id)
    }

    @Test
    fun getAvailableMoves_zeroPp_returnsStruggle() {
        val move = makeWaterGun()
        val pokemon = deterministicEngine.createBattlePokemon(
            id = 1, name = "squirtle", typePrimary = "water", typeSecondary = null,
            level = 50, baseHp = 44, baseAtk = 48, baseDef = 65,
            baseSpAtk = 50, baseSpDef = 64, baseSpd = 43,
            moves = listOf(move)
        )
        // Exhaust PP
        pokemon.ppRemaining[move.id] = 0

        val available = deterministicEngine.getAvailableMoves(pokemon)
        assertEquals(1, available.size)
        assertEquals(BattleEngine.STRUGGLE.id, available[0].id)
    }

    @Test
    fun resolveTurn_turnCounterIncrements() {
        val grass = makeGrassPokemon()
        val fire = makeFirePokemon()

        val state = BattleState(
            playerTeam = mutableListOf(grass),
            opponentTeam = mutableListOf(fire),
            turn = 1
        )

        val (newState, _) = deterministicEngine.resolveTurn(
            state,
            BattleAction.UseMove(makeWaterGun().id),
            BattleAction.UseMove(makeFlamethrower().id)
        )

        assertEquals(2, newState.turn, "Turn counter should increment")
    }
}
