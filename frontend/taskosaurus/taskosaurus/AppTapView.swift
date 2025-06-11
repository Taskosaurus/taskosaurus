import SwiftUI

// Haupt-TabView mit weißer TabBar
struct AppTabView: View {
    @StateObject private var viewModel = ViewModel()
    
    var body: some View {
        TabView {
            // Tab 1 - Hauptscreen
            NavigationStack {
                WhoWouldRatherView(viewModel: viewModel)
                    .padding(.bottom, 18) // 👈 Extra Abstand zur TabBar
            }
            .tabItem {
                Label("Wer würde eher", systemImage: "person.2.fill")
            }
            
            // Tab 2 - Einstellungen
            NavigationStack {
                SettingsView(viewModel: viewModel)
                    .padding(.bottom, 8) // Optional auch hier
            }
            .tabItem {
                Label("Einstellungen", systemImage: "gearshape")
            }
        }
        .tint(.blue) // Farbe für aktive Tab-Icons
        .toolbarBackground(.visible, for: .tabBar) // TabBar sichtbar machen
        .toolbarBackground(Color.white, for: .tabBar) // TabBar weiß machen
        .toolbarColorScheme(.light, for: .tabBar) // iOS helles Farbschema
    }
}
