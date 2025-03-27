import SwiftUI
import CoreImage
import PhotosUI

struct QRCodeScanner: View {
    @State private var selectedImage: UIImage?
    @State private var qrCodeText: String = "QR-Code wird hier angezeigt"
    @State private var isImagePickerPresented = false

    var body: some View {
        VStack {
            if let image = selectedImage {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 250)
                    .padding()
            }

            Button("QR-Code aus Bild scannen") {
                isImagePickerPresented = true
            }
            .padding()
            .sheet(isPresented: $isImagePickerPresented) {
                ImagePicker(selectedImage: $selectedImage)
            }

            Text(qrCodeText)
                .padding()
                .font(.headline)

            Button("QR-Code analysieren & öffnen") {
                if let image = selectedImage {
                    scanQRCode(from: image)
                }
            }
            .padding()
        }
    }

    func scanQRCode(from image: UIImage) {
        guard let ciImage = CIImage(image: image) else { return }

        let detector = CIDetector(ofType: CIDetectorTypeQRCode, context: nil, options: [CIDetectorAccuracy: CIDetectorAccuracyHigh])
        let features = detector?.features(in: ciImage) as? [CIQRCodeFeature]

        if let qrCodeString = features?.first?.messageString {
            qrCodeText = "Erkannt: \(qrCodeString)"

            // Debugging: Ausgabe in der Konsole
            print("Gescannt: \(qrCodeString)")

            // Versuche, die URL zu öffnen
            if let url = URL(string: qrCodeString) {
                DispatchQueue.main.async {
                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
                }
            } else {
                qrCodeText = "Kein gültiger Link"
            }
        } else {
            qrCodeText = "Kein QR-Code gefunden"
        }
    }
}

