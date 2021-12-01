package com.svetlana.kuro.notesapp.domain.repo

import retrofit2.Call
import retrofit2.http.GET

private const val MOVIE_API_KEY = "b103808f194ba627d6fe2dac1f48685f"

interface MovieDBApi {
    @GET("movie/now_playing?api_key=${MOVIE_API_KEY}&language=en-US&page=1")
    fun loadMovieNowPlaying(): Call<MovieDBEntity>
}