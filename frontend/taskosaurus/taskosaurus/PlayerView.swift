import SwiftUI

struct PlayerView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var playerName = ""
    var group: Group

    var body: some View {
        VStack {
            TextField("Spielername", text: $playerName)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            Button("Spieler erstellen") {
                viewModel.createPlayer(name: playerName, group: group)
            }
            .padding()
        }
    }
}
