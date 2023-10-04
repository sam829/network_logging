package com.sam.networklogger.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.network_logger.data.source.remote.NetworkLoggingInterceptor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class MainViewModel : ViewModel() {
    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(
            "MainViewModel",
            "error",
            throwable
        )
    }

    private fun getNetworkService(context: Context) = try {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(NetworkLoggingInterceptor(context))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    } catch (throwable: Throwable) {
        Log.e(
            "MainViewModel",
            "error",
            throwable
        )
        null
    }

    fun makeAPICall(context: Context) {
        viewModelScope.launch(handler) { getNetworkService(context)?.getTodos() }
    }

    fun postData(context: Context) {
        viewModelScope.launch(handler) {
            val postData = PostData("Example Title", "This is the post body.")
            getNetworkService(context)?.createPost(postData)
        }
    }
}

interface NetworkService {
    @GET("/todos/somsdfohh")
    suspend fun getTodos(): Response<User>

    @POST("posts")
    suspend fun createPost(@Body postData: PostData): Response<PostData>
}

data class PostData(
    val title: String,
    val body: String
)

typealias User = List<UserElement>

data class UserElement(
    val userID: Long,
    val id: Long,
    val title: String,
    val completed: Boolean
)
