package at.htlleonding.taskosaurus.data.remote

import at.htlleonding.taskosaurus.data.model.*
import retrofit2.http.*

interface PlayerApiService {

    @GET("player/list")
    suspend fun getAllPlayers(): List<Player>

    @POST("player/create")
    suspend fun createPlayer(
        @Body playerName: PlayerNameDto
    ): Player

    @GET("player/get/{id}")
    suspend fun getPlayerById(
        @Path("id") id: Int
    ): Player
}