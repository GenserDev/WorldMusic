package com.example.musicworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicworld.ui.theme.MusicWorldTheme

class MusicScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicWorldTheme {
                MusicScreen()
            }
        }
    }
}

@Composable
fun MusicScreen() {
    // Estados para manejar si la música está reproduciéndose y si es favorita
    var isPlaying by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }

    // Pantalla principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen de la carátula del álbum
        Image(
            painter = painterResource(id = R.drawable.album_cover),
            contentDescription = "Album Cover",
            modifier = Modifier
                .size(200.dp)
                .padding(top = 24.dp),
            contentScale = ContentScale.Crop
        )

        // Título de la canción
        Text(
            text = "Song Title",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Controles de reproducción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de anterior
            IconButton(onClick = {
                // Lógica para retroceder canción
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_previous),
                    contentDescription = "Previous",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            // Botón de reproducir/pausar
            IconButton(
                onClick = {
                    isPlaying = !isPlaying
                },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(64.dp)
                )
            }

            // Botón de siguiente
            IconButton(onClick = {
                // Lógica para avanzar canción
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "Next",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Botón de favoritos
        IconButton(
            onClick = {
                isFavorite = !isFavorite
            },
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Icon(
                painter = painterResource(
                    id = if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                ),
                contentDescription = "Favorite",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
