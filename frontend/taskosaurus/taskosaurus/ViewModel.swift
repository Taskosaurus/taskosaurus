import Foundation

class ViewModel: ObservableObject {
    @Published var groups: [Group] = []
        @Published var player: Player?
        
        @Published var answeredGroups: [Group] = []
        @Published var unAnsweredGroups: [Group] = []
        
        private let questionService = QuestionService()
        private let gameService = GameService()
        private let playerService = PlayerService()
        
    func loadQuestionsForGroups() async {
        var answered: [Group] = []
        var unanswered: [Group] = []
        
        for group in groups {
            guard let firstPlayer = group.players?.first,
                  let playerId = firstPlayer.id else {
                continue // wenn kein Player da ist, einfach überspringen
            }
            
            if let question = await getQuestion(playerId: playerId) {
                if question.answered {
                    answered.append(group)
                } else {
                    unanswered.append(group)
                }
            } else {
                // Falls keine Frage geladen -> Kannst entscheiden: wo hinschieben oder ignorieren
                unanswered.append(group)
            }
        }
        
        DispatchQueue.main.async {
            self.answeredGroups = answered
            self.unAnsweredGroups = unanswered
        }
    }

    
    
    // Gruppen abrufen
    func fetchGroups() {
        gameService.fetchGroups { result in
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
        gameService.createGroup(name: name) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let group):
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
    func createPlayer(name: String, group: Group, completion: @escaping (Bool) -> Void) {
        playerService.createPlayer(name: name) { result in
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
        gameService.joinGroup(player: player, group: group) { result in
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
    func getQuestion(playerId: Int) async -> Question? {
        return await withCheckedContinuation { continuation in
            questionService.fetchDailyQuestion(playerId: playerId) { result in
                switch result {
                case .success(let question):
                    continuation.resume(returning: question)
                case .failure(_):
                    continuation.resume(returning: nil)
                }
            }
        }
    }
    func answerQuestion(player: Player, answeredPlayer: Player, question: Question) async -> Question? {
        return await withCheckedContinuation { continuation in
            questionService.answerDailyQuestion(
                selectedPlayer: player,
                playerAnswered: answeredPlayer,
                question: question
            ) { result in
                switch result {
                case .success(let updatedQuestion):
                    continuation.resume(returning: updatedQuestion)
                case .failure(let error):
                    print("Fehler beim Beantworten: \(error.localizedDescription)")
                    continuation.resume(returning: nil)
                }
            }
        }
    }
}
