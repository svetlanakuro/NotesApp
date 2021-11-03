package com.svetlana.kuro.notesapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.svetlana.kuro.notesapp.domain.repo.RepositoryImpl
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val repository = RepositoryImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    val liveData: LiveData<AppState> = liveDataToObserve

    fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.Success(repository.getNoteFromLocalStorage()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("Note not found")))
            }
        }.start()
    }
}