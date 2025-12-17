//
//  PlayerListView.swift
//  taskosaurus
//
//  Created by Isabella Baumann on 06.04.25.
//

import SwiftUI

struct PlayerListView: View {
    var group: Group
    @ObservedObject var viewModel: ViewModel
    var body: some View {
            if let players = group.players, !players.isEmpty {
                ScrollView {
                    VStack(spacing: 10) {
                        ForEach(players) { player in
                            HStack {
                                Circle()
                                    .fill(Color.blue.opacity(0.7))
                                    .frame(width: 40, height: 40)
                                    .overlay(Text(player.name.prefix(1))
                                        .font(.headline)
                                        .foregroundColor(.white)
                                    )
                                
                                Text(player.name)
                                    .font(.body)
                                    .foregroundColor(.primary)
                                    .padding(.leading, 10)
                                
                                Spacer()
                            }
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(Color(.systemGray6))
                            .cornerRadius(10)
                            .shadow(radius: 2)
                        }
                    }
                    .padding(.horizontal)
                }
            } else {
                Text("Keine Mitglieder vorhanden")
                    .foregroundColor(.gray)
                    .padding()
            }
            
        }
    }
