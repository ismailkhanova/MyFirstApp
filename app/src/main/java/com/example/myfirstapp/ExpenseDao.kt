package com.example.myfirstapp


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expense_table")
    fun getExpenses(): List<ExpenseEntity>

    @Delete
    fun deleteExpenses(expense: ExpenseEntity)

    @Query("DELETE FROM EXPENSE_TABLE WHERE id =:id")
    fun deleteExpenses (id:Int)
}
