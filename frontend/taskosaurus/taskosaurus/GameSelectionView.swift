import SwiftUI

struct GameSelectionView: View {
    private let games = [
        GameInfo(title: "Kiss, Marry, Kill", color: Color(red: 1.0, green: 0.23, blue: 0.19), icon: "heart.fill"),
        GameInfo(title: "Wer würde eher", color: Color(red: 0.0, green: 0.48, blue: 1.0), icon: "person.2.fill"),
        GameInfo(title: "Wahrheit oder Pflicht", color: Color(red: 1.0, green: 0.58, blue: 0.0), icon: "questionmark.circle.fill")
    ]
    
    var body: some View {
        NavigationView {
            ZStack {
                // Hintergrund-Gradient
                LinearGradient(
                    gradient: Gradient(colors: [Color(.systemGray6), Color(.systemGray5)]),
                    startPoint: .top,
                    endPoint: .bottom
                )
                .ignoresSafeArea()
                
                // Hauptinhalt
                VStack(spacing: 20) {
                    // Titel
                    Text("Spiel auswählen")
                        .font(.system(size: 32, weight: .bold, design: .rounded))
                        .foregroundColor(.primary)
                        .padding(.top, 30)
                    
                    Spacer()
                    
                    // Spielkarten
                    ForEach(games) { game in
                        if game.title == "Wer würde eher" {
                            NavigationLink(destination: ContentView(viewModel: ViewModel())) {
                                GameCardView(game: game)
                            }
                        } else {
                            Button(action: {
                                print("\(game.title) wurde ausgewählt")
                            }) {
                                GameCardView(game: game)
                            }
                        }
                    }
                    .padding(.horizontal, 20)
                    
                    Spacer()
                    
                    // Tab Bar mit Home-Button
                    HStack {
                        Spacer()
                        HomeButton()
                        Spacer()
                    }
                    .padding(.bottom, UIApplication.shared.windows.first?.safeAreaInsets.bottom ?? 0 > 0 ? 0 : 20)
                    .background(
                        Color(.systemBackground)
                            .opacity(0.8)
                            .edgesIgnoringSafeArea(.bottom)
                    )
                }
            }
            .navigationBarHidden(true)
        }
        .accentColor(.blue)
    }
}

struct HomeButton: View {
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        Button(action: {
            presentationMode.wrappedValue.dismiss()
        }) {
            VStack(spacing: 4) {
                Image(systemName: "house.fill")
                    .font(.system(size: 22, weight: .bold))
                Text("Home")
                    .font(.system(size: 12, weight: .medium))
            }
            .foregroundColor(.white)
            .frame(width: 80, height: 60)
            .background(
                LinearGradient(
                    gradient: Gradient(colors: [Color.blue, Color.blue.opacity(0.8)]),
                    startPoint: .top,
                    endPoint: .bottom
                )
            )
            .clipShape(RoundedRectangle(cornerRadius: 18, style: .continuous))
            .shadow(color: Color.blue.opacity(0.3), radius: 10, x: 0, y: 5)
            .padding(.bottom, 5)
        }
        .buttonStyle(ScaleButtonStyle())
    }
}

struct GameCardView: View {
    let game: GameInfo
    
    var body: some View {
        HStack(spacing: 15) {
            Image(systemName: game.icon)
                .font(.system(size: 24, weight: .bold))
                .foregroundColor(.white)
                .frame(width: 40, height: 40)
                .background(Color.white.opacity(0.2))
                .clipShape(Circle())
            
            Text(game.title)
                .font(.system(size: 20, weight: .semibold, design: .rounded))
                .foregroundColor(.white)
            
            Spacer()
            
            Image(systemName: "chevron.right")
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(.white.opacity(0.8))
        }
        .padding(20)
        .background(
            LinearGradient(
                gradient: Gradient(colors: [game.color, game.color.opacity(0.8)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        )
        .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
        .shadow(color: game.color.opacity(0.3), radius: 10, x: 0, y: 5)
        .frame(height: 90)
        .padding(.vertical, 5)
        .contentShape(Rectangle())
    }
}

struct ScaleButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.95 : 1.0)
            .animation(.spring(response: 0.3, dampingFraction: 0.6), value: configuration.isPressed)
    }
}

struct GameInfo: Identifiable {
    let id = UUID()
    let title: String
    let color: Color
    let icon: String
}
