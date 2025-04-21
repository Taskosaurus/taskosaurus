import SwiftUI

struct GroupDetailView: View {
    @ObservedObject var viewModel: ViewModel
    var group: Group

    var body: some View {
        VStack(spacing: 20) {
            // Titel der Gruppe
            Text("Gruppe: " + group.name)
                .font(.title)
                .fontWeight(.bold)
                .foregroundColor(.primary)
                .padding(.top, 10)

            Text("Mitglieder dieser Gruppe")
                .font(.subheadline)
                .foregroundColor(.black)

            PlayerListView(group:group)

            // Spieler hinzufügen
            //PlayerView(viewModel: viewModel, group: group)
                //.padding()

            Spacer()
        }
        .padding()
    }
}
