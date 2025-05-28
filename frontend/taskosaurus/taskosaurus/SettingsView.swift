import SwiftUI

struct SettingsView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        Form {
            Section(header: Text("Konto")) {
                Button(role: .destructive) {
                    logout()
                } label: {
                    Text("Abmelden")
                }

                NavigationLink(destination: LoginRegisterView(viewModel: viewModel)) {
                    Text("Mit anderem Account anmelden")
                }
            }
        }
        .navigationTitle("Einstellungen")
    }

    func logout() {
        print("User logged out")
        // Echte Logout-Logik kommt später
    }
}
