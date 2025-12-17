//
//  ErrorView.swift
//  taskosaurus
//
//  Created by Kinga on 16.06.25.
//

import SwiftUI

struct ErrorView: View {
    
    @ObservedObject var viewModel: ViewModel
    
    var body: some View {
        VStack(spacing: 16) {
            Spacer()
            if let error = viewModel.errorMessage {
                Text(error)
                    .foregroundColor(.red)
                    .multilineTextAlignment(.center)
                ProgressView("Verbindung wird wiederhergestellt …")
                Spacer()
            
            }
        
        }
        .frame(maxWidth: .infinity)
    }
}


