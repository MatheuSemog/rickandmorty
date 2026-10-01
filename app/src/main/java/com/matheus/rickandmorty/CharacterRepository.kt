package com.matheus.rickandmorty

import kotlinx.coroutines.flow.Flow

class CharacterRepository(private val api: RickAndMortyApi, private val dao: CharacterDao) {
    val characters: Flow<List<CharacterEntity>> = dao.getAll()

    suspend fun syncCharacters() {
        dao.upsertAll(api.getCharacters().results.map { it.toEntity() })
    }
}
