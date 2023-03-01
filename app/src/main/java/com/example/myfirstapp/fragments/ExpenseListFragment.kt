package com.example.myfirstapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.ExpenseAdapter
import com.example.myfirstapp.adapters.ExpenseDataStore
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.example.myfirstapp.MainActivity
import com.example.myfirstapp.adapters.ExpenseDataChangeListener


class ExpenseListFragment : Fragment(), ExpenseDataChangeListener {

    private lateinit var expenseDataStore: ExpenseDataStore
    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = requireActivity() as MainActivity
        expenseDataStore = mainActivity.expenseDataStore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)

        expensesRecyclerView = view.findViewById(R.id.expenses_recycler_view)
        expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val expenses = expenseDataStore.getExpenses()
        expenseAdapter = ExpenseAdapter(expenses)
        expensesRecyclerView.adapter = expenseAdapter

        val addExpenseButton = view.findViewById<ImageButton>(R.id.imageButton4)
        addExpenseButton.setOnClickListener {
            val expenseFragment = ExpenseFragment()

            val transaction = parentFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.add(android.R.id.content, expenseFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseDataStore.setListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        expenseDataStore.setListener(null)
    }

    override fun onExpenseDataChanged() {
        val expenses = expenseDataStore.getExpenses()
        expenseAdapter.expenses = expenses
        expenseAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = ExpenseListFragment()
    }
}
