import Foundation

class NetworkService {
    
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
                // Hier versuchst du, die Antwort zu dekodieren
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


    // Spieler erstellen (POST)
    func createPlayer(name: String, completion: @escaping (Result<Player, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/player/create") else { return }
        
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
    
    func joinGroup(player: Player, group: Group, completion: @escaping (Result<Void, Error>) -> Void) {
        if var link = group.link {
            guard let url = URL(string: "\(baseURL)/\(link)") else { return }
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
            
            request.httpBody = try? JSONEncoder().encode(player)
            
            URLSession.shared.dataTask(with: request) { _, response, error in
                DispatchQueue.main.async {
                    if let error = error {
                        completion(.failure(error))
                    } else {
                        print(response)
                        completion(.success(()))
                    }
                }
            }.resume()
        }
    }
    func fetchDailyQuestion(playerId: Int, completion: @escaping (Result<Question, Error>) -> Void) {
            guard let url = URL(string: "\(baseURL)/api/question/getDailyQuestion") else { return }
            
            let requestData = ["id": playerId]  // Hier wird die Player ID übergeben
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
            request.httpBody = try? JSONEncoder().encode(requestData)
            
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
                    let question = try JSONDecoder().decode(Question.self, from: data)
                    DispatchQueue.main.async {
                        completion(.success(question))
                    }
                } catch {
                    completion(.failure(error))
                }
            }.resume()
        }
}
