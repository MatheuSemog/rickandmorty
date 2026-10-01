package com.matheus.rickandmorty

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY id")
    fun getAll(): Flow<List<CharacterEntity>>

    @Upsert
    suspend fun upsertAll(characters: List<CharacterEntity>)
}
