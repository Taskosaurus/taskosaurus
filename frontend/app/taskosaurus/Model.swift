import Foundation

struct Player: Codable, Identifiable, Hashable {
    var id: Int
    var name: String
    var group: Group
}

struct Group: Codable, Identifiable, Hashable {
    var id: Int
    var name: String
    var players: [Player]
    var link: String
}

struct Model {
    var groups: [Group] = []
    var player: [Player] = []
}
