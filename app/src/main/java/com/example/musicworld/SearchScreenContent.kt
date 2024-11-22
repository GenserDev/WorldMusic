package com.example.musicworld

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun SearchScreenContent(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        val firestore = Firebase.firestore
        var searchText by remember { mutableStateOf("") }
        var searchResults by remember { mutableStateOf(emptyList<Song>()) }
        var loading by remember { mutableStateOf(false) }

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
            // Campo de búsqueda
            SearchBar(
                searchText = searchText,
                onSearchTextChange = { newText ->
                    searchText = newText
                    if (searchText.isNotEmpty()) {
                        loading = true
                        searchInFirebase(firestore, searchText) { results ->
                            searchResults = results
                            loading = false
                        }
                    } else {
                        searchResults = emptyList()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Indicador de carga o resultados
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                if (searchResults.isEmpty()) {
                    Text(
                        text = "No se encontraron resultados.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Resultados de búsqueda",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mostrar resultados
                    searchResults.forEach { song ->
                        SearchItemRow(item = "${song.name} - ${song.artist} (${song.genre})")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF2D2D2D), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = TextStyle(fontSize = 14.sp, color = Color.White),
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    Text(
                        text = "Buscar por artista, canción o género",
                        color = Color(0xFF9E9E9E),
                        fontSize = 14.sp
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun SearchItemRow(item: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFF1C1C1E))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

// Función para buscar en Firebase
fun searchInFirebase(
    firestore: FirebaseFirestore,
    query: String,
    callback: (List<Song>) -> Unit
) {
    firestore.collection("Tracks")
        .get()
        .addOnSuccessListener { documents ->
            val results = documents.mapNotNull { document ->
                val song = document.toObject(Song::class.java)
                if (song.artist.contains(query, ignoreCase = true) ||
                    song.name.contains(query, ignoreCase = true) ||
                    song.genre.contains(query, ignoreCase = true)
                ) {
                    song
                } else null
            }
            callback(results)
        }
        .addOnFailureListener {
            callback(emptyList())
        }
}

// Clase Song
data class Song(
    val name: String = "",
    val artist: String = "",
    val genre: String = "",
    val fileName: String = "",
    val albumArt: String = ""
)
