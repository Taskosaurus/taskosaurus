import SwiftUI

struct GroupCreationView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var groupName = ""

    var body: some View {
        VStack {
            TextField("Gruppenname", text: $groupName)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            Button("Gruppe erstellen") {
                viewModel.createGroup(name: groupName)
            }
            .padding()
        }
    }
}
