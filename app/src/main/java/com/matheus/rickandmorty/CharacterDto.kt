package com.matheus.rickandmorty

data class CharacterResponse(val results: List<CharacterDto>)

data class CharacterDto(
    val id: Int,
    val name: String,
    val species: String,
    val status: String,
    val image: String
) {
    fun toEntity() = CharacterEntity(id, name, species, image)
}
