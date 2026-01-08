package at.htlleonding.taskosaurus.data.remote

import at.htlleonding.taskosaurus.data.model.Group
import retrofit2.http.GET
import retrofit2.http.Headers

interface GroupApiService {
    @GET("group/list")
    suspend fun getGroups(): List<Group>
}