package com.example.tricitytour.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MuseumViewModel(application: Application): AndroidViewModel(application) {

    private val repository: MuseumRepository
    private val allMuseums: LiveData<List<Museum>>

    init {
        val museumDao = MuseumDatabase.getDatabase(application).museumDao()
        repository = MuseumRepository(museumDao)
        allMuseums = repository.allMuseums
    }

    fun addMuseum(museum: Museum) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMuseum(museum)
        }
    }

}