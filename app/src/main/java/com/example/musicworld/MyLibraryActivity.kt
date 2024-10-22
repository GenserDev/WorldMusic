package com.example.musicworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicworld.ui.theme.MusicWorldTheme

class MyLibraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicWorldTheme {
                LibraryScreen()
            }
        }
    }
}

@Composable
fun LibraryScreen() {
    // Lista de playlists con nombres personalizados
    val playlists = remember { mutableStateListOf("Favoritos", "My Playlist", "My Playlist", "Top Hits") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF6A0DAD), Color.Black) // Morado a negro
                )
            )
            .padding(16.dp)
    ) {
        // Barra superior con el título y los iconos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Library",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                color = Color.White
            )

            Row {
                IconButton(onClick = { /* Acción para crear playlist */ }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Playlist",
                        tint = Color.White
                    )
                }

                IconButton(onClick = { /* Acción al hacer clic en ajustes */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de playlists
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(playlists.size) { index ->
                PlaylistButton(playlistName = playlists[index]) {
                    // Acción al hacer clic en la playlist
                    // Puedes agregar la lógica para redirigir a la pantalla correspondiente aquí
                }
            }
        }

        // Menú inferior con iconos
        BottomNavigationBar()
    }
}

@Composable
fun PlaylistButton(playlistName: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A0DAD)), // Color del botón
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = playlistName,
            fontSize = 18.sp, // Tamaño de la fuente
            color = Color.White // Cambiamos el color del texto a blanco
        )
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
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
fun LibraryScreenPreview() {
    MusicWorldTheme {
        LibraryScreen()
    }
}
