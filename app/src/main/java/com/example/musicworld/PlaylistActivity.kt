package com.example.musicworld

import androidx.compose.foundation.clickable
import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicworld.ui.theme.MusicWorldTheme

class PlaylistActivity : ComponentActivity() {
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = ExoPlayer.Builder(this).build() // Inicializa el reproductor

        setContent {
            MusicWorldTheme {
                PlaylistScreen(onSongClick = { songFileName ->
                    playSong(songFileName) // Llama a playSong cuando se hace clic en una canción
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release() // Libera los recursos del reproductor al destruir la actividad
    }

    private fun playSong(fileName: String) {
        // Construir la URL del MP3
        val songUrl = "https://firebasestorage.googleapis.com/v0/b/worldmusic-82323.appspot.com/o/$fileName?alt=media&token=4a4e4191-c08a-4d80-aaf5-252a5b856847"
        val mediaItem = MediaItem.fromUri(songUrl)

        // Prepara y reproduce la canción
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    @Composable
    fun PlaylistScreen(onSongClick: (String) -> Unit) {
        val playlistName = "Favoritos"
        val playlistImage = painterResource(id = R.drawable.playlist_image)
        val songs = listOf("anime_Dandadan.mp3") // Cambia esta lista según los nombres de tus archivos en Firebase

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF6A0DAD), Color.Black) // Degradado de morado a negro
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Imagen de la playlist
                Image(
                    painter = playlistImage,
                    contentDescription = "Imagen de la playlist",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 16.dp),
                    contentScale = ContentScale.Crop
                )

                // Título de la playlist
                Text(
                    text = playlistName,
                    fontSize = 24.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Lista de canciones
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(songs) { songFileName ->
                        SongItem(songName = songFileName, onClick = onSongClick)
                    }
                }

                // Espaciador que empuja el menú hacia abajo
                Spacer(modifier = Modifier.weight(1f))
            }

            // Menú inferior de navegación
            BottomNavigationBar(
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    @Composable
    fun SongItem(songName: String, onClick: (String) -> Unit) {
        // Tarjeta para cada canción
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable {
                    onClick(songName) // Llama a onClick pasando el nombre del archivo
                },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)) // Color de fondo de la tarjeta
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
                .background(Color.Black) // Fondo negro
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

    @Preview(showBackground = true)
    @Composable
    fun PlaylistScreenPreview() {
        MusicWorldTheme {
            PlaylistScreen(onSongClick = {})
        }
    }
}
