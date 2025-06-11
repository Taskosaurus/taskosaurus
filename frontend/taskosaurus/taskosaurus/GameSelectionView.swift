import SwiftUI

struct GameSelectionView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        List {
            if !viewModel.unAnsweredGroups.isEmpty {
                Section(header: Text("Nicht beantwortet")) {
                    ForEach(viewModel.unAnsweredGroups) { group in
                        NavigationLink(destination: GameView(viewModel: viewModel, groupId: group.id!)) {
                            groupRow(group: group, icon: "circle.dotted", color: .blue)
                        }
                    }
                }
            }

            if !viewModel.answeredGroups.isEmpty {
                Section(header: Text("Beantwortet")) {
                    ForEach(viewModel.answeredGroups) { group in
                        NavigationLink(destination: GameView(viewModel: viewModel, groupId: group.id!)) {
                            groupRow(group: group, icon: "checkmark.circle.fill", color: .blue)
                        }
                    }
                }
            }
        }
        .listStyle(.insetGrouped)
        .navigationTitle("Spiele")
        .onAppear {
            viewModel.startAutoRefresh()
        }
    }

    private func groupRow(group: Group, icon: String, color: Color) -> some View {
        HStack {
            Image(systemName: icon)
                .foregroundColor(color)
                .imageScale(.medium)

            Text(group.name)
                .font(.body)

            Spacer()

            if let question = viewModel.latestQuestions[group.id ?? -1],
               let total = group.players?.count {
                VoteStatusView(answers: question.answers, totalCount: total)
            }
        }
    }
}
