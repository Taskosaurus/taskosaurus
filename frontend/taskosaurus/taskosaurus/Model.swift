import Foundation

struct Player: Codable, Identifiable, Hashable {
    var id: Int?
    var name: String
}

struct Group: Identifiable, Codable, Hashable {
    var id: Int?
    var name: String
    var link: String?
    var players: [Player]?
}

struct Question: Codable {
    var answered: Bool
    var date: String
    var question: String
    var answers: [CollectedAnswer]
}

struct CollectedAnswer: Codable {
    var answeredId: Int
    var answeredName: String
    var count: Int
}

struct Answer: Codable {
    var playerId: Int
    var answerId: Int
    var groupId: Int
    var date: String
    
}
struct Model {
    var groups: [Group] = []
}
