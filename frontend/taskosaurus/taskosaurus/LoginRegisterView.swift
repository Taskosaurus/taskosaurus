import SwiftUI

struct LoginRegisterView: View {
    @State private var name: String = ""
    @State private var password: String = ""
    @State private var passwordRepeat: String = ""
    @State private var isLoginMode: Bool = true
    @State private var isLoading: Bool = false
    
    @ObservedObject var viewModel: ViewModel
    @Environment(\.dismiss) private var dismiss  // <-- für Navigation zurück
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 100, height: 100)
                        .foregroundColor(.blue)
                        .padding(.top, 50)
                    
                    Text("Willkommen")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                    
                    Picker("Modus", selection: $isLoginMode) {
                        Text("Anmelden").tag(true)
                        Text("Registrieren").tag(false)
                    }
                    .pickerStyle(SegmentedPickerStyle())
                    .padding(.horizontal, 40)
                    .padding(.bottom, 20)
                    
                    VStack(spacing: 16) {
                        TextField(isLoginMode ? "Spieler-ID eingeben" : "Dein Name", text: $name)
                            .keyboardType(isLoginMode ? .numberPad : .default)
                            .textFieldStyle(ModernTextFieldStyle())
                        
                        SecureField("Passwort", text: $password)
                            .textFieldStyle(ModernTextFieldStyle())
                        
                        if !isLoginMode {
                            SecureField("Passwort wiederholen", text: $passwordRepeat)
                                .textFieldStyle(ModernTextFieldStyle())
                        }
                    }
                    .padding(.horizontal, 24)
                    
                    Button(action: handleAuth) {
                        HStack {
                            if isLoading {
                                ProgressView()
                                    .tint(.white)
                            }
                            Text(isLoginMode ? "Anmelden" : "Registrieren")
                                .fontWeight(.semibold)
                        }
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                    }
                    .buttonStyle(ModernButtonStyle())
                    .padding(.horizontal, 24)
                    .padding(.top, 20)
                    
                    Spacer()
                }
                .padding()
            }
            .navigationBarHidden(true)
        }
    }
    
    private func handleAuth() {
        isLoading = true
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
            isLoading = false
            if isLoginMode {
                print("Anmelden mit ID: \(name)")
                if let id = Int(name) {
                    UserDefaults.standard.set(id, forKey: "playerId")
                    viewModel.playerId = id
                    Task {
                        await viewModel.loadPlayerFromId(id)
                        dismiss() // <-- Zurück nach erfolgreichem Login
                    }
                } else {
                    print("Ungültige ID")
                }
            } else {
                print("Registrieren mit Name: \(name)")
                Task {
                    if let player = await viewModel.createAndSaveUser(playerName: name) {
                        print(player)
                        dismiss() // <-- Zurück nach erfolgreicher Registrierung
                    }
                }
            }
        }
    }
}

// Custom TextField Style
struct ModernTextFieldStyle: TextFieldStyle {
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .padding(15)
            .background(Color(.systemBackground))
            .cornerRadius(10)
            .shadow(color: Color.black.opacity(0.05), radius: 5, x: 0, y: 2)
            .overlay(
                RoundedRectangle(cornerRadius: 10)
                    .stroke(Color(.systemGray4), lineWidth: 1)
            )
    }
}

// Custom Button Style
struct ModernButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(.white)
            .background(
                LinearGradient(
                    colors: [Color.blue, Color.blue.opacity(0.8)],
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
            )
            .cornerRadius(10)
            .shadow(color: Color.blue.opacity(0.3), radius: 10, x: 0, y: 5)
            .scaleEffect(configuration.isPressed ? 0.98 : 1.0)
            .animation(.easeOut(duration: 0.1), value: configuration.isPressed)
    }
}
