import SwiftUI

struct WhoWouldRatherView: View {
    @ObservedObject var viewModel: ViewModel
    
    var body: some View {
        NavigationStack {
            VStack(spacing: 24) {
                Spacer()
                
                // Haupt-CTA Button mit Animation
                NavigationLink(destination: GameSelectionView(viewModel: viewModel)) {
                    Label {
                        Text("Spielen")
                            .font(.title2.weight(.semibold))
                    } icon: {
                        Image(systemName: "gamecontroller.fill")
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 16)
                    .background(Color.blue.gradient) // Gradient für modernen Look
                    .foregroundColor(.white)
                    .cornerRadius(14)
                    .shadow(color: Color.blue.opacity(0.3), radius: 10, y: 5)
                }
                .padding(.horizontal, 40)
                .buttonStyle(.plain)
                
                // Sekundärer Button
                NavigationLink(destination: GroupCreationView(viewModel: viewModel)) {
                    Label {
                        Text("Spiel erstellen")
                            .font(.headline.weight(.medium))
                    } icon: {
                        Image(systemName: "plus.square.fill")
                    }
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 14)
                    .background(Color(.secondarySystemBackground))
                    .foregroundColor(.blue) // Akzentfarbe
                    .cornerRadius(14)
                }
                .padding(.horizontal, 40)
                .buttonStyle(.plain)
                
                Spacer()
                Spacer()
            }
            .navigationBarTitleDisplayMode(.inline)
            .background(Color(.systemGroupedBackground).ignoresSafeArea())
            .toolbar {
                ToolbarItem(placement: .principal) {
                    Text("Wer würde eher?")
                        .font(.headline)
                        .foregroundColor(.primary)
                }
            }
        }
        .tint(.blue) // Globale Akzentfarbe
        .onAppear {
            viewModel.fetchGroups()
        }
    }
}
