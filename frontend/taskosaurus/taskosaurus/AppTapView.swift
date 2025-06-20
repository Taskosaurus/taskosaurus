import SwiftUI

struct AppTabView: View {
    @StateObject private var viewModel = ViewModel()
    
    var body: some View {
        TabView {
            // Tab 1 - Hauptscreen
            NavigationStack {
                WhoWouldRatherView(viewModel: viewModel)
            }
            .tabItem {
                Label("Spielen", systemImage: "gamecontroller.fill")
            }
            
            // Tab 2 - Einstellungen
            NavigationStack {
                SettingsView(viewModel: viewModel)
            }
            .tabItem {
                Label("Einstellungen", systemImage: "gearshape.fill")
            }
        }
        .tint(.blue)
        .toolbarBackground(.visible, for: .tabBar)
        .toolbarBackground(Color.white, for: .tabBar)
        .toolbarColorScheme(.light, for: .tabBar)
    }
}

// Button style bleibt gleich
struct ScaleButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.95 : 1)
            .animation(.interactiveSpring(response: 0.3, dampingFraction: 0.5), value: configuration.isPressed)
    }
}
