package com.svetlana.kuro.notesapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.svetlana.kuro.notesapp.domain.repo.NoteSourceImpl

class MainViewModel : ViewModel() {
    private val repository = NoteSourceImpl()
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    val liveData: LiveData<AppState> = liveDataToObserve

    fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            liveDataToObserve.postValue(AppState.Success(repository.init()))
        }.start()
    }
}