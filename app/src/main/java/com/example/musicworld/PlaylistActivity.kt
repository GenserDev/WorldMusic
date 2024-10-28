package com.example.musicworld

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.example.musicworld.ui.theme.MusicWorldTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PlaylistActivity : ComponentActivity() {
    private lateinit var player: ExoPlayer
    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = ExoPlayer.Builder(this).build()

        val playlistName = intent.getStringExtra("PLAYLIST_NAME") ?: "Favoritos"
        val genres: Array<String> = intent.getStringArrayExtra("GENRES") ?: arrayOf(" ")

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
        val songUrl = "https://firebasestorage.googleapis.com/v0/b/worldmusic-82323.appspot.com/o/$fileName?alt=media&token=4a4e4191-c08a-4d80-aaf5-252a5b856847"
        val mediaItem = MediaItem.fromUri(songUrl)

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    @Composable
    fun PlaylistScreen(playlistName: String, genres: Array<String>) {
        var songs by remember { mutableStateOf(emptyList<Song>()) }
        var loading by remember { mutableStateOf(true) }

        // Cargar las canciones de Firestore
        LaunchedEffect(Unit) {
            loadSongs(genres) { loadedSongs ->
                songs = loadedSongs
                loading = false
            }
        }

        val playlistImage = painterResource(id = R.drawable.playlist_image1)

        Box(
            modifier = Modifier
                .fillMaxSize()
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
                    painter = playlistImage,
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
                    // Mostrar un indicador de carga
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
                                SongItem(songName = song.name) { playSong(song.fileName) }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            BottomNavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    private fun loadSongs(genres: Array<String>, callback: (List<Song>) -> Unit) {
        val songsList = mutableListOf<Song>()

        firestore.collection("Tracks")
            .whereIn("genre", genres.toList())
            .get()
            .addOnSuccessListener { documents ->
                Log.d("PlaylistActivity", "Número de documentos: ${documents.size()}")
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    songsList.add(song)
                    Log.d("PlaylistActivity", "Canción cargada: ${song.name} de ${song.artist}, Género: ${song.genre}")
                }
                callback(songsList)
            }
            .addOnFailureListener { exception ->
                Log.e("PlaylistActivity", "Error al cargar canciones: ", exception)
                callback(emptyList())
            }
    }

    @Composable
    fun SongItem(songName: String, onClick: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
        ) {
            Text(
                text = songName,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                textAlign = TextAlign.Start
            )
        }
    }

    @Composable
    fun BottomNavigationBar(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(vertical = 16.dp, horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Acción al ir al menú principal */ }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            }

            IconButton(onClick = { /* Acción al ir al buscador */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }

            IconButton(onClick = { /* Acción al ir al perfil de usuario */ }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White
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
