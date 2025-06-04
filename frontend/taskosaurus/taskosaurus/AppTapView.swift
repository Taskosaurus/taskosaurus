import SwiftUI

struct AppTabView: View {
    @StateObject private var viewModel = ViewModel()

    var body: some View {
        TabView {
            // Tab 1: Wer würde eher
            NavigationStack {
                WhoWouldRatherView(viewModel: viewModel)
            }
            .tabItem {
                Label("Wer würde eher", systemImage: "person.2.fill")
            }

            // Tab 2: Einstellungen
            NavigationStack {
                SettingsView(viewModel: viewModel)
            }
            .tabItem {
                Label("Einstellungen", systemImage: "gearshape")
            }
        }
        .tint(.blue)
    }
}
