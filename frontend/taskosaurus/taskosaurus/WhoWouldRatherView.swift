import SwiftUI
struct WhoWouldRatherView: View {
    @ObservedObject var viewModel: ViewModel
    @State private var isPulsing = false

    var body: some View {
        ZStack {
            // 🎨 Fancy Hintergrund, aber NICHT unter TabBar
            LinearGradient(gradient: Gradient(colors: [
                Color(.systemIndigo),
                Color(.systemPurple)
            ]), startPoint: .topLeading, endPoint: .bottomTrailing)
            .overlay(Color.black.opacity(0.3))
            // NICHT mehr: .ignoresSafeArea()
            .ignoresSafeArea(edges: [.top, .horizontal]) // ⛔️ KEIN .bottom

            VStack(spacing: 0) {
                // Header
                VStack(spacing: 8) {
                    Image(systemName: "person.2.wave.2.fill")
                        .font(.system(size: 60))
                        .symbolEffect(.bounce, value: isPulsing)
                        .foregroundStyle(
                            .linearGradient(colors: [.white, .cyan], startPoint: .top, endPoint: .bottom)
                        )
                        .shadow(radius: 10)

                    Text("Wer würde eher?")
                        .font(.largeTitle.weight(.bold))
                        .foregroundColor(.white)
                        .shadow(color: .black.opacity(0.2), radius: 2, x: 0, y: 2)

                    Text("Entdecke was deine Freunde wählen würden")
                        .font(.subheadline)
                        .foregroundColor(.white.opacity(0.9))
                }
                .padding(.top, 60)
                .padding(.bottom, 40)

                Spacer()

                // Buttons
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
                            LinearGradient(gradient: Gradient(colors: [Color.blue, Color.mint]),
                                           startPoint: .leading,
                                           endPoint: .trailing)
                        )
                        .foregroundColor(.white)
                        .cornerRadius(18)
                        .shadow(color: .blue.opacity(0.5), radius: 20, y: 10)
                        .overlay(
                            RoundedRectangle(cornerRadius: 18)
                                .stroke(Color.white.opacity(0.3), lineWidth: 1)
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
                        .background(
                            .ultraThinMaterial,
                            in: RoundedRectangle(cornerRadius: 16, style: .continuous)
                        )
                        .foregroundColor(.white)
                        .overlay(
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(Color.white.opacity(0.3), lineWidth: 1)
                        )
                    }
                    .buttonStyle(ScaleButtonStyle())
                }
                .padding(20)
                .background(
                    .ultraThinMaterial,
                    in: RoundedRectangle(cornerRadius: 28, style: .continuous)
                )
                .padding(.horizontal, 24)
                .padding(.bottom, 24) // Platz vor der weißen TabBar
            }
        }
        .navigationTitle("Wer würde eher?")
        .navigationBarTitleDisplayMode(.inline)
        .toolbarColorScheme(.dark, for: .navigationBar)
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                isPulsing.toggle()
            }
        }
    }
}


// Sanfter Button-Zoom-Effekt beim Drücken
struct ScaleButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.95 : 1)
            .animation(.interactiveSpring(response: 0.3, dampingFraction: 0.5), value: configuration.isPressed)
    }
}
