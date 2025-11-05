package com.example.mobile_apps // Cambia esto por tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mobile_apps.ui.theme.MobileappsTheme // Asegúrate de que el nombre de tu tema coincida

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Tu tema de Material 3 que se crea por defecto
            MobileappsTheme {
                MainScreen()
            }
        }
    }
}

// --- Modelos de Datos ---

// 1. Data class para representar cada reporte
data class Reporte(
    val id: String,
    val titulo: String,
    val ubicacion: String,
    val imageUrl: String, // URL de la imagen
    val estado: EstadoReporte
)

// 2. Enum para manejar los estados de forma segura
enum class EstadoReporte(val texto: String, val icon: ImageVector, val color: Color) {
    EN_REVISION("En Revisión", Icons.Filled.DateRange, Color.Gray),
    EN_PROCESO("En Proceso", Icons.Filled.Build, Color(0xFFFFA726)), // Naranja
    SOLUCIONADO("Solucionado", Icons.Filled.CheckCircle, Color(0xFF66BB6A)) // Verde
}

// 3. Datos de ejemplo
val dummyReportes = listOf(
    Reporte("1", "Bache peligroso", "Av. Busch, 3er Anillo", "https://picsum.photos/id/101/200", EstadoReporte.EN_REVISION),
    Reporte("2", "Luminaria quemada", "Calle 21 de Mayo, Plaza Principal", "https://picsum.photos/id/102/200", EstadoReporte.EN_PROCESO),
    Reporte("3", "Fuga de agua", "Av. Grigotá, C/ Tte. Vega", "https://picsum.photos/id/103/200", EstadoReporte.SOLUCIONADO),
    Reporte("4", "Semáforo no funciona", "Av. Bánzer y 4to Anillo", "https://picsum.photos/id/104/200", EstadoReporte.EN_PROCESO),
)

// --- Pantalla Principal con Navegación ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // 1. Estado para saber qué pestaña está seleccionada.
    // `rememberSaveable` guarda el estado incluso si la app rota.
    var selectedTabIndex by rememberSaveable { mutableStateOf(1) } // 1 es "Mis Reportes"

    // 2. Scaffold es la plantilla de Material Design.
    // Nos da espacios para TopAppBar, BottomAppBar, etc.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(getScreenTitle(selectedTabIndex)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            // 3. NavigationBar (esto es el BottomAppBar que mencionaste)
            NavigationBar {
                val navItems = listOf(
                    BottomNavItem("Inicio", Icons.Filled.Home),
                    BottomNavItem("Mis Reportes", Icons.AutoMirrored.Filled.List),
                    BottomNavItem("Mi Perfil", Icons.Filled.Person)
                )

                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = (index == selectedTabIndex),
                        onClick = { selectedTabIndex = index },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // 4. Contenido principal de la pantalla
        // Aplicamos el padding que nos da el Scaffold
        Box(modifier = Modifier.padding(innerPadding)) {
            // 5. Mostramos el contenido según la pestaña seleccionada
            when (selectedTabIndex) {
                0 -> PlaceholderScreen("Pantalla de Inicio")
                1 -> ReporteListScreen(reportes = dummyReportes)
                2 -> PlaceholderScreen("Pantalla de Mi Perfil")
            }
        }
    }
}

// --- Componentes de la Lista ---

@Composable
fun ReporteListScreen(reportes: List<Reporte>) {
    // LazyColumn es el "RecyclerView" de Compose.
    // Es eficiente porque solo renderiza los ítems visibles.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre tarjetas
    ) {
        items(reportes) { reporte ->
            ReporteCard(reporte = reporte)
        }
    }
}

@Composable
fun ReporteCard(reporte: Reporte) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Imagen Circular (Izquierda)
            AsyncImage(
                model = reporte.imageUrl,
                contentDescription = "Foto del reporte",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Columna de Textos (Centro)
            Column(
                modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = reporte.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reporte.ubicacion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 3. Estado (Derecha)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = reporte.estado.icon,
                    contentDescription = reporte.estado.texto,
                    tint = reporte.estado.color,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reporte.estado.texto,
                    style = MaterialTheme.typography.labelSmall,
                    color = reporte.estado.color
                )
            }
        }
    }
}

// --- Helpers y Previews ---

// Helper para los ítems de navegación
data class BottomNavItem(val label: String, val icon: ImageVector)

// Helper para el título del TopAppBar
fun getScreenTitle(index: Int): String {
    return when (index) {
        0 -> "Inicio"
        1 -> "Mis Reportes"
        2 -> "Mi Perfil"
        else -> "Reportes Santa Cruz"
    }
}

// Placeholder para las otras pantallas
@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.headlineMedium)
    }
}

// Preview para ver el diseño sin correr la app
@Preview(showBackground = true)
@Composable
fun ReporteCardPreview() {
    MobileappsTheme {
        ReporteCard(reporte = dummyReportes[1])
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MobileappsTheme {
        MainScreen()
    }
}