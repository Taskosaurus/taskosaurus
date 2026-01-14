package at.htl.leonding.qrcodescanner

import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
@Composable
fun QRCodeScannerScreen(
    // wird aufgerufen, sobald ein QR-Code erkannt wurde
    onCodeScanned: (String) -> Unit
) {

    val context = LocalContext.current

    // Lifecycle (wichtig für Kamera starten/stoppen)
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    // AndroidView erlaubt klassische Android Views in Compose
    AndroidView(
        factory = { ctx ->

            // View Kamera-Vorschau
            val previewView = PreviewView(ctx)

            val executor = ContextCompat.getMainExecutor(ctx)

            cameraProviderFuture.addListener({

                // Kamera-Provider holen
                val cameraProvider = cameraProviderFuture.get()

                // Kamera-Vorschau konfigurieren
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val scanner = BarcodeScanning.getClient()

                // ImageAnalysis analysiert jedes Kamerabild
                val analysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(
                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                    )
                    .build()

                analysis.setAnalyzer(executor) { imageProxy ->
                    val mediaImage = imageProxy.image

                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        // QR-Code-Erkennung starten
                        scanner.process(image)
                            .addOnSuccessListener { barcodes ->

                                // Erstes erkanntes Barcode-Ergebnis
                                barcodes.firstOrNull()?.rawValue?.let { code ->
                                    // Callback an MainScreen
                                    onCodeScanned(code)
                                }
                            }
                            .addOnCompleteListener {
                                imageProxy.close()
                            }

                    } else {
                        // Falls kein Bild vorhanden ist
                        imageProxy.close()
                    }
                }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA, // Rückkamera
                        preview,
                        analysis
                    )

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}