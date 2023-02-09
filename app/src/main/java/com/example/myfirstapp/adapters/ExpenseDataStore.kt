package com.example.myfirstapp.adapters

import android.content.Context
import android.content.SharedPreferences
import com.example.myfirstapp.Expense
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExpenseDataStore(private val context: Context) {

    private val expenses = mutableListOf<Expense>()

    fun addExpense(expense: Expense) {
        expenses.add(expense)
        saveExpenses()
    }

    fun getExpenses(): List<Expense> {
        return expenses
    }

    private fun saveExpenses() {
        val sharedPreferences = context.getSharedPreferences("expense_data_store", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(expenses)
        editor.putString("expenses", json)
        editor.apply()
    }

    init {
        val sharedPreferences = context.getSharedPreferences("expense_data_store", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("expenses", null)
        if (json != null) {
            val type = object : TypeToken<List<Expense>>() {}.type
            expenses.addAll(gson.fromJson(json, type))
        }
    }
}



