package org.ensosurei.trophytrack.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.ensosurei.trophytrack.BuildKonfig

class RawgApiClient {
    val client = HttpClient(){
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                coerceInputValues = true
                isLenient = true
            })
        }

        defaultRequest {
            url("https://api.rawg.io/api/")
            url.parameters.append("key", BuildKonfig.RAWG_API_KEY)
        }
    }

    suspend fun searchGames(query: String): GameSearchResponse{
        val response = client.get("games"){
            parameter("search",query)
        }
        return response.body<GameSearchResponse>()
    }
}
