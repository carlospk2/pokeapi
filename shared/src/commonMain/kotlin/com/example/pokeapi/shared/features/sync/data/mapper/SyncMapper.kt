package com.example.pokeapi.shared.features.sync.data.mapper

import com.example.pokeapi.shared.database.entity.MoveEntity
import com.example.pokeapi.shared.database.entity.PokemonEntity
import com.example.pokeapi.shared.database.entity.TypeEffectivenessEntity
import com.example.pokeapi.shared.features.sync.data.remote.dto.MoveDto
import com.example.pokeapi.shared.features.sync.data.remote.dto.PokemonDto
import com.example.pokeapi.shared.features.sync.data.remote.dto.TypeDto

fun PokemonDto.toEntity(spriteLocalPath: String? = null): PokemonEntity {
    val typesSorted = types.sortedBy { it.slot }
    val typePrimary = typesSorted.firstOrNull()?.type?.name ?: "normal"
    val typeSecondary = typesSorted.getOrNull(1)?.type?.name

    val statsMap = stats.associate { it.stat.name to it.baseStat }

    val generation = when {
        id <= 151 -> 1
        id <= 251 -> 2
        else -> 3
    }

    return PokemonEntity(
        id = id,
        name = name,
        spriteUrl = sprites.frontDefault ?: "",
        spriteLocalPath = spriteLocalPath,
        typePrimary = typePrimary,
        typeSecondary = typeSecondary,
        baseHp = statsMap["hp"] ?: 0,
        baseAttack = statsMap["attack"] ?: 0,
        baseDefense = statsMap["defense"] ?: 0,
        baseSpAttack = statsMap["special-attack"] ?: 0,
        baseSpDefense = statsMap["special-defense"] ?: 0,
        baseSpeed = statsMap["speed"] ?: 0,
        generation = generation
    )
}

fun MoveDto.toEntity(): MoveEntity = MoveEntity(
    id = id,
    name = name,
    type = type.name,
    category = damageClass.name,
    power = power,
    accuracy = accuracy,
    pp = pp,
    priority = priority
)

fun TypeDto.toTypeEffectivenessEntities(): List<TypeEffectivenessEntity> {
    val result = mutableListOf<TypeEffectivenessEntity>()

    damageRelations.doubleDamageTo.forEach { defending ->
        result.add(TypeEffectivenessEntity(
            attackingType = name,
            defendingType = defending.name,
            multiplier = 2.0f
        ))
    }
    damageRelations.halfDamageTo.forEach { defending ->
        result.add(TypeEffectivenessEntity(
            attackingType = name,
            defendingType = defending.name,
            multiplier = 0.5f
        ))
    }
    damageRelations.noDamageTo.forEach { defending ->
        result.add(TypeEffectivenessEntity(
            attackingType = name,
            defendingType = defending.name,
            multiplier = 0.0f
        ))
    }

    return result
}

fun MoveDto.isValidBattleMove(): Boolean {
    return power != null && power > 0 &&
            (damageClass.name == "physical" || damageClass.name == "special")
}

fun extractMoveIdFromUrl(url: String): Int? {
    return url.trimEnd('/').substringAfterLast('/').toIntOrNull()
}
