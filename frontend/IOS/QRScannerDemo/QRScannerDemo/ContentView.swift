import SwiftUI

struct ContentView: View {
    @State private var isScannerPresented = false

    var body: some View {
        VStack {
            Text("QR-Code Scanner Demo")
                .font(.title)
                .padding()

            Button("QR-Code scannen") {
                isScannerPresented = true
            }
            .padding()
            .sheet(isPresented: $isScannerPresented) {
                QRScannerView()
            }
        }
    }
}
