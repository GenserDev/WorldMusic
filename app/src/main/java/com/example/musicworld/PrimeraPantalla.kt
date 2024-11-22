package com.example.musicworld

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun PrimeraPantalla(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
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
            // Sección "Recientemente"
            Text(
                text = "Recientemente",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            CategoryButtons()
            Spacer(modifier = Modifier.height(24.dp))

            // Sección "More like Taylor Swift"
            Text(
                text = "More like Taylor Swift",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
            RecommendationsSection()
        }
    }
}

@Composable
fun CategoryButtons() {
    val categories = listOf(
        Pair("Pop punk 2000", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgAxqnpKwTagzdGiK-eWbaWpZQ7Li-zFmw-Q&s"),
        Pair("Electro sounds", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfUfmNlWKZONrBqz7GbBx8tl6Ykj2ccpED7A&s"),
        Pair("Roadtrip to LA", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSn3eSC5XUNkVpzYcyRSU2tY1eZ29DTFG6tg&s"),
        Pair("Fancy brunch", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBbtsgvgjiyuupjclMlc6OJkeZgB-arjr_Hw&s"),
        Pair("Gym Insanity", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8H7jpo22pawpGmimB9t2UIEF_fsY48zyj2g&s"),
        Pair("120bpm run", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZHiAPZys-fgn5vEmul4D9lJP58HlEVhJz1w&s")
    )

    // Diseño en cuadrícula para mantener tamaño uniforme
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryButton(category.first, category.second)
        }
    }
}

@Composable
fun CategoryButton(categoryName: String, imageUrl: String) {
    val context = LocalContext.current
    val genres = when (categoryName) {
        "Gym Insanity" -> arrayOf("rap")
        "Roadtrip to LA" -> arrayOf("rap", "trap")
        "Pop punk 2000" -> arrayOf("pop punk")
        "Electro sounds" -> arrayOf("electro")
        "Fancy brunch" -> arrayOf("pop")
        "120bpm run" -> arrayOf("electronic")
        else -> emptyArray()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, PlaylistActivity::class.java)
                intent.putExtra("PLAYLIST_NAME", categoryName)
                intent.putExtra("GENRES", genres)
                context.startActivity(intent)
            }
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = categoryName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = categoryName,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RecommendationsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RecommendationCard("Acoustic vibes", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8lmiSdBv5b47lcdA3s6V275IveEUQZaB8tw&s", "lofi")
            Spacer(modifier = Modifier.width(16.dp))
            RecommendationCard("Wanderlust", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEZRn0msB3bFNHtEZwwOfzSw67Nt0HdPMo0Q&s", "pop")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
        }
    }
}

@Composable
fun RecommendationCard(title: String, imageUrl: String, genre: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(140.dp)
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, PlaylistActivity::class.java)
                intent.putExtra("PLAYLIST_NAME", title)
                intent.putExtra("GENRE", genre)
                context.startActivity(intent)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(Color.Gray)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color.White
        )
        Text(
            text = "Artist names",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}



