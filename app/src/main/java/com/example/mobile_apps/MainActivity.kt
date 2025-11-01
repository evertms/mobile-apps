package com.example.mobile_apps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobile_apps.ui.theme.MobileappsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileappsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactProfileScreen()
                }
            }
        }
    }
}

// Usamos @OptIn para la TopAppBar, que es experimental
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactProfileScreen() {

    // --- REQUISITO: 3 COMPONENTES MATERIAL ---
    // REQUISITO: USAR UN LAYOUT QUE NO USAMOS
    // Componente layout no usado
    // Nos da la estructura base (barra superior, contenido)
    Scaffold(
        topBar = {
            // Componente MD #1: TopAppBar
            TopAppBar(
                title = { Text("Perfil del Contacto") }, // Título de la barra
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Color de fondo
                    titleContentColor = Color.White, // Color del título
                    actionIconContentColor = Color.White, // Color de los iconos
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    // Componente MD #2: IconButton (para navegación)
                    IconButton(onClick = { /* Acción al presionar 'atrás' */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    // Otros IconButton
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Call, contentDescription = "Llamar")
                    }

                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { paddingValues ->

        // --- REQUISITO: LISTA Y SCROLL ---
        // Componente: LazyColumn
        // Ocupa el espacio de contenido que nos da el Scaffold
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Importante: aplicar el padding del Scaffold
            horizontalAlignment = Alignment.CenterHorizontally // Centra todo el contenido
        ) {

            // --- REQUISITO: RECURSOS ---
            // Este es el primer "item" de nuestra lista
            item {
                ProfileHeader()
            }

            // Estos son los otros "items" de la lista
            // Usamos un componente Material 'ListItem' para cada fila

            item {
                // Componente MD #3: ListItem
                ListItem(
                    headlineContent = { Text("Estado") },
                    supportingContent = { Text("Expandiendo mi Dominio") },
                    leadingContent = {
                        Icon(Icons.Default.Notifications, contentDescription = "Estado")
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Móvil") },
                    supportingContent = { Text("+1 (555) 123-456") },
                    leadingContent = {
                        Icon(Icons.Default.Phone, contentDescription = "Móvil")
                    }
                )
            }

            // Añadimos items extra para forzar el SCROLL
            items(10) { index ->
                ListItem(
                    headlineContent = { Text("Otro campo $index") },
                    supportingContent = { Text("Información de relleno...") }
                )
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 32.dp)
    ) {
        // --- REQUISITO RECURSO #1: Imagen (Drawable) ---
        Image(
            painter = painterResource(id = R.drawable.profile_pic),
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Crop, // Para que la imagen llene el círculo
            modifier = Modifier
                // --- REQUISITO RECURSO #2: Dimensión (Dimen) ---
                .size(dimensionResource(id = R.dimen.profile_pic_size))
                .clip(CircleShape) // Recorta la imagen en forma de círculo
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- REQUISITO RECURSO #3: Texto (String) ---
        Text(
            text = stringResource(id = R.string.contact_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "En línea",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ContactProfilePreview() {
    MobileappsTheme {
        ContactProfileScreen()
    }
}