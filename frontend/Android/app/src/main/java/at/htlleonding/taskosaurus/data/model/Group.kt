package at.htlleonding.taskosaurus.data.model

data class Group(
    val id: Int,
    val name: String,
    val link: String,
    val players: List<Player>
)
