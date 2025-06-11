import SwiftUI

struct GroupCreationView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var groupName = ""
    @State private var creationSuccess: Bool? = nil
    @State private var feedbackMessage: String? = nil
    @State private var navigateToGroup: Group? = nil
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        NavigationStack {
            VStack(alignment: .leading, spacing: 10) {
                Text("Spiel erstellen")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .padding(.bottom, 5)
                
                Text("Erstelle dein eigenes Spiel.")
                    .font(.subheadline)
                    .foregroundColor(.gray)
                    .padding(.bottom, 15)
                
                TextField("Spielname", text: $groupName)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .overlay(RoundedRectangle(cornerRadius: 7)
                        .stroke(creationSuccess == false ? Color.red : Color.clear, lineWidth: 1))
                
                ZStack {
                    if let message = feedbackMessage {
                        Text(message)
                            .foregroundColor(creationSuccess == true ? .green : .red)
                            .font(.footnote)
                            .multilineTextAlignment(.center)
                            .transition(.opacity)
                    } else {
                        Text(" ").font(.footnote).opacity(0)
                    }
                }
                .frame(height: 20)
                .padding(.top, 5)
                
                Button(action: {
                    createGroup()
                }) {
                    HStack {
                        Image(systemName: "plus.circle.fill")
                        Text("Spiel erstellen")
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
            .navigationDestination(item: $navigateToGroup) { group in
                GroupDetailView(viewModel: viewModel, groupId: group.id!)
            }
        }
    }

    private func createGroup() {
        guard !groupName.isEmpty else {
            creationSuccess = false
            feedbackMessage = "Bitte den Gruppennamen eingeben."
            return
        }

        viewModel.createGroup(name: groupName) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let group):
                    self.creationSuccess = true
                    self.feedbackMessage = "Gruppe erfolgreich erstellt."
                    self.navigateToGroup = group // Navigation auslösen
                case .failure(let error):
                    self.creationSuccess = false
                    self.feedbackMessage = "Fehler: \(error.localizedDescription)"
                }
            }
        }
    }

}
