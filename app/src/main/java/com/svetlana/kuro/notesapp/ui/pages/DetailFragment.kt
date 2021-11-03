package com.svetlana.kuro.notesapp.ui.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.domain.NoteEntity
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        const val NOTE_EXTRA = "NOTE_EXTRA"

        fun newInstance(bundle: Bundle): DetailFragment =
            DetailFragment().apply { arguments = bundle }
    }

    private var note: NoteEntity? = null
    private var title: TextInputEditText? = null
    private var description: TextInputEditText? = null
    private var completed: CheckBox? = null
    private var datePicker: DatePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            note = requireArguments().getParcelable(NOTE_EXTRA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        initView(view)

        if (note != null) {
            populateView()
        }

        return view
    }

    override fun onStop() {
        super.onStop()
        note = collectNoteData()
    }

    private fun collectNoteData(): NoteEntity {
        val title = Objects.requireNonNull(title!!.text).toString()
        val description = Objects.requireNonNull(description!!.text).toString()
        val date = dateFromDatePicker
        val completed = completed!!.isChecked
        return if (note != null) {
            val answer = NoteEntity(title, description, completed, date)
            answer.id = note!!.id
            answer
        } else {
            NoteEntity(title, description, false, date)
        }
    }

    // Получение даты из DatePicker
    private val dateFromDatePicker: Date
        get() {
            val cal = Calendar.getInstance()
            cal[Calendar.YEAR] = datePicker!!.year
            cal[Calendar.MONTH] = datePicker!!.month
            cal[Calendar.DAY_OF_MONTH] = datePicker!!.dayOfMonth
            return cal.time
        }

    private fun initView(view: View) {
        title = view.findViewById(R.id.input_title)
        description = view.findViewById(R.id.input_description)
        datePicker = view.findViewById(R.id.input_date)
        completed = view.findViewById(R.id.completed)
    }

    private fun populateView() {
        title!!.setText(note!!.title)
        description!!.setText(note!!.description)
        completed!!.isChecked = note!!.isCompleted
        note!!.date?.let { initDatePicker(it) }
    }

    // Установка даты в DatePicker
    private fun initDatePicker(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        datePicker!!.init(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH],
            null
        )
    }
}