import SwiftUI

struct GameSelectionView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        if let errorMessage = viewModel.errorMessage {
            Text(errorMessage)
        }
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
            if !viewModel.answeredGroups.isEmpty {
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
        }
        .listStyle(.insetGrouped)
        .navigationTitle("Spiele")
        .onAppear {
            Task {
                await viewModel.loadQuestionsForGroups()
            }
            viewModel.fetchGroups()
        }
        
    }
   

    @ViewBuilder
    private func groupRow(group: Group) -> some View {
        HStack {
            Text(group.name)
            Spacer()

            if let question = viewModel.latestQuestions[group.id ?? -1],
               let total = group.players?.count {
                VoteStatusView(answers: question.answers, totalCount: total)
            }
        }
    }
}
