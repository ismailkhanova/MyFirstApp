package com.example.myfirstapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.ExpenseEntity
import com.example.myfirstapp.MainApplication
import com.example.myfirstapp.R
import kotlinx.android.synthetic.main.fragment_expense.view.date_text_view
import kotlinx.android.synthetic.main.item_expense.view.*

//class ExpenseAdapter(private var expenseList: MutableList<Expense> = mutableListOf()) :
//    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {
//
//    fun add(item: Expense) {
//        expenseList.add(item)
//        notifyDataSetChanged()
//    }
//
//    fun add(expenses: List<Expense>) {
//        expenseList = expenses.toMutableList()
//        notifyDataSetChanged()
//
//    }
//
//    class ExpenseViewHolder(val view: View) : RecyclerView.ViewHolder(view)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
//        return ExpenseViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
//        val expense = expenseList[position]
//        holder.view.amount_text_view.text = expense.amount.toString()
//        holder.view.note_text_view.text = expense.note
//        holder.view.date_text_view.text = expense.date
//    }
//
//    override fun getItemCount() = expenseList.size
//}
//

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

        holder.view.delete_button.setOnClickListener {
            val expenseToDelete = expenseList[position]
            val context = holder.view.context

            // Удаление расхода из базы данных
            expenseToDelete.id?.let { id ->
                MainApplication.expenseDao?.deleteExpenses(id)
            }

            // Обновление списка в адаптере
            val entities = MainApplication.expenseDao?.getExpenses()
            val expenses = entities?.map {
                Expense(
                    id = it.id,
                    amount = it.amount,
                    note = it.note,
                    date = it.date
                )
            }
            expenseList = expenses?.toMutableList() ?: mutableListOf()
            notifyDataSetChanged()

            Toast.makeText(context, "Expense deleted", Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return expenseList.size
    }
}



