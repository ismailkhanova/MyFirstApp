package com.example.myfirstapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExpenseEntity::class], version = 1)

abstract class AppDatabase : RoomDatabase() {

    abstract val expenseDao: ExpenseDao


}
