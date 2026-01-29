package at.htlleonding.taskosaurus.data.remote

import at.htlleonding.taskosaurus.data.model.Group
import at.htlleonding.taskosaurus.data.model.Player
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupApiService {
    @GET("group/list")
    suspend fun getGroups(): List<Group>

    @POST("group/create/{name}")
    suspend fun createGroup(
        @Path("name") name: String,
        @Body player: Player
    ): Group

    @GET("group/join/{id}/{playerName}")
    suspend fun joinGroup(
        @Path("id") groupId: Int,
        @Path("playerName") playerName: String
    ): Player

    @POST("group/getJoinedGroups")
    suspend fun getJoinedGroups(
        @Body player: Player
    ): List<Group>


}