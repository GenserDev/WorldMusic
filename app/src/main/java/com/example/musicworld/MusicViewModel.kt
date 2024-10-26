package com.example.musicworld

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
    private val repository = MusicRepository()

    val playlists = mutableListOf<Playlist_inicio>()

    fun loadPlaylists() {
        viewModelScope.launch {
            playlists.add(
                Playlist_inicio(
                    name = "Roadtrip to LA",  // Cambia aquí de title a name
                    songs = repository.getSongsByGenre(listOf("trap", "rap"))
                )
            )
            playlists.add(
                Playlist_inicio(
                    name = "Gym Insanity",  // Cambia aquí también
                    songs = repository.getSongsByGenre(listOf("rap"))
                )
            )
            // Agrega las demás playlists aquí...
        }
    }
}

