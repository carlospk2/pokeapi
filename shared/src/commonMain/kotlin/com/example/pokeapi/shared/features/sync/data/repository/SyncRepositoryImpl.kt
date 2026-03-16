package com.example.pokeapi.shared.features.sync.data.repository

import com.example.pokeapi.shared.database.dao.MoveDao
import com.example.pokeapi.shared.database.dao.PokemonDao
import com.example.pokeapi.shared.database.dao.PokemonMoveDao
import com.example.pokeapi.shared.database.dao.SyncStatusDao
import com.example.pokeapi.shared.database.dao.TrainerDao
import com.example.pokeapi.shared.database.dao.TypeEffectivenessDao
import com.example.pokeapi.shared.database.entity.PokemonMoveEntity
import com.example.pokeapi.shared.database.entity.SyncStatusEntity
import com.example.pokeapi.shared.database.entity.TrainerEntity
import com.example.pokeapi.shared.features.sync.data.TrainerSeedData
import com.example.pokeapi.shared.features.sync.data.mapper.extractMoveIdFromUrl
import com.example.pokeapi.shared.features.sync.data.mapper.isValidBattleMove
import com.example.pokeapi.shared.features.sync.data.mapper.toEntity
import com.example.pokeapi.shared.features.sync.data.mapper.toTypeEffectivenessEntities
import com.example.pokeapi.shared.features.sync.data.remote.PokemonApiService
import com.example.pokeapi.shared.features.sync.domain.model.SyncPhase
import com.example.pokeapi.shared.features.sync.domain.model.SyncProgress
import com.example.pokeapi.shared.features.sync.domain.repository.SyncRepository
import com.example.pokeapi.shared.platform.fileExists
import com.example.pokeapi.shared.platform.getSpritesDirectory
import com.example.pokeapi.shared.platform.saveFile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SyncRepositoryImpl(
    private val apiService: PokemonApiService,
    private val pokemonDao: PokemonDao,
    private val moveDao: MoveDao,
    private val pokemonMoveDao: PokemonMoveDao,
    private val typeEffectivenessDao: TypeEffectivenessDao,
    private val syncStatusDao: SyncStatusDao,
    private val trainerDao: TrainerDao
) : SyncRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override fun startSync(): Flow<SyncProgress> = flow {
        emit(SyncProgress.InProgress(SyncPhase.TYPES, 0, 18))

        try {
            // Phase 1: Download Types (1-18)
            for (typeId in 1..18) {
                try {
                    val typeDto = apiService.getType(typeId)
                    val entities = typeDto.toTypeEffectivenessEntities()
                    typeEffectivenessDao.upsertAll(entities)
                    delay(100)
                } catch (e: Exception) {
                    // log but continue
                }
                emit(SyncProgress.InProgress(SyncPhase.TYPES, typeId, 18))
            }

            // Phase 2: Download Pokemon 1-386 and collect move IDs
            val syncStatus = syncStatusDao.get() ?: SyncStatusEntity()
            val startFromPokemon = if (syncStatus.isCompleted) 1 else (syncStatus.lastSyncedPokemonId + 1)
            val totalPokemon = 386
            val moveIdSet = mutableSetOf<Int>()
            val pokemonMoveMap = mutableMapOf<Int, List<Int>>() // pokemonId -> list of moveIds

            emit(SyncProgress.InProgress(SyncPhase.POKEMON, 0, totalPokemon))

            for (pokemonId in 1..386) {
                emit(SyncProgress.InProgress(SyncPhase.POKEMON, pokemonId - 1, totalPokemon))
                if (pokemonId < startFromPokemon) {
                    // Already downloaded; but we still need to collect move IDs
                    // (We'll re-fetch move links from DB relationships)
                    continue
                }
                try {
                    val pokemonDto = apiService.getPokemon(pokemonId)
                    // Collect move IDs
                    val moveIds = pokemonDto.moves.mapNotNull { slot ->
                        extractMoveIdFromUrl(slot.move.url)
                    }
                    moveIdSet.addAll(moveIds)
                    pokemonMoveMap[pokemonId] = moveIds

                    // Download sprite
                    val spriteLocalPath = if (pokemonDto.sprites.frontDefault != null) {
                        val spritePath = "${getSpritesDirectory()}/$pokemonId.png"
                        if (!fileExists(spritePath)) {
                            try {
                                val bytes = apiService.downloadBytes(pokemonDto.sprites.frontDefault)
                                saveFile(spritePath, bytes)
                                spritePath
                            } catch (e: Exception) {
                                null
                            }
                        } else {
                            spritePath
                        }
                    } else null

                    val entity = pokemonDto.toEntity(spriteLocalPath)
                    pokemonDao.upsert(entity)

                    // Update sync status
                    syncStatusDao.upsert(SyncStatusEntity(
                        id = 1,
                        isCompleted = false,
                        lastSyncedPokemonId = pokemonId,
                        lastSyncedMoveId = syncStatus.lastSyncedMoveId
                    ))
                    delay(100)
                } catch (e: Exception) {
                    // Log and continue
                }
                emit(SyncProgress.InProgress(SyncPhase.POKEMON, pokemonId, totalPokemon))
            }

            // Phase 3: Download Moves
            emit(SyncProgress.InProgress(SyncPhase.MOVES, 0, moveIdSet.size))
            val validMoveIds = mutableSetOf<Int>()
            var moveCount = 0
            for (moveId in moveIdSet) {
                try {
                    val moveDto = apiService.getMove(moveId)
                    if (moveDto.isValidBattleMove()) {
                        moveDao.upsert(moveDto.toEntity())
                        validMoveIds.add(moveId)
                    }
                    delay(100)
                } catch (e: Exception) {
                    // Log and continue
                }
                moveCount++
                emit(SyncProgress.InProgress(SyncPhase.MOVES, moveCount, moveIdSet.size))
            }

            // Phase 4: Create PokemonMove relationships
            emit(SyncProgress.InProgress(SyncPhase.SPRITES, 0, pokemonMoveMap.size))
            var pmCount = 0
            for ((pokemonId, moveIds) in pokemonMoveMap) {
                val entities = moveIds
                    .filter { it in validMoveIds }
                    .map { moveId -> PokemonMoveEntity(pokemonId = pokemonId, moveId = moveId) }
                if (entities.isNotEmpty()) {
                    pokemonMoveDao.upsertAll(entities)
                }
                pmCount++
                emit(SyncProgress.InProgress(SyncPhase.SPRITES, pmCount, pokemonMoveMap.size))
            }

            // Phase 5: Seed trainers
            emit(SyncProgress.InProgress(SyncPhase.TRAINERS, 0, TrainerSeedData.trainers.size))
            if (trainerDao.count() == 0) {
                val trainerEntities = TrainerSeedData.trainers.map { seed ->
                    val teamMembers = seed.team.map { member ->
                        TrainerMemberJsonModel(
                            pokemonId = member.pokemonId,
                            level = member.level,
                            moveNames = member.moveNames
                        )
                    }
                    val teamJson = json.encodeToString(teamMembers)
                    TrainerEntity(
                        id = seed.id,
                        name = seed.name,
                        difficultyTier = seed.difficultyTier,
                        aiStrategy = seed.aiStrategy,
                        teamJson = teamJson
                    )
                }
                trainerDao.insertAll(trainerEntities)
            }
            emit(SyncProgress.InProgress(SyncPhase.TRAINERS, TrainerSeedData.trainers.size, TrainerSeedData.trainers.size))

            // Mark as completed
            syncStatusDao.upsert(SyncStatusEntity(id = 1, isCompleted = true, lastSyncedPokemonId = 386))
            emit(SyncProgress.Completed)
        } catch (e: Exception) {
            emit(SyncProgress.Error(e.message ?: "Unknown error during sync"))
        }
    }

    override suspend fun isSyncCompleted(): Boolean {
        return syncStatusDao.get()?.isCompleted == true
    }
}

@kotlinx.serialization.Serializable
data class TrainerMemberJsonModel(
    val pokemonId: Int,
    val level: Int,
    val moveNames: List<String>
)
