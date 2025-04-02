import SwiftUI

struct PlayerView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var playerName = ""
    var group: Group

    var body: some View {
        HStack {
            TextField("Spielername", text: $playerName)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            Button(action: {
                viewModel.createPlayer(name: playerName, group: group)
            }) {
                Image(systemName: "plus.circle.fill")
                    .font(.title)
                    .foregroundColor(.blue)
                    .padding()
            }

            
            
        }
    }
}
