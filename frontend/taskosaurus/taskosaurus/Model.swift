import Foundation

struct Player: Codable, Identifiable, Hashable {
    var id: Int?
    var name: String
}


struct Group: Identifiable, Codable {
    var id: Int?
    var name: String
    var link: String?
    var players: [Player]?
}

struct Question: Identifiable, Codable {
    var id: Int?
    var question: String
}

struct Model {
    var groups: [Group] = []
}
