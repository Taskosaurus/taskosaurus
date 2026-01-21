package at.htlleonding.taskosaurus.data.model

data class DailyQuestionAnswer(
    val playerId: Int,
    val answerId: Int,
    val groupId: Int,
    val date: String? = null
)

