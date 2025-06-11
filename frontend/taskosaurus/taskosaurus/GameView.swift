import SwiftUI
import Charts

struct GameView: View {
    @ObservedObject var viewModel: ViewModel
    let groupId: Int

    @State private var selectedPlayer: Player?
    @State private var hasVoted = false
    @State private var receivedQuestion: Question?

    private var currentGroup: Group? {
        viewModel.groups.first(where: { $0.id == groupId })
    }

    var body: some View {
        VStack {
            if let group = currentGroup {
                HStack {
                    Text("Bereits abgestimmt: ").foregroundColor(.gray)
                    if let players = group.players, let question = receivedQuestion {
                        VoteStatusView(answers: question.answers, totalCount: players.count)
                    }
                }

                questionSection()

                if let players = group.players, let question = receivedQuestion, !players.isEmpty && !question.answered {
                    playerListSection(players: players)
                    voteButton(players: players, group: group)
                } else if let question = receivedQuestion, question.answered {
                    voteResultsChart(question: question)
                } else {
                    Text("Keine Mitglieder vorhanden")
                        .foregroundColor(.gray)
                        .padding()
                }
            } else {
                Text("Gruppe nicht gefunden")
                    .foregroundColor(.red)
                    .padding()
            }
        }
        .navigationTitle(currentGroup?.name ?? "Gruppe")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            if let group = currentGroup {
                ToolbarItem(placement: .navigationBarTrailing) {
                    NavigationLink(destination: GroupDetailView(viewModel: viewModel, groupId: group.id!)) {
                        Image(systemName: "person.3.sequence")
                            .font(.title2)
                    }
                }
            }
        }
        .onAppear {
            Task {
                if let group = currentGroup, let firstPlayer = group.players?.first, let groupId = group.id {
                    receivedQuestion = await viewModel.getQuestion(playerId: firstPlayer.id!, groupId: groupId)
                }
            }
        }
    }

    // MARK: - View Sections

    @ViewBuilder
    private func questionSection() -> some View {
        if let question = receivedQuestion {
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
    private func voteButton(players: [Player], group: Group) -> some View {
        if let question = receivedQuestion, !question.answered {
            Button(action: {
                Task {
                    if let selected = selectedPlayer,
                       let answeringPlayer = viewModel.player,
                       let updatedQuestion = await viewModel.answerQuestion(
                           player: answeringPlayer,
                           answeredPlayer: selected,
                           groupId: group.id ?? 0,
                           question: question
                       ) {
                        receivedQuestion = updatedQuestion
                    }
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
        }
    }

    @ViewBuilder
    private func voteResultsChart(question: Question) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            let sortedAnswers = question.answers.sorted { $0.count > $1.count }

            Chart {
                ForEach(sortedAnswers, id: \.answeredId) { item in
                    BarMark(
                        x: .value("Votes", item.count),
                        y: .value("Player", item.answeredName)
                    )
                }
            }
            .chartXAxis {
                AxisMarks(position: .bottom)
            }
            .chartYAxis {
                AxisMarks(position: .leading)
            }
            .frame(height: 250)
            .padding(.horizontal)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .topLeading)
    }
}
