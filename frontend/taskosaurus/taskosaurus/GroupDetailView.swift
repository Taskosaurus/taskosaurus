import SwiftUI

struct GroupDetailView: View {
    @ObservedObject var viewModel: ViewModel
    let groupId: Int

    @State private var showQRInput = false
    @State private var qrInputText = ""
    @State private var generatedQRImage: UIImage?

    private var currentGroup: Group? {
        viewModel.groups.first(where: { $0.id == groupId })
    }

    var body: some View {
        ZStack {
            if !viewModel.hasConnection {
                // Verbindung unterbrochen:
                ErrorView(viewModel: viewModel)
            }
            if let group = currentGroup {
                mainContent(group: group)
                    .blur(radius: showQRInput ? 3 : 0)
                    .animation(.easeInOut, value: showQRInput)
            }
            else {
                Text("Gruppe nicht gefunden")
                    .foregroundColor(.red)
                    .padding()
            }
            if showQRInput, let group = currentGroup {
                qrOverlay(for: group)
            }
        }
    }
        
    

    // MARK: - Main Content
    private func mainContent(group: Group) -> some View {
        VStack(spacing: 20) {
            Text("Spiel: \(group.name)")
                .font(.title)
                .fontWeight(.bold)

            Text("Mitglieder dieses Spiels")
                .font(.subheadline)
                .foregroundColor(.secondary)

           PlayerListView( group: group, viewModel: viewModel)

            Button {
                showQRInput = true
                qrInputText = ""
                generatedQRImage = nil
            } label: {
                Label("Freunde einladen", systemImage: "qrcode")
                    .padding(.horizontal, 20)
                    .padding(.vertical, 10)
            }
            .buttonStyle(.borderedProminent)
            .padding(.top)

            Spacer()
        }
        .padding()
    }

    // MARK: - QR Overlay

    @ViewBuilder
    private func qrOverlay(for group: Group) -> some View {
        Color.black.opacity(0.4)
            .ignoresSafeArea()
            .zIndex(1)

        VStack(spacing: 16) {
            Text(generatedQRImage == nil ? "QR-Code generieren" : "Dein QR-Code")
                .font(.headline)

            if let image = generatedQRImage {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFit()
                    .frame(width: 200, height: 200)
                    .padding()

                Button("Fertig") {
                    showQRInput = false
                }
                .frame(maxWidth: .infinity)
            } else {
                Text("Gib den Spielernamen eines Freundes ein")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)

                TextField("Name eingeben", text: $qrInputText)
                    .textFieldStyle(.roundedBorder)
                    .padding(.horizontal)

                HStack(spacing: 16) {
                    Button("Abbrechen") {
                        showQRInput = false
                    }
                    .frame(maxWidth: .infinity)
                    .foregroundColor(.red)

                    Button("Generieren") {
                        if let qrImage = viewModel.generateQRCodeForGroup(playername: qrInputText, group: group) {
                            generatedQRImage = qrImage
                        }
                    }
                    .frame(maxWidth: .infinity)
                    .disabled(qrInputText.isEmpty)
                }
            }
        }
        .padding()
        .background(Color(.systemBackground))
        .cornerRadius(14)
        .shadow(radius: 10)
        .frame(width: 300)
        .transition(.scale.combined(with: .opacity))
        .zIndex(2)
    }
}

