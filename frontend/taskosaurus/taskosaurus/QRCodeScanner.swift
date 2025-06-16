import SwiftUI
import CoreImage
import PhotosUI

struct QRCodeScanner: View {
    @State private var selectedImage: UIImage?
    @State private var qrCodeText: String = "QR-Code wird hier angezeigt"
    @State private var isImagePickerPresented = false
    @Environment(\.openURL) var openURL

    var body: some View {
        VStack {
            if let image = selectedImage {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 250)
                    .padding()
            }

            Button("Bild auswählen & QR-Code scannen") {
                isImagePickerPresented = true
            }
            .padding()
            .sheet(isPresented: $isImagePickerPresented) {
                PhotoPicker(image: $selectedImage)
            }

            Text(qrCodeText)
                .padding()
                .font(.headline)
                .multilineTextAlignment(.center)

            if selectedImage != nil {
                Button("QR-Code analysieren & öffnen") {
                    if let image = selectedImage {
                        scanQRCode(from: image)
                    }
                }
                .padding()
            }
        }
        .padding()
    }

    func scanQRCode(from image: UIImage) {
        guard let ciImage = CIImage(image: image) else {
            qrCodeText = "Bild konnte nicht verarbeitet werden"
            return
        }

        let detector = CIDetector(ofType: CIDetectorTypeQRCode,
                                  context: nil,
                                  options: [CIDetectorAccuracy: CIDetectorAccuracyHigh])

        let features = detector?.features(in: ciImage) as? [CIQRCodeFeature]

        if let qrCodeString = features?.first?.messageString {
            // Ergänze ggf. https://
            let urlString = qrCodeString.hasPrefix("http") ? qrCodeString : "https://\(qrCodeString)"
            qrCodeText = "Erkannt: \(urlString)"
            print("Gescannt: \(urlString)")

            if let url = URL(string: urlString) {
                DispatchQueue.main.async {
                    openURL(url)
                }
            } else {
                qrCodeText = "Kein gültiger Link"
            }
        } else {
            qrCodeText = "Kein QR-Code gefunden"
        }
    }
}

// MARK: - PHPicker Wrapper für SwiftUI
struct PhotoPicker: UIViewControllerRepresentable {
    @Binding var image: UIImage?

    func makeUIViewController(context: Context) -> PHPickerViewController {
        var config = PHPickerConfiguration()
        config.filter = .images
        config.selectionLimit = 1

        let picker = PHPickerViewController(configuration: config)
        picker.delegate = context.coordinator
        return picker
    }

    func updateUIViewController(_ uiViewController: PHPickerViewController, context: Context) {}

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject, PHPickerViewControllerDelegate {
        let parent: PhotoPicker

        init(_ parent: PhotoPicker) {
            self.parent = parent
        }

        func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
            picker.dismiss(animated: true)

            guard let provider = results.first?.itemProvider,
                  provider.canLoadObject(ofClass: UIImage.self) else { return }

            provider.loadObject(ofClass: UIImage.self) { object, error in
                if let image = object as? UIImage {
                    DispatchQueue.main.async {
                        self.parent.image = image
                    }
                }
            }
        }
    }
}
