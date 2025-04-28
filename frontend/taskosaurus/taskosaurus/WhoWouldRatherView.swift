import SwiftUI

struct WhoWouldRatherView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        NavigationStack {
            VStack(alignment: .leading) {
                NavigationView {
                            VStack {
                                NavigationLink(destination: GameSelectionView(viewModel: viewModel) ) {
                                    Text("Spielen")
                                        .font(.title)
                                        .fontWeight(.bold)
                                        .foregroundColor(.white)
                                        .padding()
                                        .frame(maxWidth: .infinity)
                                        .background(Color.blue)
                                        .cornerRadius(12)
                                        .shadow(radius: 8)
                                        .padding(.horizontal)
                                }
                                NavigationLink(destination: GroupCreationView(viewModel: viewModel)) {
                                    Text("Spiel erstellen")
                                        .font(.headline)
                                        .fontWeight(.regular)
                                        .foregroundColor(.black)
                                        .padding(.vertical, 10)
                                        .padding(.horizontal, 20)
                                        .background(Color.gray.opacity(0.2))
                                        .cornerRadius(8)
                                }
                                .padding(.horizontal, 40)


                            }
                            
                        }
                .navigationTitle("Wer würde eher?")
                
            }
        }
        .onAppear {
            viewModel.fetchGroups()
        }
    }
}
