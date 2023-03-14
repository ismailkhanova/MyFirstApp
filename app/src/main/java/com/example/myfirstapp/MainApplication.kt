package com.example.myfirstapp

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            this, AppDatabase::class.java,
            "expenses_db"
        ).allowMainThreadQueries().build()

        expenseDao = db.expenseDao
    }


    companion object {
        var expenseDao: ExpenseDao? = null
    }

}