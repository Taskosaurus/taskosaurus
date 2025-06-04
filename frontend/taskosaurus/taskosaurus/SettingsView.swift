import SwiftUI

struct SettingsView: View {
    @ObservedObject var viewModel: ViewModel
    
    var body: some View {
        NavigationStack {
            Form {
                Section {
                    if let player = viewModel.player {
                        HStack {
                            Image(systemName: "person.circle.fill")
                                .font(.system(size: 36))
                                .foregroundColor(.blue)
                            
                            VStack(alignment: .leading, spacing: 2) {
                                Text(player.name)
                                    .font(.headline)
                            }
                        }
                        .padding(.vertical, 4)
                    }
                }
                
                Section {
                    Button(role: .destructive) {
                        logout()
                    } label: {
                        HStack {
                            Image(systemName: "rectangle.portrait.and.arrow.right")
                            Text("Abmelden")
                        }
                    }
                    
                    NavigationLink {
                        LoginRegisterView(viewModel: viewModel)
                    } label: {
                        HStack {
                            Image(systemName: "person.badge.plus")
                            Text("Mit anderem Account anmelden")
                        }
                    }
                }
            }
            .navigationTitle("Einstellungen")
        }
    }
    
    func logout() {
        print("User logged out")
        // Echte Logout-Logik kommt später
    }
}
