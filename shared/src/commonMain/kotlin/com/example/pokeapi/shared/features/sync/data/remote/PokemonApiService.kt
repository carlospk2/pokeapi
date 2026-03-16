package com.example.pokeapi.shared.features.sync.data.remote

import com.example.pokeapi.shared.features.sync.data.remote.dto.MoveDto
import com.example.pokeapi.shared.features.sync.data.remote.dto.PokemonDto
import com.example.pokeapi.shared.features.sync.data.remote.dto.TypeDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PokemonApiService(private val httpClient: HttpClient) {

    private val baseUrl = "https://pokeapi.co/api/v2"

    suspend fun getPokemon(id: Int): PokemonDto {
        return httpClient.get("$baseUrl/pokemon/$id").body()
    }

    suspend fun getMove(id: Int): MoveDto {
        return httpClient.get("$baseUrl/move/$id").body()
    }

    suspend fun getType(id: Int): TypeDto {
        return httpClient.get("$baseUrl/type/$id").body()
    }

    suspend fun downloadBytes(url: String): ByteArray {
        return httpClient.get(url).body()
    }
}
