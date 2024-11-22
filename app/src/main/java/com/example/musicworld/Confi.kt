package com.example.musicworld

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Confi(navController: NavHostController) {
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
            Spacer(modifier = Modifier.height(16.dp))
            ProfileHeader()
            Spacer(modifier = Modifier.height(24.dp))

            // Campos de información del usuario con estilo mejorado
            EditableUserInfoField("Username", "Jose Lopez")
            EditableUserInfoField("Email", "lonnie.murphy@gmail.com")
            EditableUserInfoField("Phone", "(269)-748-9882")
            EditableUserInfoField("Gender", "Male")
        }
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile), // Imagen local en drawable
                    contentDescription = "User Profile Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
                IconButton(
                    onClick = { /* Acción para cambiar la foto */ },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6A0DAD))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change Photo",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lonnie Murphy",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun EditableUserInfoField(label: String, initialValue: String) {
    var value by remember { mutableStateOf(initialValue) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp) // Altura ajustada
                .background(Color.Black, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = value,
                onValueChange = { value = it },
                textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp) // Espaciado interno
            )
        }
    }
}

