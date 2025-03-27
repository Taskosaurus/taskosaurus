import SwiftUI

struct GroupDetailView: View {
    @ObservedObject var viewModel: ViewModel
    var group: Group

    var body: some View {
        VStack {
            Text("Gruppe: \(group.name)")
                .font(.title)
            List(group.players!) { player in
                Text(player.name)
            }
            PlayerView(viewModel: viewModel, group: group)
        }
    }
}
