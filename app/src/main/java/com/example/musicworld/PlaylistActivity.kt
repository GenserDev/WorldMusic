package com.example.musicworld

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.compose.rememberNavController
import com.example.musicworld.ui.theme.MusicWorldTheme
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch

class PlaylistActivity : ComponentActivity() {
    private lateinit var player: ExoPlayer
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var currentSongIndex by mutableStateOf(0)
    private var isPlaying by mutableStateOf(false)

    private val playlist = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = ExoPlayer.Builder(this).build()

        val playlistName = intent.getStringExtra("PLAYLIST_NAME") ?: "Playlist"
        val genres: Array<String> = intent.getStringArrayExtra("GENRES") ?: arrayOf()

        setContent {
            MusicWorldTheme {
                PlaylistScreen(playlistName, genres)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    private fun playSong(fileName: String) {
        val storageReference = Firebase.storage.reference.child(fileName)

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            val songUrl = uri.toString()

            try {
                if (player.isPlaying) {
                    player.stop()
                    player.clearMediaItems()
                }
                val mediaItem = MediaItem.fromUri(songUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
                isPlaying = true
            } catch (e: Exception) {
                Log.e("PlaylistActivity", "Error al reproducir la canción: ${e.message}")
            }
        }.addOnFailureListener { exception ->
            Log.e("PlaylistActivity", "Error al obtener la URL de la canción: ${exception.message}")
        }
    }

    private fun pauseOrResumeSong() {
        if (isPlaying) {
            player.pause()
            isPlaying = false
        } else {
            player.play()
            isPlaying = true
        }
    }

    private fun nextSong() {
        currentSongIndex = (currentSongIndex + 1) % playlist.size
        playSong(playlist[currentSongIndex].fileName)
    }

    private fun previousSong() {
        currentSongIndex = if (currentSongIndex == 0) {
            playlist.size - 1
        } else {
            currentSongIndex - 1
        }
        playSong(playlist[currentSongIndex].fileName)
    }

    private fun loadSongs(genres: Array<String>, callback: (List<Song>) -> Unit) {
        val songsList = mutableListOf<Song>()

        firestore.collection("Tracks")
            .whereIn("genre", genres.toList())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    songsList.add(song)
                }
                playlist.clear()
                playlist.addAll(songsList)
                callback(songsList)
            }
            .addOnFailureListener { exception ->
                Log.e("PlaylistActivity", "Error al cargar canciones: ${exception.message}")
                callback(emptyList())
            }
    }

    private fun addSongToFavorites(song: Song, onSuccess: () -> Unit) {
        val favoritesRef = firestore.collection("Playlists").document("Favorites")

        favoritesRef.update("songs", FieldValue.arrayUnion(song))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                Log.e("PlaylistActivity", "Error al agregar canción a Favoritos: ${e.message}")
            }
    }

    @Composable
    fun PlaylistScreen(playlistName: String, genres: Array<String>) {
        var songs by remember { mutableStateOf(emptyList<Song>()) }
        var loading by remember { mutableStateOf(true) }
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            loadSongs(genres) { loadedSongs ->
                songs = loadedSongs
                loading = false
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = { BottomNavigationBar(navController = rememberNavController()) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF6A0DAD), Color.Black)
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.playlist_image1),
                        contentDescription = "Imagen de la playlist",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = playlistName,
                        fontSize = 24.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        if (songs.isEmpty()) {
                            Text(
                                text = "No hay canciones en esta playlist.",
                                color = Color.White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(songs) { song ->
                                    SongItem(
                                        songName = song.name,
                                        onClick = {
                                            currentSongIndex = songs.indexOf(song)
                                            playSong(song.fileName)
                                        },
                                        onFavoriteClick = {
                                            addSongToFavorites(song) {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        "Canción agregada a Favoritos"
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Barra de controles inferior
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { previousSong() }) {
                            Image(
                                painter = painterResource(id = R.drawable.atras),
                                contentDescription = "Anterior"
                            )
                        }

                        IconButton(onClick = { pauseOrResumeSong() }) {
                            Image(
                                painter = painterResource(id = if (isPlaying) R.drawable.pausa else R.drawable.reproductor),
                                contentDescription = if (isPlaying) "Pausar" else "Reproducir"
                            )
                        }

                        IconButton(onClick = { nextSong() }) {
                            Image(
                                painter = painterResource(id = R.drawable.siguiente),
                                contentDescription = "Siguiente"
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SongItem(songName: String, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color(0xFF2A2A2A), RoundedCornerShape(12.dp))
                .clickable { onClick() }
        ) {
            Text(
                text = songName,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            )
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Agregar a Favoritos",
                    tint = Color.Red
                )
            }
        }
    }

    data class Song(
        val genre: String = "",
        val title: String = "",
        val artist: String = "",
        val fileName: String = "",
        val name: String = ""
    )
}
