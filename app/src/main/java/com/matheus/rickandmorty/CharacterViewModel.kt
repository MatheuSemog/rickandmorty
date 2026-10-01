package com.matheus.rickandmorty

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = CharacterRepository(RickAndMortyApi.create(), AppDatabase.create(app).characterDao())

    val characters: StateFlow<List<CharacterEntity>> = repository.characters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    fun sync() {
        viewModelScope.launch {
            _syncState.value = SyncState.Loading
            _syncState.value = try {
                repository.syncCharacters()
                SyncState.Success
            } catch (e: Exception) {
                SyncState.Error("Sem conexão com o multiverso. Exibindo dados salvos.")
            }
        }
    }
}
