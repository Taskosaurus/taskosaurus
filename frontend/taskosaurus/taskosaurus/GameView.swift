import SwiftUI
import Charts

struct GameView: View {
    @ObservedObject var viewModel: ViewModel
    let groupId: Int

    @State private var selectedPlayer: Player?
    @State private var receivedQuestion: Question?

    private var currentGroup: Group? {
        viewModel.groups.first(where: { $0.id == groupId })
    }

    var body: some View {
        ZStack {
            Color(.systemGray6)  // Darker background
                .ignoresSafeArea()

            VStack(spacing: 16) {
                if let group = currentGroup {
                    headerSection(group: group)
                    questionSection()

                    ZStack {
                        if let players = group.players,
                           let question = receivedQuestion,
                           !question.answered,
                           !players.isEmpty {
                            votingSection(players: players, group: group)
                        } else if let question = receivedQuestion, question.answered {
                            resultsSection(question: question)
                        } else {
                            Text("Keine Mitglieder vorhanden")
                                .foregroundColor(.secondary)
                        }
                    }
                    .frame(maxHeight: .infinity, alignment: .top)
                    .padding(.horizontal)
                } else {
                    Text("Gruppe nicht gefunden")
                        .foregroundColor(.red)
                        .padding()
                }
            }
            .padding(.top)
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
                if let group = currentGroup,
                   let firstPlayer = group.players?.first,
                   let groupId = group.id {
                    receivedQuestion = await viewModel.getQuestion(playerId: firstPlayer.id!, groupId: groupId)
                }
            }
        }
    }

    // MARK: - UI Sections

    @ViewBuilder
    private func headerSection(group: Group) -> some View {
        if let question = receivedQuestion,
           let total = group.players?.count {
            let votes = question.answers.reduce(0) { $0 + $1.count }
            let percent = Double(votes) / Double(max(total, 1))

            VStack(spacing: 6) {
                Text("Abgestimmt: \(votes)/\(total)")
                    .font(.subheadline)
                    .foregroundColor(.secondary)

                ProgressView(value: percent)
                    .progressViewStyle(LinearProgressViewStyle(tint: .accentColor))
                    .frame(height: 6)
                    .clipShape(Capsule())
            }
            .padding(.horizontal)
        }
    }

    @ViewBuilder
    private func questionSection() -> some View {
        if let question = receivedQuestion {
            Text(question.question)
                .font(.title2.weight(.semibold))
                .multilineTextAlignment(.center)
                .padding()
        } else {
            ProgressView("Lade Frage...")
                .padding()
        }
    }

    @ViewBuilder
    private func votingSection(players: [Player], group: Group) -> some View {
        VStack(spacing: 12) {
            ScrollView {
                VStack(spacing: 10) {
                    ForEach(players) { player in
                        playerRow(player)
                    }
                }
                .padding(.top, 8)
            }

            Button(action: {
                Task {
                    if let selected = selectedPlayer,
                       let answeringPlayer = viewModel.player,
                       let updated = await viewModel.answerQuestion(
                           player: answeringPlayer,
                           answeredPlayer: selected,
                           groupId: group.id ?? 0,
                           question: receivedQuestion!
                       ) {
                        receivedQuestion = updated
                    }
                }
            }) {
                Text("Abstimmen")
                    .fontWeight(.semibold)
                    .frame(maxWidth: .infinity)
                    .frame(height: 48)
                    .background(selectedPlayer != nil ? Color.accentColor : Color(.systemGray4))
                    .foregroundColor(.white)
                    .cornerRadius(12)
            }
            .disabled(selectedPlayer == nil)
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
        .background(Color.white) // <-- hier jetzt weiß statt grau
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
    private func resultsSection(question: Question) -> some View {
        let sorted = question.answers.sorted { $0.count > $1.count }

        VStack(spacing: 16) {
            Text("Ergebnisse")
                .font(.headline)
                .frame(maxWidth: .infinity, alignment: .leading)

            Chart {
                ForEach(sorted, id: \.answeredId) { item in
                    BarMark(
                        x: .value("Stimmen", item.count),
                        y: .value("Spieler", item.answeredName)
                    )
                    .foregroundStyle(Color.accentColor.gradient)
                    .cornerRadius(6)
                }
            }
            .frame(height: 250)
        }
        .padding()
        .background(Color(.systemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
        .shadow(color: .black.opacity(0.05), radius: 10, x: 0, y: 4)
    }
}
