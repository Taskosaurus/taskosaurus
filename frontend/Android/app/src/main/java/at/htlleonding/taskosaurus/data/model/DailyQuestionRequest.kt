package at.htlleonding.taskosaurus.data.model

data class DailyQuestionRequest(
    val id: Int,
    val name: String,
    val groupId: Int,
    val date: String? = null
)