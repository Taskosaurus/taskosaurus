import SwiftUI

struct GameFormSelectionView: View {
    private let games = [
        GameInfo(title: "Kiss, Marry, Kill", color: Color(red: 1.0, green: 0.23, blue: 0.19), icon: "heart.fill"),
        GameInfo(title: "Wer würde eher", color: Color(red: 0.0, green: 0.48, blue: 1.0), icon: "person.2.fill"),
        GameInfo(title: "Wahrheit oder Pflicht", color: Color(red: 1.0, green: 0.58, blue: 0.0), icon: "questionmark.circle.fill")
    ]
    
    var body: some View {
        NavigationView {
            ZStack {
                LinearGradient(gradient: Gradient(colors: [Color(.systemGray6), Color(.systemGray5)]),
                             startPoint: .top, endPoint: .bottom)
                    .ignoresSafeArea()
                
                VStack(spacing: 20) {
                    Text("Spiel auswählen")
                        .font(.largeTitle.weight(.bold))
                        .foregroundColor(.primary)
                        .padding(.top, 30)
                    
                    Spacer()
                    
                    ForEach(games) { game in
                        if game.title == "Wer würde eher" {
                            NavigationLink(destination: GameSelectionView(viewModel: ViewModel())
                                .navigationBarBackButtonHidden(true)
                                .toolbar {
                                    ToolbarItem(placement: .topBarLeading) {
                                        HomeButton()
                                    }
                                }
                                .accentColor(.blue) // Setzt die Akzentfarbe für diese View
                            ) {
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
                }
            }
            .navigationBarHidden(true)
        }
        .accentColor(.primary) // Haupt-Akzentfarbe bleibt primary
    }
}

struct HomeButton: View {
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        Button(action: {
            dismiss()
        }) {
            Image(systemName: "house.fill")
                .font(.system(size: 20))
                .foregroundColor(.blue) // Explizit blaue Farbe für das Haus-Icon
                .padding(8)
                .background(Color(.systemGray5))
                .clipShape(Circle())
        }
    }
}

struct GameInfo: Identifiable {
    let id = UUID()
    let title: String
    let color: Color
    let icon: String
}

struct GameCardView: View {
    let game: GameInfo
    
    var body: some View {
        HStack(spacing: 15) {
            Image(systemName: game.icon)
                .font(.title)
                .foregroundColor(.white)
                .frame(width: 40)
            
            Text(game.title)
                .font(.title2.weight(.semibold))
                .foregroundColor(.white)
            
            Spacer()
            
            Image(systemName: "chevron.right")
                .font(.body.weight(.bold))
                .foregroundColor(.white.opacity(0.7))
        }
        .padding(25)
        .background(
            RoundedRectangle(cornerRadius: 20)
                .fill(game.color)
                .shadow(color: game.color.opacity(0.3), radius: 10, x: 0, y: 5)
        )
        .frame(height: 90)
        .contentShape(Rectangle())
    }
}
