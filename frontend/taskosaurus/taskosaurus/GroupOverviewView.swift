import SwiftUI

struct GroupOverviewView: View {
    @ObservedObject var viewModel: ViewModel
    var group: Group

    var body: some View {
        VStack {
            Text("Test")
                .font(.largeTitle)
                .fontWeight(.bold)
                .padding()
            
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
    }
}
