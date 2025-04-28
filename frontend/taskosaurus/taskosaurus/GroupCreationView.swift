import SwiftUI

struct GroupCreationView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var groupName = ""
    @State private var playerName = ""
    @State private var groupCreationSuccess: Bool? = nil
    @State private var playerCreationSuccess: Bool? = nil
    @State private var lastCreatedGroup: String? = nil
    @State private var lastCreatedPlayer: String? = nil
    @Environment(\.dismiss) private var dismiss // Zum Schließen der View
    
    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Spiel erstellen")
                .font(.largeTitle)
                .fontWeight(.bold)
                .padding(.bottom, 5)
            
            Text("Erstelle dein eigenes Spiel.")
                .font(.subheadline)
                .foregroundColor(.gray)
                .padding(.bottom, 15)
            
            // Eingabefeld für den Gruppennamen
            HStack {
                TextField("Spielname", text: $groupName)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .overlay(RoundedRectangle(cornerRadius: 7)
                        .stroke(groupCreationSuccess == false ? Color.red : Color.clear, lineWidth: 1))
                
                Button(action: {
                    createGroupAndPlayer()
                }) {
                    Image(systemName: "plus.circle.fill")
                        .font(.title)
                        .foregroundColor(.blue)
                        .padding()
                }
            }
            
            // Erfolgsmeldung für Gruppe
            if let lastGroup = lastCreatedGroup, groupCreationSuccess == true {
                Text("Spiel: \(lastGroup) wurde erfolgreich erstellt")
                    .foregroundColor(.green)
                    .font(.footnote)
                    .multilineTextAlignment(.center)
                    .padding(.top, 5)
            }
            
            // Fehler bei der Gruppen-Erstellung
            if groupCreationSuccess == false {
                Text("Fehler beim Erstellen des Spiels.")
                    .foregroundColor(.red)
                    .fontWeight(.bold)
                    .padding(.top, 5)
            }
            
            // Eingabefeld für den Spielernamen
            HStack {
                TextField("Gib deinen Namen ein:", text: $playerName)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .overlay(RoundedRectangle(cornerRadius: 7)
                        .stroke(playerCreationSuccess == false ? Color.red : Color.clear, lineWidth: 1))
                
                Button(action: {
                    createGroupAndPlayer()
                }) {
                    Image(systemName: "plus.circle.fill")
                        .font(.title)
                        .foregroundColor(.blue)
                        .padding()
                }
            }
            
            // Erfolgsmeldung für Spieler
            if let lastPlayer = lastCreatedPlayer, playerCreationSuccess == true {
                Text("Spieler: \(lastPlayer) wurde erfolgreich erstellt und der Gruppe zugewiesen")
                    .foregroundColor(.green)
                    .font(.footnote)
                    .multilineTextAlignment(.center)
                    .padding(.top, 5)
            }
            
            // Fehler bei der Spieler-Erstellung
            if playerCreationSuccess == false {
                Text("Fehler beim Erstellen des Spielers.")
                    .foregroundColor(.red)
                    .fontWeight(.bold)
                    .padding(.top, 5)
            }
            
            Spacer()
        }
        .padding()
    }
    
    private func createGroupAndPlayer() {
        guard !groupName.isEmpty, !playerName.isEmpty else {
            groupCreationSuccess = false
            playerCreationSuccess = false
            return
        }
        
        // Erstelle die Gruppe und gib sie zurück
        viewModel.createGroup(name: groupName) { groupSuccess in
            DispatchQueue.main.async {
                self.groupCreationSuccess = groupSuccess
                if groupSuccess {
                    self.lastCreatedGroup = groupName
                    self.groupName = "" // Eingabefeld leeren
                    
                    // Jetzt Spieler erstellen und der Gruppe zuweisen
                    let group: Group = viewModel.groups[viewModel.groups.count - 1]
                    let createdPlayerName = self.playerName
                    viewModel.createPlayer(name: createdPlayerName, group: group) { playerSuccess in     DispatchQueue.main.async {
                            self.playerCreationSuccess = playerSuccess
                            if playerSuccess {
                                self.lastCreatedPlayer = createdPlayerName
                                self.playerName = "" // Eingabefeld leeren
                                
                                // Automatisch nach 0.8 Sekunden zurück
                                DispatchQueue.main.asyncAfter(deadline: .now() + 0.8) {
                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

