package at.htl.leonding.qrcodegenerator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val etContent = findViewById<EditText>(R.id.etContent)
        val btnGenerate = findViewById<Button>(R.id.btnGenerate)
        val ivQrCode = findViewById<ImageView>(R.id.ivQrCode)

        // BUTTON CLICK LISTENER
        btnGenerate.setOnClickListener {

            // 1. Text aus dem Feld holen und Leerzeichen entfernen (.trim())
            val text = etContent.text.toString().trim()

            if (text.isNotEmpty()) {
                // 2. QR-Code generieren über unser Hilfsobjekt (QRGenerator.kt)
                val bitmap = QRGenerator.generate(text)

                if (bitmap != null) {
                    // 3. Wenn erfolgreich: Das Bild im ImageView anzeigen
                    ivQrCode.setImageBitmap(bitmap)
                } else {
                    // Fehlermeldung als kleiner Banner (Toast)
                    Toast.makeText(this, "Fehler beim Erstellen", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Wenn das Feld leer ist, zeigen wir eine Fehlermeldung direkt am Feld
                etContent.error = "Bitte Text eingeben"
            }
        }
    }
}