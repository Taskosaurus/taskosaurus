import SwiftUI

struct LoginRegisterView: View {
    @State private var name: String = ""
    @State private var isLoginMode: Bool = true
    @State private var isLoading: Bool = false
    
    @ObservedObject var viewModel: ViewModel
    
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
                        TextField("Dein Name", text: $name)
                            .textFieldStyle(ModernTextFieldStyle())
                        
                        // weitere Felder hinzufügen (Passwort)
                    }
                    .padding(.horizontal, 24)
                    
                    // Action Button
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
                    
                    Text("playerId: \(viewModel.playerId)")
                    
                    Spacer()
                }
                .padding()
            }
            .background(Color(.systemGroupedBackground).ignoresSafeArea())
            .navigationBarHidden(true)
        }
    }
    
    private func handleAuth() {
        isLoading = true
        // Simuliere Netzwerkanfrage
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
            isLoading = false
            if isLoginMode {
                print("Anmelden mit Name: \(name)")
                
                
            } else {
                print("Registrieren mit Name: \(name)")
                Task{
                    if let player = await viewModel.createAndSaveUser(playerName: name){
                        print(player)
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

