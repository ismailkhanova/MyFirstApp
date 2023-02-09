package com.example.myfirstapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.ExpenseAdapter
import com.example.myfirstapp.adapters.ExpenseDataStore

class ExpenseListFragment : Fragment() {

    private lateinit var expenseDataStore: ExpenseDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expenseDataStore = ExpenseDataStore(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)

        val expenses = expenseDataStore.getExpenses()
        val expensesRecyclerView = view.findViewById<RecyclerView>(R.id.expenses_recycler_view)
        expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        expensesRecyclerView.adapter = ExpenseAdapter(expenses)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExpenseListFragment()
    }
}