package at.htlleonding.taskosaurus.data.remote

import at.htlleonding.taskosaurus.data.utitlity.EmulatorHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    /*
    val BASE_URL = if (EmulatorHelper.isEmulator()) {
        "http://10.0.2.2:8080/api/"  // Emulator
    } else {
        "http://127.0.0.1:8080/api/"  // Real device
    }  */
    val BASE_URL = "https://it210142.cloud.htl-leonding.ac.at/api/"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "TaskosaurusApp/1.0")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val groupApi: GroupApiService by lazy {
        retrofit.create(GroupApiService::class.java)
    }

    val playerApi: PlayerApiService by lazy {
        retrofit.create(PlayerApiService::class.java)
    }

    val questionApi: QuestionApiService by lazy {
        retrofit.create(QuestionApiService::class.java)
    }
}