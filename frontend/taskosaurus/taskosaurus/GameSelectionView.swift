import SwiftUI

struct GameSelectionView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        List {
            if !viewModel.unAnsweredGroups.isEmpty {
                Section {
                    ForEach(viewModel.unAnsweredGroups) { group in
                        NavigationLink(destination: GameView(viewModel: viewModel, group: group)) {
                            groupRow(group: group)
                        }
                    }
                } header: {
                    Text("Nicht beantwortet")
                        .font(.headline)
                        .textCase(.none)
                }
            }

            Section {
                ForEach(viewModel.answeredGroups) { group in
                    NavigationLink(destination: GameView(viewModel: viewModel, group: group)) {
                        groupRow(group: group)
                    }
                }
            } header: {
                Text("Beantwortet")
                    .font(.headline)
                    .textCase(.none)
            }
        }
        .listStyle(.insetGrouped)
        .navigationTitle("Spiele")
        .onAppear {
            Task {
                await viewModel.loadQuestionsForGroups()
            }
        }
    }

    @ViewBuilder
    private func groupRow(group: Group) -> some View {
        HStack {
            Text(group.name)
            Spacer()

            if let question = viewModel.latestQuestions[group.id ?? -1],
               let total = group.players?.count {
                VoteStatusView(answeredCount: question.answers.count, totalCount: total)
            }
        }
    }
}
