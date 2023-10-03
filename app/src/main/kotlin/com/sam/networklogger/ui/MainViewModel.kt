package com.sam.networklogger.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.network_logger.data.source.remote.NetworkLoggingInterceptor
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class MainViewModel : ViewModel() {
    fun makeAPICall(context: Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(NetworkLoggingInterceptor(context))
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val networkService = retrofit.create(NetworkService::class.java)
        viewModelScope.launch {
            val response = networkService.getTodos()
//            Log.e("response", "${response.body()}")
        }
    }
}

interface NetworkService {
    @GET("todos/")
    suspend fun getTodos(): Response<User>
}

typealias User = List<UserElement>

data class UserElement(
    val userID: Long,
    val id: Long,
    val title: String,
    val completed: Boolean
)
