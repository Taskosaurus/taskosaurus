//
//  GameService.swift
//  taskosaurus
//
//  Created by Isabella Baumann on 21.04.25.
//
import Foundation

class GameService {
    let baseURL = "http://localhost:8080"
    
    // Gruppen abrufen – Fehler sauber über completion weitergeben
    func fetchGroups(player: Player, completion: @escaping (Result<[Group], Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/group/getJoinedGroups") else {
            DispatchQueue.main.async {
                completion(.failure(URLError(.badURL)))
            }
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONEncoder().encode(player)
        
        URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                DispatchQueue.main.async {
                    completion(.failure(error))
                }
                return
            }
            guard let data = data else {
                DispatchQueue.main.async {
                    completion(.failure(URLError(.badServerResponse)))
                }
                return
            }
            do {
                let groups = try JSONDecoder().decode([Group].self, from: data)
                DispatchQueue.main.async {
                    completion(.success(groups))
                }
            } catch {
                DispatchQueue.main.async {
                    completion(.failure(error))
                }
            }
        }.resume()
    }

    // Gruppe erstellen – Fehler sauber über completion weitergeben
    func createGroup(player: Player, name: String, completion: @escaping (Result<Void, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/group/create/\(name)") else {
            DispatchQueue.main.async {
                completion(.failure(URLError(.badURL)))
            }
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONEncoder().encode(player)
        
        URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                DispatchQueue.main.async {
                    completion(.failure(error))
                }
                return
            }

            guard let data = data else {
                DispatchQueue.main.async {
                    completion(.failure(URLError(.badServerResponse)))
                }
                return
            }

            do {
                _ = try JSONDecoder().decode(Group.self, from: data)
                DispatchQueue.main.async {
                    completion(.success(()))
                }
            } catch {
                print("Fehler beim Dekodieren der Antwort: \(error.localizedDescription)")
                DispatchQueue.main.async {
                    completion(.failure(error))
                }
            }
        }.resume()
    }

    // Gruppe beitreten – Fehler sauber über completion weitergeben
    func joinGroup(player: Player, group: Group, completion: @escaping (Result<Group, Error>) -> Void) {
        guard let link = group.link, let url = URL(string: "\(baseURL)\(link)") else {
            DispatchQueue.main.async {
                completion(.failure(URLError(.badURL)))
            }
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONEncoder().encode(player)
        
        URLSession.shared.dataTask(with: request) { data, _, error in
            if let error = error {
                DispatchQueue.main.async {
                    completion(.failure(error))
                }
                return
            }

            guard let data = data else {
                DispatchQueue.main.async {
                    completion(.failure(URLError(.badServerResponse)))
                }
                return
            }

            do {
                let updatedGroup = try JSONDecoder().decode(Group.self, from: data)
                DispatchQueue.main.async {
                    completion(.success(updatedGroup))
                }
            } catch {
                DispatchQueue.main.async {
                    completion(.failure(error))
                }
            }
        }.resume()
    }
}
