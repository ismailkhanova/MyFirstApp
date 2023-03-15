package com.example.myfirstapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapters.ExpenseAdapter
import androidx.fragment.app.FragmentTransaction
import com.example.myfirstapp.*
import com.example.myfirstapp.adapters.Expense

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

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseAdapter = ExpenseAdapter()
        initViews()
        initList()

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

//class ExpenseListFragment : Fragment() {
//
//    private lateinit var expensesRecyclerView: RecyclerView
//    private lateinit var expenseAdapter: ExpenseAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val mainActivity = requireActivity() as MainActivity
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)
//
//        expensesRecyclerView = view.findViewById(R.id.expenses_recycler_view)
//
//
//
//
//        val addExpenseButton = view.findViewById<ImageButton>(R.id.imageButton4)
//        addExpenseButton.setOnClickListener {
//            val expenseFragment = ExpenseFragment()
//
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            transaction.add(android.R.id.content, expenseFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }
//
//        return view
//    }
//
//    fun addExpense(expense: Expense) {
//        expenseAdapter.add(expense)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        expenseAdapter = ExpenseAdapter()
//        initViews()
//        initList()
//
//    }
//
//    private fun initViews() {
//        expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        expensesRecyclerView.adapter = expenseAdapter
//    }
//
//    private fun initList() {
//        val entities = MainApplication.expenseDao?.getExpenses()
//        val expenses = entities?.map {
//            Expense(
//                amount = it.amount,
//                note = it.note,
//                date = it.date
//            )
//        }
//        expenses?.let {
//            expenseAdapter.add(it)
//            expenseAdapter.notifyDataSetChanged()
//        }
//
//    }
//
//    companion object {
//        fun newInstance() = ExpenseListFragment()
//    }
//}

