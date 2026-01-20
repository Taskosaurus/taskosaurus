package at.htl.leonding.qrcodescanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import at.htl.leonding.qrcodescanner.ui.theme.QRCodeScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCodeScannerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CheckCameraPermissionAndShowContent() // Prüft Kamera-Berechtigung und zeigt dann den Inhalt
                }
            }
        }
    }
}

@Composable
fun CheckCameraPermissionAndShowContent() {
    val context = androidx.compose.ui.platform.LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(     // Prüft, ob CAMERA Permission erlaubt ist
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        // Wird aufgerufen, wenn der User erlaubt oder ablehnt
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        // Falls keine Kamera-Berechtigung vorhanden ist
        if (!hasCameraPermission) {
            // Öffnet den Systemdialog zur Kamera-Erlaubnis
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
    // wenn erlaubt, wird der eigentliche QR-Code-Scanner angezeigt
    if (hasCameraPermission) {
        MainScreen()
    }
}