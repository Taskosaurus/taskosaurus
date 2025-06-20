import SwiftUI

struct WhoWouldRatherView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var isPulsing = false
    
    var body: some View {
        ZStack {
            
            VStack(spacing: 0) {
                Spacer() // Push content down
                
                // Header - jetzt weiter unten
                VStack(spacing: 8) {
                    Image(systemName: "person.2.wave.2.fill")
                        .font(.system(size: 60))
                        .symbolEffect(.bounce, value: isPulsing)
                        .foregroundStyle(
                            .linearGradient(colors: [.blue, .cyan], startPoint: .top, endPoint: .bottom)
                        )
                    
                    Text("Wer würde eher?")
                        .font(.largeTitle.weight(.bold))
                        .foregroundColor(.primary)
                    
                    Text("Entdecke was deine Freunde wählen würden")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                .padding(.bottom, 40) // Abstand zu den Buttons
                
                Spacer() // Gleichmäßige Verteilung
                
                // Buttons bleiben unverändert
                VStack(spacing: 20) {
                    NavigationLink(destination: GameSelectionView(viewModel: viewModel)) {
                        HStack(spacing: 12) {
                            Image(systemName: "play.fill")
                                .font(.title3.weight(.bold))
                            Text("Jetzt spielen")
                                .font(.title2.weight(.semibold))
                        }
                        .frame(maxWidth: .infinity)
                        .frame(height: 60)
                        .background(
                            LinearGradient(gradient: Gradient(colors: [
                                Color.blue,
                                Color(red: 0.2, green: 0.6, blue: 1)
                            ]),
                                           startPoint: .leading,
                                           endPoint: .trailing)
                        )
                        .foregroundColor(.white)
                        .cornerRadius(18)
                        .shadow(color: .blue.opacity(0.2), radius: 10, y: 5)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.blue.opacity(0.3), lineWidth: 1)
                        )
                    }
                    .buttonStyle(ScaleButtonStyle())
                    
                    NavigationLink(destination: GroupCreationView(viewModel: viewModel)) {
                        HStack(spacing: 12) {
                            Image(systemName: "plus")
                                .font(.headline.weight(.bold))
                            Text("Spiel erstellen")
                                .font(.headline.weight(.semibold))
                        }
                        .frame(maxWidth: .infinity)
                        .frame(height: 50)
                        .foregroundColor(.blue)
                        .cornerRadius(16)
                        .overlay(
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(Color.blue.opacity(0.3), lineWidth: 1)
                        )
                        .shadow(color: .black.opacity(0.05), radius: 5, y: 3)
                    }
                    .buttonStyle(ScaleButtonStyle())
                }
                .padding(20)
                .cornerRadius(28)
                .padding(.horizontal, 24)
                .padding(.bottom, 24)
            }
        }
        .navigationTitle("Wer würde eher?")
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                isPulsing.toggle()
            }
        }
    }
}
