import SwiftUI

struct GroupCreationView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var groupName = ""
    @State private var playerName = ""
    @State private var creationSuccess: Bool? = nil
    @State private var feedbackMessage: String? = nil
    @Environment(\.dismiss) private var dismiss
    
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
            
            // Gruppennamen-Eingabe
            TextField("Spielname", text: $groupName)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .overlay(RoundedRectangle(cornerRadius: 7)
                    .stroke(creationSuccess == false ? Color.red : Color.clear, lineWidth: 1))
            
            // Spielernamen-Eingabe
            TextField("Gib deinen Namen ein:", text: $playerName)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .overlay(RoundedRectangle(cornerRadius: 7)
                    .stroke(creationSuccess == false ? Color.red : Color.clear, lineWidth: 1))
            
            // Rückmeldung (immer Platz reserviert)
            ZStack {
                if let message = feedbackMessage {
                    Text(message)
                        .foregroundColor(creationSuccess == true ? .green : .red)
                        .font(.footnote)
                        .multilineTextAlignment(.center)
                        .transition(.opacity)
                } else {
                    Text(" ") // Platzhalter für gleichbleibende Höhe
                        .font(.footnote)
                        .opacity(0)
                }
            }
            .frame(height: 20) // feste Höhe für Stabilität
            .padding(.top, 5)
            
            // Button
            Button(action: {
                createGroupAndPlayer()
            }) {
                HStack {
                    Image(systemName: "plus.circle.fill")
                    Text("Spiel & Spieler erstellen")
                }
                .font(.title2)
                .padding()
                .frame(maxWidth: .infinity)
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(10)
            }
            .padding(.top)

            Spacer()
        }
        .padding()
    }
    
    private func createGroupAndPlayer() {
        guard !groupName.isEmpty, !playerName.isEmpty else {
            creationSuccess = false
            feedbackMessage = "Bitte alle Felder ausfüllen."
            return
        }
        
        viewModel.createGroup(name: groupName) { groupSuccess in
            DispatchQueue.main.async {
                if groupSuccess {
                    viewModel.fetchGroups()
                } else {
                    creationSuccess = false
                    feedbackMessage = "Gruppe konnte nicht erstellt werden."
                }
            }
        }
    }
}
