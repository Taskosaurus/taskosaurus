import SwiftUI

struct GameSelectionView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        NavigationStack {
            VStack(alignment: .leading) {
                HStack {
                    Text("Gruppen")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                    
                    Spacer()
                    
                    NavigationLink(destination: GroupCreationView(viewModel: viewModel)) {
                        Image(systemName: "plus.square")
                            .font(.title)
                    }
                }
                .padding(.horizontal)
                
                List(viewModel.groups) { group in
                    NavigationLink(destination: GameView(viewModel: viewModel, group: group)) {
                        Text(group.name)
                    }
                }
            }
        }
        .onAppear {
            viewModel.fetchGroups()
        }
    }
}
