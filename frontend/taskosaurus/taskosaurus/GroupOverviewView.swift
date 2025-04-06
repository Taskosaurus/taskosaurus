import SwiftUI
import Charts

struct GroupOverviewView: View {
    @ObservedObject var viewModel: ViewModel
    var group: Group

    @State private var selectedPlayer: Player?
    @State private var votes: [Player: Int] = [:]
    @State private var hasVoted = false

    var body: some View {
        VStack {
            questionSection() // Handle optional question safely
            Spacer()
            
            if let players = group.players, !players.isEmpty {
                playerListSection(players: players)
                voteButton(players: players)
            } else {
                Text("Keine Mitglieder vorhanden")
                    .foregroundColor(.gray)
                    .padding()
            }
        }
        .navigationTitle(group.name)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                NavigationLink(destination: GroupDetailView(viewModel: viewModel, group: group)) {
                    Image(systemName: "person.3.sequence")
                        .font(.title2)
                }
            }
        }
        .onAppear {
            if let firstPlayer = group.players?.first {
                viewModel.getQuestion(playerId: firstPlayer.id!)
            }
        }
    }

    // MARK: - View Sections

    @ViewBuilder
    private func questionSection() -> some View {
        // Safely unwrap ⁠ viewModel.question ⁠ here
        if let question = viewModel.question {
            Text(question.question)
                .font(.title2)
                .fontWeight(.semibold)
                .padding()
        } else {
            Text("Lade Frage...")
                .font(.title3)
                .foregroundColor(.gray)
                .padding()
        }
    }

    @ViewBuilder
    private func playerListSection(players: [Player]) -> some View {
        ScrollView {
            VStack(spacing: 10) {
                ForEach(players) { player in
                    playerRow(player)
                }
            }
            .padding(.horizontal)
        }
    }

    private func playerRow(_ player: Player) -> some View {
        HStack {
            Circle()
                .fill(Color.blue.opacity(0.7))
                .frame(width: 40, height: 40)
                .overlay(
                    Text(player.name.prefix(1))
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
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(selectedPlayer == player ? Color.blue : Color.clear, lineWidth: 2)
        )
        .onTapGesture {
            selectedPlayer = player
        }
    }

    @ViewBuilder
    private func voteButton(players: [Player]) -> some View {
        if let question = viewModel.question, !viewModel.question!.answered {
            Button(action: {
                // Safely unwrap selectedPlayer before calling answerQuestion
                if let selected = selectedPlayer {
                    viewModel.answerQuestion(player: players[0], answeredPlayer: selected, question: question)
                }
            }) {
                Text("Abstimmen")
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(selectedPlayer != nil ? Color.blue : Color.gray)
                    .foregroundColor(.white)
                    .cornerRadius(10)
            }
            .padding()
            .disabled(selectedPlayer == nil)
        } else {
            var playerCountDict: [String: Int] = [:]
            /*for player in viewModel.question!.answers {
                playerCountDict[player.id, default: 0] += 1
            }

            // Step 2: Convert the dictionary to an array of tuples with (name, count)
            let nameCountArray = playerCountDict.map { (name, count) in
                (name, count)
            }

            Chart {
                
                BarMark(
                    x: .value("Shape Type", data[0].type),
                    y: .value("Total Count", data[0].count)
                )
                BarMark(
                     x: .value("Shape Type", data[1].type),
                     y: .value("Total Count", data[1].count)
                )
                BarMark(
                     x: .value("Shape Type", data[2].type),
                     y: .value("Total Count", data[2].count)
                )
            }*/
        }
    }
}
