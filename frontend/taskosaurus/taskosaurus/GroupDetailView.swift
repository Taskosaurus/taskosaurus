import SwiftUI

struct GroupDetailView: View {
    var group: Group

    var body: some View {
        VStack {
            Text("Gruppe: \(group.name)")
                .font(.title)
            List(group.players) { player in
                Text(player.name)
            }
        }
    }
}
