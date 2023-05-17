package com.example.myfirstapp.fragments


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapters.ExpenseAdapter
import androidx.fragment.app.FragmentTransaction
import com.example.myfirstapp.*
import com.example.myfirstapp.data.Expense
import com.example.myfirstapp.data.MainApplication

class ExpenseListFragment : Fragment() {

    private lateinit var expensesRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)

        expensesRecyclerView = view.findViewById(R.id.expenses_recycler_view)

        val addExpenseButton = view.findViewById<ImageButton>(R.id.imageButton4)
        addExpenseButton.setOnClickListener {
            val expenseFragment = ExpenseFragment.newInstance(expenseAdapter)

            val transaction = parentFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction.add(android.R.id.content, expenseFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val showTotalButton = view.findViewById<Button>(R.id.show_total_button)
        showTotalButton.setOnClickListener {
            showTotalExpenseDialog()
        }

        return view
    }

    private fun showTotalExpenseDialog() {
        val totalExpense = MainApplication.expenseDao?.getTotalExpense()

        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Total Expense")
            .setMessage("Total Expense: $totalExpense")
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseAdapter = ExpenseAdapter()
        initViews()
//        initList()

    }

    override fun onResume() {
        super.onResume()
        initList()
    }

    private fun initViews() {
        expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        expensesRecyclerView.adapter = expenseAdapter
    }

    private fun initList() {
        val entities = MainApplication.expenseDao?.getExpenses()
        val expenses = entities?.map {
            Expense(
                id = it.id,
                amount = it.amount,
                note = it.note,
                date = it.date
            )
        }
        expenses?.let {
            expenseAdapter.add(it)
        }
    }

    companion object {
        fun newInstance() = ExpenseListFragment()
    }
}

