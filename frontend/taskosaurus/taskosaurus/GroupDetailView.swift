import SwiftUI

struct GroupDetailView: View {
    @ObservedObject var viewModel: ViewModel
    var group: Group
    @State private var showQRInput = false
    @State private var qrInputText = ""
    @State private var generatedQRImage: UIImage?
    
    var body: some View {
        ZStack {
            // Hauptinhalt
            VStack(spacing: 20) {
                Text("Spiel: " + group.name)
                    .font(.title)
                    .fontWeight(.bold)
                
                Text("Mitglieder dieses Spieles")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                
                PlayerListView(group: group)
                
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
            .blur(radius: showQRInput ? 3 : 0)
            .animation(.easeInOut, value: showQRInput)
            
            // QR Input Overlay
            if showQRInput {
                Color.black.opacity(0.4)
                    .ignoresSafeArea()
                    .zIndex(1)
                    .onTapGesture {
                        // Deaktiviert das Schließen durch Tippen außerhalb
                    }
                
                VStack(spacing: 16) {
                    Text(generatedQRImage == nil ? "QR-Code generieren" : "Dein QR-Code")
                        .font(.headline)
                    
                    if generatedQRImage == nil {
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
                                // Aufruf der ViewModel-Funktion und Zuweisung des QR-Codes
                                if let qrImage = viewModel.generateQRCodeForGroup(playername: qrInputText, group: group) {
                                    generatedQRImage = qrImage
                                }
                            }
                            .frame(maxWidth: .infinity)
                            .disabled(qrInputText.isEmpty)
                        }
                    } else {
                        Image(uiImage: generatedQRImage!)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 200, height: 200)
                            .padding()
                        
                        Button("Fertig") {
                            showQRInput = false
                        }
                        .frame(maxWidth: .infinity)
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
    }
}
