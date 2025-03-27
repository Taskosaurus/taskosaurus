import SwiftUI

struct GroupCreationView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var groupName = ""
    @State private var creationSuccess: Bool? = nil
    @State private var lastCreatedGroup: String? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Gruppe erstellen")
                .font(.largeTitle)
                .fontWeight(.bold)
                .padding(.bottom, 5)
            
            Text("Erstelle deine eigene Gruppe.")
                .font(.subheadline)
                .foregroundColor(.gray)
                .padding(.bottom, 15)
            
            HStack {
                TextField("Gruppenname", text: $groupName)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .overlay(RoundedRectangle(cornerRadius: 7)
                        .stroke(creationSuccess == false ? Color.red : Color.clear, lineWidth: 1))

                
                Button(action: {
                    createGroup()
                }) {
                    Image(systemName: "plus.circle.fill")
                        .font(.title)
                        .foregroundColor(.blue)
                        .padding()
                }
            }
            
            if let lastGroup = lastCreatedGroup, creationSuccess == true {
                Text("Gruppe: \(lastGroup) wurde erfolgreich erstellt")
                    .foregroundColor(.green)
                    .font(.footnote)
                    .multilineTextAlignment(.center)
                    .padding(.top, 5)
            }
            
            if creationSuccess == false {
                Text("Fehler beim Erstellen der Gruppe.")
                    .foregroundColor(.red)
                    .fontWeight(.bold)
                    .padding(.top, 5)
            }
            
            Spacer()
        }
        .padding()
    }
    
    private func createGroup() {
        guard !groupName.isEmpty else {
            creationSuccess = false
            return
        }
        
        let createdGroupName = groupName // Speichere den Namen vor dem Zurücksetzen
        viewModel.createGroup(name: createdGroupName)
        DispatchQueue.main.async {
            self.creationSuccess = true
            self.lastCreatedGroup = createdGroupName
            self.groupName = "" // Eingabefeld zurücksetzen
        }
    }
}
