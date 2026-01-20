package at.htl.leonding.qrcodescanner

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var showScanner by remember { mutableStateOf(false) }   // State: bestimmt, ob der QR-Scanner angezeigt wird

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "QR-Code Scanner Demo", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { showScanner = true }) {
            Text("QR-Code scannen")
        }

        if (showScanner) {
            Dialog(
                onDismissRequest = { showScanner = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // QR-Code-Scanner (Kamera)
                        // Übergibt den gescannten Code als String
                        QRCodeScannerScreen { code ->
                            showScanner = false     // Scanner schließen
                            openUrl(context, code) // Gescannte URL öffnen
                        }

                        // Button zum Schließen
                        Button(
                            onClick = { showScanner = false },
                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp)
                        ) {
                            Text("Abbrechen")
                        }
                    }
                }
            }
        }
    }
}

// Funktion zum Öffnen einer URL im Browser
fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))  // Intent zum Öffnen einer URL
        context.startActivity(intent)                                              // Startet den Browser oder passende App
    } catch (e: Exception) {
        Toast.makeText(context, "URL fehlerhaft", Toast.LENGTH_SHORT).show()
    }
}