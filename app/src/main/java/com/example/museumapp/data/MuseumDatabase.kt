package com.example.tricitytour.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Museum::class], version = 1, exportSchema = false)
abstract class MuseumDatabase: RoomDatabase() {
    abstract fun museumDao(): MuseumDAO

    companion object {
        @Volatile
        private var INSTANCE: MuseumDatabase? = null
        fun getDatabase(context: Context): MuseumDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MuseumDatabase::class.java,
                    "museum_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}