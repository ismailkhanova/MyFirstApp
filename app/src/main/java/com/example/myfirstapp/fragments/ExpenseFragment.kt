package com.example.myfirstapp.fragments

//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.example.myfirstapp.R
//
//
//class ExtensesFragment : Fragment() {
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_extenses, container, false)
//    }
//

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstapp.Expense
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.ExpenseDataStore

class ExpenseFragment : Fragment() {

    private lateinit var expenseDataStore: ExpenseDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        expenseDataStore = ExpenseDataStore(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense, container, false)

        val amountEditText = view.findViewById<EditText>(R.id.amount_edit_text)
        val noteEditText = view.findViewById<EditText>(R.id.note_edit_text)
        val dateTextView = view.findViewById<TextView>(R.id.date_text_view)
        val viewExpensesButton = view.findViewById<Button>(R.id.view_expenses_button)

        viewExpensesButton.setOnClickListener {
            val expenseListFragment = ExpenseListFragment()
            val fragmentTransaction = this@ExpenseFragment.parentFragmentManager.beginTransaction()
            fragmentTransaction?.replace(R.id.fragment_container, expenseListFragment)
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        val saveExpenseButton = view.findViewById<Button>(R.id.save_button)
        saveExpenseButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDouble()
            val note = noteEditText.text.toString()
            val date = dateTextView.text.toString()
            val expense = Expense(amount, note, date)

            expenseDataStore.addExpense(expense)

            Toast.makeText(requireContext(), "Expense saved!", Toast.LENGTH_SHORT).show()

            amountEditText.setText("")
            noteEditText.setText("")
        }

        return view
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ExpenseFragment()
    }
}

