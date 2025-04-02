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
            if !hasVoted {
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

                // Liste der Spieler mit Auswahlmöglichkeit
                if let players = group.players, !players.isEmpty {
                    VStack(spacing: 10) {
                        ForEach(players) { player in
                            Button(action: {
                                selectedPlayer = player
                            }) {
                                HStack {
                                    Text(player.name)
                                        .font(.body)
                                        .foregroundColor(.primary)
                                        .padding()
                                    
                                    Spacer()

                                    // Abstand nach rechts für den Kreis
                                    if selectedPlayer?.id == player.id {
                                        Image(systemName: "checkmark.circle.fill")
                                            .foregroundColor(.green)
                                            .padding(.trailing, 10)
                                    } else {
                                        Image(systemName: "circle")
                                            .foregroundColor(.gray)
                                            .padding(.trailing, 10)
                                    }
                                }
                                .frame(maxWidth: .infinity)
                                .background(Color(.systemGray6))
                                .cornerRadius(10)
                                .padding(.horizontal)
                            }
                        }
                    }
                } else {
                    Text("Keine Mitglieder in dieser Gruppe")
                        .foregroundColor(.gray)
                        .padding()
                }

                // Bestätigungs-Button
                Button(action: {
                    if let selected = selectedPlayer {
                        votes[selected, default: 0] += 1  // Stimme für den Spieler erhöhen
                        hasVoted = true  // Umfrage ausblenden
                    }
                }) {
                    Text("Bestätigen")
                        .font(.headline)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                .padding()
                .disabled(selectedPlayer == nil)  // Button nur aktiv, wenn ein Spieler gewählt wurde
            } else {
                // Diagramm nur anzeigen, wenn es Stimmen gibt
                if !votes.isEmpty {
                    VStack {
                        Text("Aktuelle Umfrage-Ergebnisse")
                            .font(.headline)
                            .padding(.top)

                        Chart {
                            ForEach(votes.keys.sorted(by: { votes[$0]! > votes[$1]! }), id: \.id) { player in
                                BarMark(
                                    x: .value("Spieler", player.name),
                                    y: .value("Stimmen", votes[player]!)
                                )
                                .foregroundStyle(Color.blue)
                            }
                        }
                        .frame(height: 200)
                        .padding()
                    }
                } else {
                    Text("Noch keine Stimmen abgegeben")
                        .foregroundColor(.gray)
                        .padding()
                }
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
            viewModel.getQuestion()  // Frage abrufen, wenn die Ansicht geladen wird
        }
    }
}
