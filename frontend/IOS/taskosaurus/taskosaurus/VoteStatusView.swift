//
//  VoteStatusView.swift
//  taskosaurus
//
//  Created by Isabella Baumann on 07.05.25.
//


import SwiftUI

struct VoteStatusView: View {
    let answers: [CollectedAnswer]
    let totalCount: Int

    var body: some View {
        let answeredCount = answers.reduce(0){$0 + $1.count}
        Text("\(answeredCount)/\(totalCount)")
            .font(.subheadline)
            .foregroundColor(.gray)
    }
}
