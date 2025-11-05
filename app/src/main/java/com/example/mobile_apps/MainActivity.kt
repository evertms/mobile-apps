package com.example.mobile_apps

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter // <-- Importante: de la biblioteca Coil
import com.example.mobile_apps.ui.theme.MobileappsTheme // Asegúrate que coincida con tu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileappsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlowerPickerScreen()
                }
            }
        }
    }
}

@Composable
fun FlowerPickerScreen() {
    // --- 1. El Estado ---
    // Aquí guardaremos la URI (la "dirección") de la imagen que el usuario seleccione.
    // Usamos 'remember' para que el estado sobreviva a las recomposiciones.
    // Inicialmente es 'null' porque no se ha seleccionado nada.
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // --- 2. El Lanzador (Launcher) ---
    // Esta es la parte clave. Preparamos un "lanzador" que sabe cómo...
    // Contrato: ...pedir un archivo de 'PickVisualMedia' (el selector de fotos moderno).
    // Resultado: ...y qué hacer cuando tengamos el resultado ('onResult').
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            // Cuando el usuario elige una foto, la 'uri' llega aquí.
            // Actualizamos nuestro estado con la nueva URI.
            imageUri = uri
        }
    )

    // --- 3. La UI (La Interfaz) ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // El botón que inicia todo el proceso
        Button(
            onClick = {
                // --- 4. La Acción ---
                // Al hacer clic, le decimos al lanzador que se ejecute.
                // Le especificamos que SOLO queremos imágenes ('ImageOnly').
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text("Seleccionar Flor para Reconocimiento")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 5. El Resultado (La Vista Previa) ---
        // Si 'imageUri' NO es nula (es decir, el usuario ya eligió algo)...
        if (imageUri != null) {
            Text("Flor seleccionada:")
            Image(
                // Aquí usamos 'rememberAsyncImagePainter' de la biblioteca Coil
                // para cargar la imagen desde la URI que nos dio el selector.
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = "Imagen de la flor seleccionada",
                modifier = Modifier
                    .size(250.dp) // Le damos un tamaño fijo
                    .padding(top = 16.dp),
                contentScale = ContentScale.Crop // Recorta la imagen para que llene el espacio
            )
        } else {
            // Si 'imageUri' ES nula, mostramos este texto
            Text("Aún no has seleccionado ninguna flor.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlowerPickerPreview() {
    MobileappsTheme {
        FlowerPickerScreen()
    }
}