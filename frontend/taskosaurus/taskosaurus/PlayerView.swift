import SwiftUI

struct PlayerView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var playerName = ""

    var body: some View {
        VStack {
            TextField("Spielername", text: $playerName)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            Button("Spieler erstellen") {
                viewModel.createPlayer(name: playerName)
            }
            .padding()
        }
    }
}
