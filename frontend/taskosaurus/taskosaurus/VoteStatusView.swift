//
//  VoteStatusView.swift
//  taskosaurus
//
//  Created by Isabella Baumann on 07.05.25.
//


import SwiftUI

struct VoteStatusView: View {
    let answeredCount: Int
    let totalCount: Int

    var body: some View {
        Text("\(answeredCount)/\(totalCount)")
            .font(.subheadline)
            .foregroundColor(.gray)
    }
}
