import Foundation

class ViewModel: ObservableObject {
    @Published var groups: [Group] = []
    @Published var playerId: Int = 0
    @Published var player: Player?
        
    @Published var answeredGroups: [Group] = []
    @Published var unAnsweredGroups: [Group] = []
    @Published var latestQuestions: [Int: Question] = [:]

    private let questionService = QuestionService()
    private let gameService = GameService()
    private let playerService = PlayerService()
    
    init() {
        let id = UserDefaults.standard.integer(forKey: "playerId")
        if id != 0 {
            self.playerId = id
            Task {
                self.loadPlayerFromId(1)
            }
        }
    }

    
    func createAndSaveUser(playerName: String, password: String) async -> Player? {
        if let createdPlayer = await createPlayer(name: playerName, password: password) {
            DispatchQueue.main.async {
                self.player = createdPlayer
                self.playerId = createdPlayer.id ?? 0
                UserDefaults.standard.set(self.playerId, forKey: "playerId")
            }
            return createdPlayer
        } else {
            return nil
        }
    }

    func loadPlayerFromId(_ id: Int) {
        playerService.getPlayer(by: id) { result in
            switch result {
            case .success(let loadedPlayer):
                DispatchQueue.main.async {
                    self.player = loadedPlayer
                }
            case .failure(let error):
                print("Fehler beim Laden des Spielers: \(error.localizedDescription)")
            }
        }
    }

    
        
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
        if let player = self.player{
            gameService.fetchGroups(player: player) { result in
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
    
    func createPlayer(name: String, password: String) async -> Player? {
        await withCheckedContinuation { continuation in
            playerService.createPlayer(name: name, password: password) { result in
                switch result {
                case .success(let player):
                    continuation.resume(returning: player)
                    UserDefaults.standard.set(player.id, forKey: "playerId")
                case .failure(let error):
                    print("Fehler beim Erstellen des Spielers: \(error.localizedDescription)")
                    continuation.resume(returning: nil)
                }
            }
        }
    }

    func loginPlayer(name: String, password: String) async -> Player? {
        await withCheckedContinuation { continuation in
            playerService.loginPlayer(name: name, password: password) { result in
                switch result {
                case .success(let player):
                    continuation.resume(returning: player)
                    self.player = player
                    UserDefaults.standard.set(player.id, forKey: "playerId")
                case .failure(let error):
                    print("Fehler beim LOGIN des Spielers: \(error.localizedDescription)")
                    continuation.resume(returning: nil)
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

