package com.example.musicworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicworld.ui.theme.MusicWorldTheme

class PrimeraPantalla : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicWorldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MusicWorldScreen()
                }
            }
        }
    }
}

@Composable
fun MusicWorldScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF6A0DAD), Color.Black)
                )
            )
    ) {
        // Este bloque tendrá peso para ocupar el espacio disponible
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

        // Barra de navegación siempre en la parte inferior
        CustomBottomNavigationBar()
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = categoryName,
            modifier = Modifier.size(100.dp) // Hacemos la imagen más grande
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
            RecommendationCard("Acoustic vibes", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8lmiSdBv5b47lcdA3s6V275IveEUQZaB8tw&s")
            Spacer(modifier = Modifier.width(16.dp))
            RecommendationCard("Wanderlust", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEZRn0msB3bFNHtEZwwOfzSw67Nt0HdPMo0Q&s")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RecommendationCard("Country roads", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTc_Ei36dN99xfgFbNz8JGpic9BR5QSitcBaQ&s")
        }
    }
}

@Composable
fun RecommendationCard(title: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .background(Color.Transparent) // Fondo transparente
            .padding(8.dp),
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

@Composable
fun CustomBottomNavigationBar() {
    Row(
        modifier = Modifier
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
