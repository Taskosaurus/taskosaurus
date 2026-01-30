package at.htlleonding.taskosaurus.data.model

data class Player(
    val id: Int,
    val name: String
)
data class PlayerNameDto(
    val name: String,
    val password: String
)