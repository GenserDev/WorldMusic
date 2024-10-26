package com.example.musicworld

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicRepository {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getSongsByGenre(genres: List<String>): List<Song> {
        val songs = mutableListOf<Song>()
        for (genre in genres) {
            val snapshot = firestore.collection("songs")
                .whereEqualTo("genre", genre)
                .get()
                .await()

            snapshot.documents.forEach { document ->
                val song = document.toObject(Song::class.java)
                if (song != null) {
                    songs.add(song)
                }
            }
        }
        return songs
    }
}
