import Foundation

class ViewModel: ObservableObject {
    @Published var groups: [Group] = []
        @Published var player: Player?
        
        @Published var answeredGroups: [Group] = []
        @Published var unAnsweredGroups: [Group] = []
        @Published var latestQuestions: [Int: Question] = [:]

        
        private let questionService = QuestionService()
        private let gameService = GameService()
        private let playerService = PlayerService()
        
    func loadQuestionsForGroups() async {
        var answered: [Group] = []
        var unanswered: [Group] = []

        for group in groups {
            guard let firstPlayer = group.players?.first,
                  let playerId = firstPlayer.id,
                  let groupId = group.id else {
                continue
            }

            if let question = await getQuestion(playerId: playerId, groupId: groupId) {
                DispatchQueue.main.async {
                    self.latestQuestions[groupId] = question
                }

                if question.answered {
                    answered.append(group)
                } else {
                    unanswered.append(group)
                }
            } else {
                unanswered.append(group)
            }
        }

        DispatchQueue.main.async {
            self.answeredGroups = answered
            self.unAnsweredGroups = unanswered
        }
    }


    
    
    func fetchGroups(completion: (() -> Void)? = nil) {
        gameService.fetchGroups { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let groups):
                    self.groups = groups
                case .failure(let error):
                    print("Fehler beim Laden der Gruppen: \(error.localizedDescription)")
                }
                completion?()
            }
        }
    }

    
    func createGroup(name: String, completion: @escaping (Bool) -> Void) {
        gameService.createGroup(name: name) { result in
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
    
    func createPlayer(name: String, group: Group, completion: @escaping (Bool) -> Void) {
        playerService.createPlayer(name: name) { (result: Result<Player, Error>) in
            DispatchQueue.main.async {
                switch result {
                case .success(let player):
                    print("test")
                    self.player = player
                    self.joinGroup(player: player, group: group)
                    completion(true)
                case .failure(let error):
                    print("Fehler beim Erstellen des Spielers: \(error.localizedDescription)")
                    completion(false)
                }
            }
        }
    }

    
    func joinGroup(player: Player, group: Group) {
        gameService.joinGroup(player: player, group: group) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let updatedGroup):
                    print("Player successfully joined group.")
                    // Replace old group in groups list
                    if let index = self.groups.firstIndex(where: { $0.id == updatedGroup.id }) {
                        self.groups[index] = updatedGroup
                    } else {
                        self.groups.append(updatedGroup)
                    }
                case .failure(let error):
                    print("Fehler beim joinen: \(error.localizedDescription)")
                }
            }
        }
    }

    
    func getQuestion(playerId: Int, groupId: Int) async -> Question? {
        return await withCheckedContinuation { continuation in
            questionService.fetchDailyQuestion(playerId: playerId, groupId: groupId) { result in
                switch result {
                case .success(let question):
                    // Sortiere die Antworten direkt nach der count-Eigenschaft
                    let sortedAnswers = question.answers.sorted { $0.count > $1.count }
                    var sortedQuestion = question
                    sortedQuestion.answers = sortedAnswers
                    continuation.resume(returning: sortedQuestion)
                case .failure(_):
                    continuation.resume(returning: nil)
                }
            }
        }
    }

    func answerQuestion(player: Player, answeredPlayer: Player, groupId: Int, question: Question) async -> Question? {
        return await withCheckedContinuation { continuation in
            questionService.answerDailyQuestion(
                selectedPlayer: player,
                playerAnswered: answeredPlayer,
                groupId: groupId,
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

