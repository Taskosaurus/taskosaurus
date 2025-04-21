//
//  PlayerService.swift
//  taskosaurus
//
//  Created by Isabella Baumann on 21.04.25.
//

import Foundation

class PlayerService {
    
    let baseURL = "http://localhost:8080/api/player"
    
    // Spieler erstellen (POST)
    func createPlayer(name: String, completion: @escaping (Result<Player, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/create") else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        //let player = Player(id: nil, name: name)
        let playerData = ["name": name]
        request.httpBody = try? JSONEncoder().encode(playerData)
        
        URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                completion(.failure(error))
                return
            }
            guard let data = data else {
                completion(.failure(URLError(.badServerResponse)))
                return
            }
            do {
                let player = try JSONDecoder().decode(Player.self, from: data)
                DispatchQueue.main.async {
                    completion(.success(player))
                }
            } catch {
                completion(.failure(error))
            }
        }.resume()
    }
}
