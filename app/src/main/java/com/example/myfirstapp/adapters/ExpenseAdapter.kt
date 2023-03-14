package com.example.myfirstapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.Expense
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.fragment_expense.view.date_text_view
import kotlinx.android.synthetic.main.item_expense.view.*

class ExpenseAdapter(private var expenseList: MutableList<Expense> = mutableListOf()) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    fun add(item: Expense) {
        expenseList.add(item)
        notifyDataSetChanged()
    }

    fun add(expenses: List<Expense>) {
        expenseList = expenses.toMutableList()
        notifyDataSetChanged()

    }

    class ExpenseViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.view.amount_text_view.text = expense.amount.toString()
        holder.view.note_text_view.text = expense.note
        holder.view.date_text_view.text = expense.date
    }

    override fun getItemCount() = expenseList.size
}



