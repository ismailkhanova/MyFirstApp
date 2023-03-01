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
import com.example.myfirstapp.Expense
import com.example.myfirstapp.MainActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.ExpenseDataChangeListener
import com.example.myfirstapp.adapters.ExpenseDataStore

class ExpenseFragment : Fragment() {

    private lateinit var expenseDataStore: ExpenseDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity = requireActivity() as MainActivity
        expenseDataStore = mainActivity.expenseDataStore
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
            val expense = Expense(amount, note, date)

            expenseDataStore.addExpense(expense)

            Toast.makeText(requireContext(), "Expense saved!", Toast.LENGTH_SHORT).show()

            amountEditText.setText("")
            noteEditText.setText("")

            // Close the current fragment and return to the ExpenseListFragment
            parentFragmentManager.popBackStack()
        }

        return view
    }

    companion object {
        fun newInstance() = ExpenseFragment()
    }
}
