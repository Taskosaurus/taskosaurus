import SwiftUI

struct GroupDetailView: View {
    @ObservedObject var viewModel: ViewModel
    var group: Group

    var body: some View {
        VStack(spacing: 20) {
            // Titel der Gruppe
            Text("Gruppe: " + group.name)
                .font(.title)
                .fontWeight(.bold)
                .foregroundColor(.primary)
                .padding(.top, 10)

            Text("Mitglieder dieser Gruppe")
                .font(.subheadline)
                .foregroundColor(.black)

            // Liste der Spieler
            if let players = group.players, !players.isEmpty {
                ScrollView {
                    VStack(spacing: 10) {
                        ForEach(players) { player in
                            HStack {
                                Circle()
                                    .fill(Color.blue.opacity(0.7))
                                    .frame(width: 40, height: 40)
                                    .overlay(Text(player.name.prefix(1))
                                        .font(.headline)
                                        .foregroundColor(.white)
                                    )
                                
                                Text(player.name)
                                    .font(.body)
                                    .foregroundColor(.primary)
                                    .padding(.leading, 10)
                                
                                Spacer()
                            }
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(Color(.systemGray6))
                            .cornerRadius(10)
                            .shadow(radius: 2)
                        }
                    }
                    .padding(.horizontal)
                }
            } else {
                Text("Keine Mitglieder vorhanden")
                    .foregroundColor(.gray)
                    .padding()
            }

            // Spieler hinzufügen
            PlayerView(viewModel: viewModel, group: group)
                .padding()

            Spacer()
        }
        .padding()
    }
}
