package at.htlleonding.taskosaurus.data.model

import java.time.LocalDate

data class Question(
    val answered: Boolean,
    val date: String,  // Changed from LocalDate to String to match JSON response
    val question: String,
    val shortenedQuestion: String,
    val currentLeader: String?,
    val answers: List<Answer>
)

data class Answer(
    val answeredId: Int,
    val answeredName: String,
    val count: Int
)