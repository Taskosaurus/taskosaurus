import Foundation
import UIKit
import CoreImage
import CoreImage.CIFilterBuiltins

class ViewModel: ObservableObject {
    @Published var groups: [Group] = []
    @Published var playerId: Int = 0
    @Published var player: Player?
    @Published var errorMessage: String?
    @Published var answeredGroups: [Group] = []
    @Published var unAnsweredGroups: [Group] = []
    @Published var latestQuestions: [Int: Question] = [:]
    @Published var hasConnection: Bool = true

    private let questionService = QuestionService()
    private let gameService = GameService()
    private let playerService = PlayerService()

    private var refreshTimer: Timer?

    init() {
        var id = UserDefaults.standard.integer(forKey: "playerId")
        if id != 0 {
            self.playerId = id
            Task {
                self.loadPlayerFromId(id)
            }
        } else {
            loadPlayerFromId(1)
        }
    }

    func startAutoRefresh() {
        self.fetchGroups {
            Task {
                await self.loadQuestionsForGroups()
            }
        }

        refreshTimer = Timer.scheduledTimer(withTimeInterval: 5.0, repeats: true) { [weak self] _ in
            guard let self = self else { return }
            self.fetchGroups {
                Task {
                    await self.loadQuestionsForGroups()
                }
            }
        }
    }

    func stopAutoRefresh() {
        refreshTimer?.invalidate()
        refreshTimer = nil
    }

    func loginWithId(_ id: Int) {
        self.playerId = id
        UserDefaults.standard.set(id, forKey: "playerId")
        self.loadPlayerFromId(id)
    }

    func createAndSaveUser(playerName: String) async -> Player? {
        if let createdPlayer = await createPlayer(name: playerName) {
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
                self.errorMessage = error.localizedDescription
            }
        }
    }

    func loadQuestionsForGroups() async {
        var answered: [Group] = []
        var unanswered: [Group] = []

        let playerId = self.playerId

        for group in groups {
            if let groupId = group.id {
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
        }

        DispatchQueue.main.async {
            self.answeredGroups = answered
            self.unAnsweredGroups = unanswered
        }
    }

    func fetchGroups(completion: @escaping () -> Void = {}) {
        guard let player = self.player else {
            self.hasConnection = false
            completion()
            return
        }

        gameService.fetchGroups(player: player) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let groups):
                    self.groups = groups
                    self.hasConnection = true
                case .failure(let error):
                    self.hasConnection = false
                    self.errorMessage = error.localizedDescription
                }
                completion()
            }
        }
    }

    func createGroup(name: String, completion: @escaping (Result<Group, Error>) -> Void) {
        guard let player = self.player else {
            completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Kein Spieler vorhanden"])))
            return
        }
        gameService.createGroup(player: player, name: name) { result in
            DispatchQueue.main.async {
                switch result {
                case .success():
                    self.fetchGroups {
                        if let createdGroup = self.groups.first(where: { $0.name == name }) {
                            completion(.success(createdGroup))
                        } else {
                            completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Gruppe nicht gefunden"])))
                        }
                    }
                case .failure(let error):
                    completion(.failure(error))
                }
            }
        }
    }

    func createPlayer(name: String) async -> Player? {
        await withCheckedContinuation { continuation in
            playerService.createPlayer(name: name) { result in
                switch result {
                case .success(let player):
                    continuation.resume(returning: player)
                case .failure(let error):
                    print("Fehler beim Erstellen des Spielers: \(error.localizedDescription)")
                    continuation.resume(returning: nil)
                }
            }
        }
    }

    func generateQRCodeForGroup(playername: String, group: Group) -> UIImage? {
        if let link = group.link {
            let url = "\(link)/\(playername)"
            let data = Data(url.utf8)

            if let filter = CIFilter(name: "CIQRCodeGenerator") {
                filter.setValue(data, forKey: "inputMessage")
                let transform = CGAffineTransform(scaleX: 10, y: 10)

                if let output = filter.outputImage?.transformed(by: transform) {
                    let context = CIContext(options: nil)
                    if let cgImage = context.createCGImage(output, from: output.extent) {
                        return UIImage(cgImage: cgImage)
                    }
                }
            }
        }
        return nil
    }

    func joinGroup(player: Player, group: Group) {
        gameService.joinGroup(player: player, group: group) { result in
            DispatchQueue.main.async {
                switch result {
                case .success(let updatedGroup):
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
                    self.hasConnection = true
                    var sortedQuestion = question
                    sortedQuestion.answers = question.answers.sorted { $0.count > $1.count }
                    continuation.resume(returning: sortedQuestion)
                case .failure(let error):
                    continuation.resume(returning: nil)
                    self.errorMessage = error.localizedDescription
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
                    self.hasConnection = true
                    continuation.resume(returning: updatedQuestion)
                case .failure(let error):
                    print("Fehler beim Beantworten: \(error.localizedDescription)")
                    continuation.resume(returning: nil)
                    self.hasConnection = false
                    self.errorMessage = error.localizedDescription
                }
            }
        }
    }
}

