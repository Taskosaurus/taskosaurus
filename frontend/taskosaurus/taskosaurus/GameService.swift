//
//  GameService.swift
//  taskosaurus
//
//  Created by Isabella Baumann on 21.04.25.
//
import Foundation
class GameService {
    let baseURL = "http://localhost:8080"
    
    // Gruppen abrufen
    func fetchGroups(completion: @escaping (Result<[Group], Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/group/list") else { return }
        
        URLSession.shared.dataTask(with: url) { data, _, error in
            if let error = error {
                completion(.failure(error))
                return
            }
            guard let data = data else {
                completion(.failure(URLError(.badServerResponse)))
                return
            }
            do {
                let groups = try JSONDecoder().decode([Group].self, from: data)
                DispatchQueue.main.async {
                    completion(.success(groups))
                }
            } catch {
                completion(.failure(error))
            }
        }.resume()
    }
    
    // Gruppe erstellen (POST)
    func createGroup(name: String, completion: @escaping (Result<Void, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/group/create") else { return }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let group = Group(id: nil, name: name, link: nil, players: [])
        request.httpBody = try? JSONEncoder().encode(group)
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                return
            }
            
            // Debugging der Antwort
            if let data = data {
                if let responseString = String(data: data, encoding: .utf8) {
                    print("Antwort vom Server: \(responseString)")
                }
            }
            
            guard let data = data else {
                completion(.failure(URLError(.badServerResponse)))
                return
            }
            
            do {
                let group = try JSONDecoder().decode(Group.self, from: data)
                DispatchQueue.main.async {
                    completion(.success(()))
                }
            } catch {
                // Verbessere die Fehlerbehandlung, um genauere Informationen zu liefern
                print("Fehler beim Dekodieren der Antwort: \(error.localizedDescription)")
                completion(.failure(error))
            }
        }.resume()
    }
    
    func joinGroup(player: Player, group: Group, completion: @escaping (Result<Group, Error>) -> Void) {
        guard let link = group.link, let url = URL(string: "\(baseURL)\(link)") else { return }
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        request.httpBody = try? JSONEncoder().encode(player)
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            print(response)
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
