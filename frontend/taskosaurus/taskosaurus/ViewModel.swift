import Foundation

class ViewModel: ObservableObject {
    @Published var groups: [Group] = []
    @Published var player: Player?
    
    private let service = NetworkService()
    
    // Gruppen abrufen
    func fetchGroups() {
        service.fetchGroups { result in
            switch result {
            case .success(let groups):
                self.groups = groups
            case .failure(let error):
                print("Fehler beim Laden der Gruppen: \(error.localizedDescription)")
            }
        }
    }

    // Gruppe erstellen
    func createGroup(name: String) {
        service.createGroup(name: name) { result in
            if case .failure(let error) = result {
                print("Fehler beim Erstellen der Gruppe: \(error.localizedDescription)")
            }
            self.fetchGroups()
        }
    }
    
    // Spieler erstellen
    func createPlayer(name: String) {
        service.createPlayer(name: name) { result in
            switch result {
            case .success(let player):
                self.player = player
            case .failure(let error):
                print("Fehler beim Erstellen des Spielers: \(error.localizedDescription)")
            }
        }
    }
}
