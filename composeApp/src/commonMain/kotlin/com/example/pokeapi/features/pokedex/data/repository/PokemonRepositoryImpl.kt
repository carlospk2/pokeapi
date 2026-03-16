package com.example.pokeapi.features.pokedex.data.repository

import com.example.pokeapi.features.pokedex.domain.model.Pokemon
import com.example.pokeapi.features.pokedex.domain.model.PokemonDetail
import com.example.pokeapi.features.pokedex.domain.model.PokemonMove
import com.example.pokeapi.features.pokedex.domain.repository.PokemonRepository
import com.example.pokeapi.shared.database.dao.MoveDao
import com.example.pokeapi.shared.database.dao.PokemonDao
import com.example.pokeapi.shared.database.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PokemonRepositoryImpl(
    private val pokemonDao: PokemonDao,
    private val moveDao: MoveDao
) : PokemonRepository {

    override fun getAll(): Flow<List<Pokemon>> =
        pokemonDao.getAll().map { list -> list.map { it.toDomain() } }

    override fun searchByName(query: String): Flow<List<Pokemon>> =
        pokemonDao.searchByName(query).map { list -> list.map { it.toDomain() } }

    override fun getByType(type: String): Flow<List<Pokemon>> =
        pokemonDao.getByType(type).map { list -> list.map { it.toDomain() } }

    override fun getByGeneration(generation: Int): Flow<List<Pokemon>> =
        pokemonDao.getByGeneration(generation).map { list -> list.map { it.toDomain() } }

    override suspend fun getDetail(id: Int): PokemonDetail? {
        val entity = pokemonDao.getById(id) ?: return null
        val moves = moveDao.getMovesByPokemon(id).first().map { move ->
            PokemonMove(
                id = move.id,
                name = move.name,
                type = move.type,
                category = move.category,
                power = move.power,
                accuracy = move.accuracy,
                pp = move.pp
            )
        }
        return entity.toDetailDomain(moves)
    }

    private fun PokemonEntity.toDomain() = Pokemon(
        id = id,
        name = name,
        spriteUrl = spriteUrl,
        spriteLocalPath = spriteLocalPath,
        typePrimary = typePrimary,
        typeSecondary = typeSecondary,
        generation = generation
    )

    private fun PokemonEntity.toDetailDomain(moves: List<PokemonMove>) = PokemonDetail(
        id = id,
        name = name,
        spriteUrl = spriteUrl,
        spriteLocalPath = spriteLocalPath,
        typePrimary = typePrimary,
        typeSecondary = typeSecondary,
        baseHp = baseHp,
        baseAttack = baseAttack,
        baseDefense = baseDefense,
        baseSpAttack = baseSpAttack,
        baseSpDefense = baseSpDefense,
        baseSpeed = baseSpeed,
        generation = generation,
        moves = moves
    )
}
