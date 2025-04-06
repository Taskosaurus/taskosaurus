import Foundation

class ViewModel: ObservableObject {
    @Published var groups: [Group] = []
    @Published var player: Player?
    @Published var question: Question?
    
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
    func createGroup(name: String, completion: @escaping (Bool) -> Void) {
        service.createGroup(name: name) { result in
            DispatchQueue.main.async {
                switch result {
                    case .success():
                        self.fetchGroups()
                        completion(true)
                    case .failure(let error):
                        print("Fehler beim Erstellen der Gruppe: \(error.localizedDescription)")
                        completion(false)
                }
            }
        }
    }
    
    // Spieler erstellen
    func createPlayer(name: String, group: Group) {
        service.createPlayer(name: name) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let player):
                    self.player = player
                    self.joinGroup(player: self.player!, group: group)
                case .failure(let error):
                    print("Fehler beim Erstellen des Spielers: \(error.localizedDescription)")
                }
            }
        }
    }
    
    //beim spieler erstellen, gleich Gruppe zuweisen
    func joinGroup(player: Player, group: Group) {
        service.joinGroup(player: player, group: group) { result in
            DispatchQueue.main.async {
                switch result {
                case .success():
                    print("success")
                    self.fetchGroups()
                case .failure(let error):
                    print("Fehler beim joinen: \(error.localizedDescription)")
                }
            }
        }
    }
    
    // Holen der täglichen Frage
        func getQuestion(playerId: Int) {
            service.fetchDailyQuestion(playerId: playerId) { result in
                switch result {
                case .success(let question):
                    self.question = question
                case .failure(let error):
                    print("Fehler beim Abrufen der Frage: \(error.localizedDescription)")
                }
            }
        }
    func answerQuestion(player: Player, answeredPlayer: Player, question: Question) {
        print(player)
        print(answeredPlayer)
        print(question)
        service.answerDailyQuestion(selectedPlayer: player, playerAnswered: answeredPlayer, question: question) { result in
            switch result {
            case .success(let question):
                self.question = question
            case .failure(let error):
                print("Fehler beim Beantworten der Frage: \(error.localizedDescription)")
            }
        }
    }

}
