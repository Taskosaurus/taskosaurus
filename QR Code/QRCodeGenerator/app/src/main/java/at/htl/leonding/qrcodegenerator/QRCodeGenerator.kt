package at.htl.leonding.qrcodegenerator

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.journeyapps.barcodescanner.BarcodeEncoder

object QRGenerator {
    /**
     * Wandelt Text in ein QR-Code Bild (Bitmap) um.
     */
    fun generate(text: String, size: Int = 512): Bitmap? {
        return try {
            // Konfiguration: Rand auf Minimum setzen (1)
            val hints = mapOf(EncodeHintType.MARGIN to 1)

            // ZXing-Tool initialisieren
            val encoder = BarcodeEncoder()

            // Text -> QR-Matrix -> Bitmap-Bild
            encoder.encodeBitmap(text, BarcodeFormat.QR_CODE, size, size, hints)

        } catch (e: Exception) {
            // Fehler abfangen (z.B. wenn der Text für einen QR-Code zu groß ist)
            e.printStackTrace()
            null
        }
    }
}