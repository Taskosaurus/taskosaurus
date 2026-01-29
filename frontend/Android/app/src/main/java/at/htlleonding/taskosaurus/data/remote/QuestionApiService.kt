package at.htlleonding.taskosaurus.data.remote

import at.htlleonding.taskosaurus.data.model.*
import retrofit2.http.*

interface QuestionApiService {

    @GET("question/list")
    suspend fun getAllQuestions(): List<Question>

    @POST("question/getDailyQuestion")
    suspend fun getDailyQuestion(
        @Body request: DailyQuestionRequest
    ): Question

    @POST("question/answerDailyQuestion")
    suspend fun answerDailyQuestion(
        @Body answer: DailyQuestionAnswer
    ): Question
}