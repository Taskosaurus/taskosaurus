import SwiftUI

struct ContentView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        NavigationStack {
            List(viewModel.groups) { group in
                NavigationLink(destination: GroupDetailView(viewModel: viewModel, group: group)) {
                    Text(group.name)
                        .fontWeight(.bold)
                }
            }
            .navigationTitle("Gruppen")
            .toolbar {
                NavigationLink(destination: GroupCreationView(viewModel: viewModel)) {
                    Text("Gruppe erstellen")
                }
            }
        }
        .onAppear {
            viewModel.fetchGroups()
        }
    }
}
