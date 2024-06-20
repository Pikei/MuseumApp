package com.example.tricitytour.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MuseumDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(museum: Museum)

    @Query("SELECT name, address, theme, avgVisitingHours FROM museums 'false' ORDER BY name ASC")
    fun getAllMuseums(): LiveData<List<Museum>>

    @Query("SELECT name, address, theme, avgVisitingHours FROM museums WHERE seen = 'false' ORDER BY name ASC")
    fun getUnseenMuseums(): LiveData<List<Museum>>

    @Query("SELECT name, address, theme, avgVisitingHours FROM museums WHERE seen = 'true' ORDER BY name ASC")
    fun getSeenMuseums(): LiveData<List<Museum>>

    @Query("SELECT name, address, theme, avgVisitingHours FROM museums WHERE theme LIKE :theme ORDER BY name ASC")
    //TODO: format theme to be case insensitive and before calling method change theme to "%theme%"
    fun getMuseumByTheme(theme: String): LiveData<List<Museum>>

    @Query("SELECT name, address, theme, avgVisitingHours FROM museums WHERE avgVisitingHours BETWEEN :minTime AND :maxTime ORDER BY name ASC")
    fun getMuseumByTime(minTime: Double, maxTime: Double): LiveData<List<Museum>>

    @Query("SELECT name, address, theme, avgVisitingHours FROM museums WHERE name LIKE :name ORDER BY name ASC")
    fun getMuseumByName(name: String): LiveData<List<Museum>>

    @Query("UPDATE museums SET seen = 'false' WHERE id = :id")
    fun markAsUnseen(id: Int)

    @Query("UPDATE museums SET seen = 'true' WHERE id = :id")
    fun markAsSeen(id: Int)
}