package com.example.musicworld

// Código 1 (Modelo de Datos)
data class Song(val fileName: String, val genre: String)
data class Playlist_inicio(val name: String, val songs: List<Song>)


