package com.example.tricitytour.data

import androidx.lifecycle.LiveData

class MuseumRepository(private val museumDAO: MuseumDAO) {
    val allMuseums: LiveData<List<Museum>> = museumDAO.getAllMuseums()

    suspend fun addMuseum(museum: Museum) {
        museumDAO.insert(museum)
    }

}