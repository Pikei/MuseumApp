package com.example.museumapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val ALL = "all"
    val VISITED = "visited"
    val UNVISITED = "unvisited"
    private val names = listOf(
        "Muzeum II Wojny Światowej w Gdańsku",
        "Muzeum Poczty Polskiej w Gdańsku",
        "Europejskie Centrum Solidarności w Gdańsku",
        "Muzeum Emigracji w Gdyni",
        "Ośrodek Kultury Morskiej w Gdańsku",
        "Muzeum Archeologiczne w Gdańsku",
        "Muzeum Bursztynu",
        "Muzeum Narodowe w Gdańsku",
        "Muzeum Gdańska - Ratusz Głównego Miasta",
        "Muzeum Miasta Gdyni",
        "Muzeum Sopotu",
        "Twierdza Wisłoujście"
    )
    private val addresses = listOf(
        "plac Władysława Bartoszewskiego 1, 80-862 Gdańsk",
        "plac Obrońców Poczty Polskiej 1/2, 80-800 Gdańsk",
        "pI. Solidarności 1, 80-863 Gdańsk",
        "Polska 1, 81-339 Gdynia",
        "Tokarska 21/25, 80-888 Gdańsk",
        "Mariacka 25/26, 80-833 Gdańsk",
        "Wielkie Młyny 16, 80-849 Gdańsk",
        "Toruńska 1, 80-822 Gdańsk",
        "Długa 46, 80-831 Gdańsk",
        "Zawiszy Czarnego 1, 81-374 Gdynia",
        "Księcia Józefa Poniatowskiego 8, 81-724 Sopot",
        "Stara Twierdza 1, 80-551 Gdańsk"
    )
    private val themes = listOf(
        "II World War, history",
        "II World War, history",
        "history",
        "history, culture",
        "history, sea",
        "archeology, prehistory, history",
        "archeology, prehistory, history",
        "history, culture, art",
        "history, culture, art",
        "history, culture",
        "history, culture",
        "history"
    )
    private val longitudes = listOf(
        18.659937,
        18.656687,
        18.649687,
        18.547937,
        18.657438,
        18.656438,
        18.649938,
        18.646937,
        18.652687,
        18.547187,
        18.576062,
        18.679938
    )
    private val latitudes = listOf(
        54.356063,
        54.355062,
        54.361312,
        54.533062,
        54.351062,
        54.349312,
        54.353938,
        54.345437,
        54.348813,
        54.516188,
        54.439937,
        54.395688
    )

    private val imageNames = listOf(
        "muzeum_ii_wojny_swiatowej_w_gdansku",
        "muzeum_poczty_polskiej",
        "ecs",
        "muzeum_emigracji",
        "okm",
        "muzeum_archeologiczne",
        "muzeum_bursztynu",
        "muzeum_narodowe_w_gdansku",
        "ratusz_gdansk",
        "muzeum_gdyni",
        "muzeum_sopotu",
        "twierdza_wisloujscie"
    )

    companion object {
        private const val DATABASE_NAME = "museum.db"
        private const val DATABASE_VERSION = 2
        private const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS museums " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, address TEXT, theme TEXT, " +
                "longitude REAL, latitude REAL, " +
                "seen INTEGER, visitedDate TEXT, imgName TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS museums")
        onCreate(db!!)
    }

    fun init() {
        val db = this.writableDatabase
        val containsData = db.rawQuery("SELECT COUNT(*) FROM museums", null).use {
            it.moveToFirst()
            it.getInt(0)
        }
        if (containsData == 0) {
            insertData(db)
        }
        db.close()
    }

    fun getMuseums(s: String): MutableList<Map<String, String>> {
        val db = this.readableDatabase
        val result = mutableListOf<Map<String, String>>()
        val cursor = when (s) {
            "all" -> allMuseumsCursor(db)
            "visited" -> visitedMuseumsCursor(db)
            "unvisited" -> unvisitedMuseumsCursor(db)
            else -> null
        }

        cursor?.use {
            while (it.moveToNext()) {
                val record = mapOf(
                    "name" to it.getString(it.getColumnIndexOrThrow("name")),
                    "address" to it.getString(it.getColumnIndexOrThrow("address")),
                    "theme" to it.getString(it.getColumnIndexOrThrow("theme"))
                )
                result.add(record)
            }
        }
        db.close()
        cursor?.close()
        return result
    }

    fun setMuseumAsUnvisited(name: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("seen", 0)
        values.put("visitedDate", "")
        db.update("museums", values, "name = ?", arrayOf(name))
    }

    fun getDetails(name: String): Map<String, String> {
        val db = this.readableDatabase
        val result = mutableMapOf<String, String>()
        val cursor = db.query("museums", null, "name = ?", arrayOf(name), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                result["name"] = it.getString(it.getColumnIndexOrThrow("name"))
                result["address"] = it.getString(it.getColumnIndexOrThrow("address"))
                result["theme"] = it.getString(it.getColumnIndexOrThrow("theme"))
                result["longitude"] = it.getString(it.getColumnIndexOrThrow("longitude"))
                result["latitude"] = it.getString(it.getColumnIndexOrThrow("latitude"))
                result["seen"] = it.getString(it.getColumnIndexOrThrow("seen"))
                result["visitedDate"] = it.getString(it.getColumnIndexOrThrow("visitedDate"))
            }
        }
        db.close()
        cursor?.close()
        return result
    }


    private fun allMuseumsCursor(db: SQLiteDatabase): Cursor? {
        return db.query(
            "museums",
            arrayOf("name", "address", "theme"),
            null,
            null,
            null,
            null,
            "name"
        )
    }

    private fun visitedMuseumsCursor(db: SQLiteDatabase): Cursor? {
        return db.query(
            "museums",
            arrayOf("name", "address", "theme"),
            "seen = 1",
            null,
            null,
            null,
            "name"
        )
    }

    private fun unvisitedMuseumsCursor(db: SQLiteDatabase): Cursor? {
        return db.query(
            "museums",
            arrayOf("name", "address", "theme"),
            "seen = 0",
            null,
            null,
            null,
            "name"
        )
    }


    private fun insertData(db: SQLiteDatabase) {
        for (i in names.indices) {
            val values = ContentValues()
            values.put("name", names[i])
            values.put("address", addresses[i])
            values.put("theme", themes[i])
            values.put("longitude", longitudes[i])
            values.put("latitude", latitudes[i])
            values.put("seen", 0)
            values.put("visitedDate", "")
            values.put("imgName", imageNames[i])
            db.insert("museums", null, values)
        }
    }

    fun getVisitedDate(name: String): String {
        val db = this.readableDatabase
        val cursor = db.query(
            "museums",
            arrayOf("visitedDate"),
            "name = ?",
            arrayOf(name),
            null,
            null,
            null)
        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndexOrThrow("visitedDate"))
            }
        }
        db.close()
        cursor?.close()
        return ""
    }

    fun setMuseumAsVisitedWithDate(museumName: String, date: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("seen", 1)
            put("visitedDate", date)
        }
        db.update("museums", contentValues, "name = ?", arrayOf(museumName))
    }

    fun getImgName(name: String): String {
        val db = this.readableDatabase
        val cursor = db.query(
            "museums",
            arrayOf("imgName"),
            "name = ?",
            arrayOf(name),
            null,
            null,
            null)
        cursor?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndexOrThrow("imgName"))
            }
        }
        db.close()
        cursor?.close()
        return ""
    }
}