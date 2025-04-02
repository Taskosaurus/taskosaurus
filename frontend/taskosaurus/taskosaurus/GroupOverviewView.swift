import SwiftUI
import Charts  // Für das Balkendiagramm

struct GroupOverviewView: View {
    @ObservedObject var viewModel: ViewModel
    var group: Group

    @State private var selectedPlayer: Player?  // Ausgewählter Spieler
    @State private var votes: [Player: Int] = [:]  // Stimmen für Spieler
    @State private var hasVoted = false  // Flag: Hat der Nutzer abgestimmt?

    var body: some View {
        VStack {
            // Frage anzeigen
            if let question = viewModel.question {
                Text(question.question)
                    .font(.title2)
                    .fontWeight(.semibold)
                    .padding()
            } else {
                Text("Lade Frage...")
                    .font(.title3)
                    .foregroundColor(.gray)
                    .padding()
            }

            Spacer()
        }
        .navigationTitle(group.name)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                NavigationLink(destination: GroupDetailView(viewModel: viewModel, group: group)) {
                    Image(systemName: "person.3.sequence")
                        .font(.title2)
                }
            }
        }
        .onAppear {
            // Hier wird die Frage beim Laden der View abgerufen
            // Du kannst den playerId anpassen, um den jeweiligen Spieler zu übergeben
            if let firstPlayer = group.players?.first {
                viewModel.getQuestion(playerId: firstPlayer.id ?? 0)  // Frage mit dem ersten Spieler abrufen
            }
        }
    }
}
