package com.car.myfirstapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.car.myfirstapp.*
import com.car.myfirstapp.data.Expense
import com.car.myfirstapp.adapters.ExpenseAdapter
import com.car.myfirstapp.data.ExpenseEntity
import com.car.myfirstapp.data.MainApplication


//class ExpenseFragment : Fragment() {
//
//    private lateinit var expenseAdapter: ExpenseAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_expense, container, false)
//
//
//        val amountEditText = view.findViewById<EditText>(R.id.amount_edit_text)
//        val noteEditText = view.findViewById<EditText>(R.id.note_edit_text)
//        val dateTextView = view.findViewById<TextView>(R.id.date_text_view)
//
//        val saveExpenseButton = view.findViewById<Button>(R.id.save_button)
//        saveExpenseButton.setOnClickListener {
//            val amount = amountEditText.text.toString().toDouble()
//            val note = noteEditText.text.toString()
//            val date = dateTextView.text.toString()
//
//            val expense = Expense(
//                amount = amount,
//                note = note,
//                date = date ?: "",
//            )
//
//            val entity = ExpenseEntity(
//                amount = amount,
//                note = note,
//                date = date ?: "",
//            )
//
//            MainApplication.expenseDao?.addExpense(entity)
//            expenseAdapter.add(expense)
//
//            Toast.makeText(requireContext(), "Expense saved!", Toast.LENGTH_SHORT).show()
//
//            amountEditText.setText("")
//            noteEditText.setText("")
//
//            // Close the current fragment and return to the ExpenseListFragment
//            parentFragmentManager.popBackStack()
//        }
//
//        return view
//    }
//
//    companion object {
//        fun newInstance(expenseAdapter: ExpenseAdapter) = ExpenseFragment().apply {
//            this.expenseAdapter = expenseAdapter
//        }
//    }
//}

import android.app.DatePickerDialog
import com.car.myfirstapp.R
import java.text.SimpleDateFormat
import java.util.*

class ExpenseFragment : Fragment() {

    private lateinit var expenseAdapter: ExpenseAdapter
    private lateinit var selectedDate: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense, container, false)

        val amountEditText = view.findViewById<EditText>(R.id.amount_edit_text)
        val noteEditText = view.findViewById<EditText>(R.id.note_edit_text)
        val dateEditText = view.findViewById<EditText>(R.id.date_edit_text)


        selectedDate = Calendar.getInstance()

        val saveExpenseButton = view.findViewById<Button>(R.id.save_button)
        saveExpenseButton.setOnClickListener {
            val amountStr = amountEditText.text.toString()
            val note = noteEditText.text.toString()
            val date = dateEditText.text.toString()

            if (amountStr.isBlank() || note.isBlank() || date.isBlank()) {
                Toast.makeText(requireContext(), "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show()
            } else {
                val amount = amountStr.toDouble()

                val expense = Expense(
                    amount = amount,
                    note = note,
                    date = date
                )

                val entity = ExpenseEntity(
                    amount = amount,
                    note = note,
                    date = date
                )

                MainApplication.expenseDao?.addExpense(entity)
                expenseAdapter.add(expense)

                Toast.makeText(requireContext(), "Запись сохранена!", Toast.LENGTH_SHORT).show()

                amountEditText.setText("")
                noteEditText.setText("")

                // Close the current fragment and return to the ExpenseListFragment
                parentFragmentManager.popBackStack()
            }
        }

        dateEditText.setOnClickListener {
            showDatePicker()
        }

        return view
    }

    private fun showDatePicker() {
        val currentDate = selectedDate ?: Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                selectedDate = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }
                updateDateText()
            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    private fun updateDateText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(selectedDate.time)

        val dateEditText = requireView().findViewById<EditText>(R.id.date_edit_text)
        dateEditText.setText(dateString)
    }

    companion object {
        fun newInstance(expenseAdapter: ExpenseAdapter) = ExpenseFragment().apply {
            this.expenseAdapter = expenseAdapter
        }
    }
}