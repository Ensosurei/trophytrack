package org.ensosurei.trophytrack.network

import kotlinx.serialization.Serializable

@Serializable
data class RawgGameDto(
    val id: Int,
    val name: String,
    val background_image: String?,
    val platforms: List<PlatformContainerDto>?
){
}

@Serializable
data class GameSearchResponse(
    val results : List<RawgGameDto> = emptyList()
)

@Serializable
data class PlatformContainerDto(
    val platform: RawgPlatformDto
)
@Serializable
data class RawgPlatformDto(
    val name: String
)

