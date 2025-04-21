import Foundation

class QuestionService {
    
    let baseURL = "http://localhost:8080/api/question"

    func fetchDailyQuestion(playerId: Int, completion: @escaping (Result<Question, Error>) -> Void) {
            guard let url = URL(string: "\(baseURL)/getDailyQuestion") else { return }
            
            let requestData = ["id": playerId]  
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
    func answerDailyQuestion(selectedPlayer: Player, playerAnswered: Player, question: Question, completion: @escaping (Result<Question, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/answerDailyQuestion") else { return }
        
        let requestData = Answer(playerId: selectedPlayer.id!, answerId: playerAnswered.id!, date: question.date)
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONEncoder().encode(requestData)
        
        
        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                return
            }
            
            // Check the response code and print it
            if let response = response as? HTTPURLResponse {
                print("HTTP Status Code: \(response.statusCode)") // Should be 200
            }
            
            // Check if data is nil or not
            if let data = data {
                // Print the raw data as a string to see what you're getting
                if let jsonString = String(data: data, encoding: .utf8) {
                    print("Raw Data: \(jsonString)")
                } else {
                    print("Failed to convert data to string.")
                }
                
                // Proceed with decoding if data is present
                do {
                    let question = try JSONDecoder().decode(Question.self, from: data)
                    DispatchQueue.main.async {
                        completion(.success(question))
                    }
                } catch {
                    completion(.failure(error))
                }
            } else {
                completion(.failure(URLError(.badServerResponse)))
            }
        }.resume()
    }
}
