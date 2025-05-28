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
    func createPlayer(name: String, password: String, completion: @escaping (Result<Player, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/create") else {
            print("Ungültige URL für Spieler erstellen")
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let playerData = ["name": name, "password": password]
        
        do {
            request.httpBody = try JSONEncoder().encode(playerData)
        } catch {
            print("Fehler beim Codieren der Daten: \(error)")
            completion(.failure(error))
            return
        }

        print("Sende Anfrage an \(url)")
        print("Request Body: \(String(data: request.httpBody ?? Data(), encoding: .utf8) ?? "nil")")

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Netzwerkfehler: \(error)")
                completion(.failure(error))
                return
            }

            if let httpResponse = response as? HTTPURLResponse {
                print("Statuscode: \(httpResponse.statusCode)")
            }

            guard let data = data else {
                print("Keine Daten erhalten")
                completion(.failure(URLError(.badServerResponse)))
                return
            }

            print("Antwortdaten (JSON): \(String(data: data, encoding: .utf8) ?? "Keine Daten als String darstellbar")")

            do {
                let player = try JSONDecoder().decode(Player.self, from: data)
                DispatchQueue.main.async {
                    completion(.success(player))
                }
            } catch {
                print("Fehler beim Decodieren: \(error)")
                completion(.failure(error))
            }
        }.resume()
    }

    // Spieler einloggen (POST)
    func loginPlayer(name: String, password: String, completion: @escaping (Result<Player, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/login") else {
            print("Ungültige URL für Login")
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let playerData = ["name": name, "password": password]
        
        do {
            request.httpBody = try JSONEncoder().encode(playerData)
        } catch {
            print("Fehler beim Codieren der Daten: \(error)")
            completion(.failure(error))
            return
        }

        print("Sende Login-Anfrage an \(url)")
        print("Request Body: \(String(data: request.httpBody ?? Data(), encoding: .utf8) ?? "nil")")

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                print("Netzwerkfehler: \(error)")
                completion(.failure(error))
                return
            }

            if let httpResponse = response as? HTTPURLResponse {
                print("Statuscode: \(httpResponse.statusCode)")
            }

            guard let data = data else {
                print("Keine Daten erhalten")
                completion(.failure(URLError(.badServerResponse)))
                return
            }

            print("Antwortdaten (JSON): \(String(data: data, encoding: .utf8) ?? "Keine Daten als String darstellbar")")

            do {
                let player = try JSONDecoder().decode(Player.self, from: data)
                DispatchQueue.main.async {
                    completion(.success(player))
                }
            } catch {
                print("Fehler beim Decodieren: \(error)")
                completion(.failure(error))
            }
        }.resume()
    }

    
    func getPlayer(by id: Int, completion: @escaping (Result<Player, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/get/\(id)") else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.setValue("application/json", forHTTPHeaderField: "Accept")
        
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
