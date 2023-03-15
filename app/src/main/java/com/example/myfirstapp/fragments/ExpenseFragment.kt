package com.example.myfirstapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myfirstapp.*
import com.example.myfirstapp.adapters.Expense
import com.example.myfirstapp.adapters.ExpenseAdapter


class ExpenseFragment : Fragment() {

    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense, container, false)


        val amountEditText = view.findViewById<EditText>(R.id.amount_edit_text)
        val noteEditText = view.findViewById<EditText>(R.id.note_edit_text)
        val dateTextView = view.findViewById<TextView>(R.id.date_text_view)

        val saveExpenseButton = view.findViewById<Button>(R.id.save_button)
        saveExpenseButton.setOnClickListener {
            val amount = amountEditText.text.toString().toDouble()
            val note = noteEditText.text.toString()
            val date = dateTextView.text.toString()

            val expense = Expense(
                amount = amount,
                note = note,
                date = date ?: "",
            )

            val entity = ExpenseEntity(
                amount = amount,
                note = note,
                date = date ?: "",
            )

            MainApplication.expenseDao?.addExpense(entity)
            expenseAdapter.add(expense)

            Toast.makeText(requireContext(), "Expense saved!", Toast.LENGTH_SHORT).show()

            amountEditText.setText("")
            noteEditText.setText("")

            // Close the current fragment and return to the ExpenseListFragment
            parentFragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        fun newInstance(expenseAdapter: ExpenseAdapter) = ExpenseFragment().apply {
            this.expenseAdapter = expenseAdapter
        }
    }
}