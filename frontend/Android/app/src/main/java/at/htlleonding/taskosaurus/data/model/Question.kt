package at.htlleonding.taskosaurus.data.model

data class Question(
    val answered: Boolean,
    val date: String,
    val question: String,
    val answers: List<CollectedAnswer>
)