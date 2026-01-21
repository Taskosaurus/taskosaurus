package at.htlleonding.taskosaurus.data.remote

import at.htlleonding.taskosaurus.data.utitlity.EmulatorHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val BASE_URL = if (EmulatorHelper.isEmulator()) {
        "http://10.0.2.2:8080/api/"  // Emulator
    } else {
        "http://localhost:8080/api/"  // Real device
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "TaskosaurusApp/1.0")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: GroupApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroupApiService::class.java)
    }
}