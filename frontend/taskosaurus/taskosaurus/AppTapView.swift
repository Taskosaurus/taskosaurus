//
//  AppTapView.swift
//  taskosaurus
//
//  Created by Isabella Baumann on 21.04.25.
//

// AppTabView.swift
import SwiftUI

// Definiere die 3 Spieltypen als eigenständige Tabs
enum GameTab: String, CaseIterable {
    case truthOrDare = "Wahrheit oder Pflicht"
    case whoWouldRather = "Wer würde eher"
    case kissMarryKill = "Kiss Marry Kill"
    
    var icon: String {
        switch self {
        case .truthOrDare: return "questionmark.circle.fill"
        case .whoWouldRather: return "person.2.fill"
        case .kissMarryKill: return "heart.fill"
        }
    }
}

struct AppTabView: View {
    @State private var selectedTab: GameTab = .whoWouldRather
    @StateObject private var viewModel = ViewModel() // 👈 Zentrales ViewModel für die App

    var body: some View {
        TabView(selection: $selectedTab) {
            // Tab 1: Wahrheit oder Pflicht
            NavigationStack {
                TruthOrDareView()
            }
            .tabItem {
                Label(GameTab.truthOrDare.rawValue, systemImage: GameTab.truthOrDare.icon)
            }
            .tag(GameTab.truthOrDare)
            
            // Tab 2: Wer würde eher
            NavigationStack {
                WhoWouldRatherView(viewModel: viewModel) 
            }
            .tabItem {
                Label(GameTab.whoWouldRather.rawValue, systemImage: GameTab.whoWouldRather.icon)
            }
            .tag(GameTab.whoWouldRather)
            
            // Tab 3: Kiss Marry Kill
            NavigationStack {
                KissMarryKillView()
            }
            .tabItem {
                Label(GameTab.kissMarryKill.rawValue, systemImage: GameTab.kissMarryKill.icon)
            }
            .tag(GameTab.kissMarryKill)
        }
        .tint(.blue)
    }
}

// Platzhalter-Views (für die fehlenden Spiele)
struct TruthOrDareView: View {
    var body: some View {
        Text("Wahrheit oder Pflicht View")
            .navigationTitle("Wahrheit oder Pflicht")
    }
}

struct KissMarryKillView: View {
    var body: some View {
        Text("Kiss Marry Kill View")
            .navigationTitle("Kiss Marry Kill")
    }
}
