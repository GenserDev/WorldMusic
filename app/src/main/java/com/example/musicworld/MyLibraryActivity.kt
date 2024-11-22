package com.example.musicworld

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun LibraryScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        val playlists = remember { mutableStateListOf("Favoritos", "My Playlist", "Top Hits") }
        val context = LocalContext.current

        // Estado para controlar la visibilidad del cuadro de diálogo
        var showDialog by remember { mutableStateOf(false) }
        // Estado para almacenar el nombre de la nueva playlist
        var newPlaylistName by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF6A0DAD), Color.Black)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Library",
                    fontSize = 24.sp,
                    color = Color.White
                )
                Row {
                    IconButton(onClick = { navController.navigate("confi_screen") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Playlist",
                            tint = Color.White
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(playlists.size) { index ->
                    val playlistName = playlists[index]

                    Button(
                        onClick = {
                            // Navegar a PlaylistActivity pasando el nombre de la playlist
                            val intent = Intent(context, PlaylistActivity::class.java)
                            intent.putExtra("PLAYLIST_NAME", playlistName)
                            intent.putExtra("GENRES", arrayOf("custom")) // Géneros personalizados
                            context.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A0DAD)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = playlistName, color = Color.White)
                    }
                }
            }
        }

        // Cuadro de diálogo para agregar nueva playlist
        if (showDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
                    .clickable { showDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "New Playlist",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        TextField(
                            value = newPlaylistName,
                            onValueChange = { newPlaylistName = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray.copy(alpha = 0.2f))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    if (newPlaylistName.text.isNotBlank()) {
                                        playlists.add(newPlaylistName.text)
                                        newPlaylistName = TextFieldValue("")
                                    }
                                    showDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A0DAD))
                            ) {
                                Text(text = "Add", color = Color.White)
                            }
                            Button(
                                onClick = { showDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                            ) {
                                Text(text = "Cancel", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
