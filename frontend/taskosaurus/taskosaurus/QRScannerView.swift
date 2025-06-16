import SwiftUI

struct QRScannerView: UIViewControllerRepresentable {
    var onFound: (String) -> Void
    var onCancel: () -> Void

    func makeUIViewController(context: Context) -> QRScannerViewController {
        let controller = QRScannerViewController()
        controller.onCodeFound = onFound
        controller.onCancel = onCancel
        return controller
    }

    func updateUIViewController(_ uiViewController: QRScannerViewController, context: Context) {
        // Nichts zu tun – keine dynamische Aktualisierung nötig
    }
}
