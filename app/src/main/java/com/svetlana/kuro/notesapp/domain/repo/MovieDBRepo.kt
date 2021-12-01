package com.svetlana.kuro.notesapp.domain.repo

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

interface MovieDBRepo {
    @WorkerThread
    @Throws(Throwable::class)
    fun getMovieSync(): MovieDBEntity

    fun getMovie(): LiveData<MovieDBEntity>

    fun getMovieAsync(
        onSuccess: (MovieDBEntity) -> Unit,
        onError: (Throwable) -> Unit
    )
}