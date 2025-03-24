import SwiftUI

@main
struct taskosaurusApp: App {
    let viewModel = ViewModel() // Initialisiere das ViewModel

    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: viewModel) // Übergebe das ViewModel an ContentView
        }
    }
}
