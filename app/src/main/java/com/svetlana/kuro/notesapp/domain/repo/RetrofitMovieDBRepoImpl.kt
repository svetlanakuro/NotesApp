package com.svetlana.kuro.notesapp.domain.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/3/"

class RetrofitMovieDBRepoImpl : MovieDBRepo {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var api: MovieDBApi = retrofit.create(MovieDBApi::class.java)

    override fun getMovieSync(): MovieDBEntity {
        return api.loadMovieNowPlaying().execute().body() ?: MovieDBEntity()
    }

    override fun getMovie(): LiveData<MovieDBEntity> {
        val liveData: MutableLiveData<MovieDBEntity> = MutableLiveData()

        api.loadMovieNowPlaying().enqueue(object : Callback<MovieDBEntity> {
            override fun onResponse(
                call: Call<MovieDBEntity>,
                response: Response<MovieDBEntity>
            ) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    liveData.postValue(body)
                }
            }

            override fun onFailure(call: Call<MovieDBEntity>, t: Throwable) {
                throw Exception(t)
            }

        })
        return liveData
    }

    override fun getMovieAsync(
        onSuccess: (MovieDBEntity) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        api.loadMovieNowPlaying().enqueue(object : Callback<MovieDBEntity> {
            override fun onResponse(
                call: Call<MovieDBEntity>,
                response: Response<MovieDBEntity>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: throw IllegalStateException("Null result"))
                } else {
                    onError(Throwable("Unknown error"))
                }
            }

            override fun onFailure(call: Call<MovieDBEntity>, t: Throwable) {
                onError(t)
            }
        })
    }
}