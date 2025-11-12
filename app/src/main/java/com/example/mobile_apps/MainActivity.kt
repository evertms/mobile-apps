package com.example.mobile_apps

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_apps.ui.theme.MobileappsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileappsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PermissionRequestScreen()
                }
            }
        }
    }
}

// Usamos @OptIn porque la API de Accompanist Permissions es experimental
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestScreen() {

    // --- 1. ESTADO PARA UN SOLO PERMISO (CÁMARA) ---
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    // --- 2. ESTADO PARA MÚLTIPLES PERMISOS (UBICACIÓN + GALERÍA) ---
    // Creamos la lista de permisos que necesitamos
    val permissionsToRequest = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // El permiso de Galería cambió en API 33 (Android 13)
    // Así que pedimos el permiso correcto según la versión del SO
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = permissionsToRequest
    )

    // --- 3. LA VISTA (UI) ---
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // --- Botón 1: Cámara ---
        Button(onClick = {
            // Lanza el diálogo para pedir el permiso
            cameraPermissionState.launchPermissionRequest()
        }) {
            Text("Pedir Permiso de Cámara")
        }

        // Muestra el estado actual del permiso
        Text(
            if (cameraPermissionState.status.isGranted)
                "Cámara: ¡Permiso Concedido!"
            else
                "Cámara: Permiso Denegado."
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Botón 2: Ubicación + Galería ---
        Button(onClick = {
            // Lanza el diálogo para pedir MÚLTIPLES permisos
            multiplePermissionsState.launchMultiplePermissionRequest()
        }) {
            Text("Pedir Ubicación + Galería")
        }

        // Muestra el estado de todos los permisos
        Text(
            if (multiplePermissionsState.allPermissionsGranted)
                "Ubicación y Galería: ¡Todos Concedidos!"
            else
                "Ubicación y Galería: Uno o más permisos denegados."
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview() {
    MobileappsTheme {
        PermissionRequestScreen()
    }
}