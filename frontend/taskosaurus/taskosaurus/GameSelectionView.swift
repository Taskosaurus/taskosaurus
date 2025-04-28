import SwiftUI

struct GameSelectionView: View {
    @ObservedObject var viewModel: ViewModel
    
    var body: some View {
        List {
            if !viewModel.unAnsweredGroups.isEmpty {
                Section {
                    ForEach(viewModel.unAnsweredGroups) { group in
                        NavigationLink(destination: GameView(viewModel: viewModel, group: group)) {
                            Text(group.name)
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
                        Text(group.name)
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
}
