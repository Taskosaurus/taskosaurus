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


struct Model {
    var groups: [Group] = []
}
