package com.example.musicworld

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage

@Composable
fun PrimeraPantalla(navController: NavHostController) {
    Column(
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
                .weight(1f)
                .padding(16.dp)
        ) {
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
            Spacer(modifier = Modifier.height(16.dp))
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

        // Barra de navegación inferior
        BottomNavigationBar(navController = navController)
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

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.take(3).forEach { category ->
                CategoryButton(category.first, category.second)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            categories.drop(3).forEach { category ->
                CategoryButton(category.first, category.second)
            }
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
        else -> emptyArray() // Devuelve un array vacío si no hay coincidencia
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, PlaylistActivity::class.java)
                intent.putExtra("PLAYLIST_NAME", categoryName)
                intent.putExtra("GENRES", genres) // Enviar como array de géneros
                context.startActivity(intent)
            }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = categoryName,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = categoryName,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
            RecommendationCard("Country roads", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTc_Ei36dN99xfgFbNz8JGpic9BR5QSitcBaQ&s", "country")
        }
    }
}

@Composable
fun RecommendationCard(title: String, imageUrl: String, genre: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(140.dp)
            .background(Color.Transparent)
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, PlaylistActivity::class.java)
                intent.putExtra("PLAYLIST_NAME", title)
                intent.putExtra("GENRE", genre)  // Pasar el género como un String
                context.startActivity(intent)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = "Artist names",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}


