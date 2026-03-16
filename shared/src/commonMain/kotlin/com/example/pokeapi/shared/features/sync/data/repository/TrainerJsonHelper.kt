package com.example.pokeapi.shared.features.sync.data.repository

import kotlinx.serialization.json.Json

private val jsonParser = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

fun decodeTrainerTeamJson(teamJson: String): List<TrainerMemberJsonModel> {
    return jsonParser.decodeFromString(teamJson)
}
