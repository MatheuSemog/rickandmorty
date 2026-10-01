package com.matheus.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MultiverseScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiverseScreen(viewModel: CharacterViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsStateWithLifecycle()
    val state by viewModel.syncState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        val current = state
        if (current is SyncState.Error) snackbar.showSnackbar(current.message)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Multiverse Radar", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1B5E20),
                    titleContentColor = Color(0xFF97CE4C),
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = viewModel::sync, enabled = state !is SyncState.Loading) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sincronizar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(contentPadding = PaddingValues(12.dp)) {
                items(characters, key = { it.id }) { CharacterCard(it) }
            }
            if (state is SyncState.Loading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (characters.isEmpty()) {
                Text("Nenhum personagem salvo. Toque em sincronizar.", Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CharacterCard(character: CharacterEntity) {
    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(72.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(character.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(character.species, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
