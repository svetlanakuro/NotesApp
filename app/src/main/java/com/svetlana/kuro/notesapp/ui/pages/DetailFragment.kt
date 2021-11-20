package com.svetlana.kuro.notesapp.ui.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.databinding.DetailFragmentBinding
import com.svetlana.kuro.notesapp.domain.NoteEntity
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        const val NOTE_EXTRA = "NOTE_EXTRA"

        fun newInstance(bundle: Bundle): DetailFragment =
            DetailFragment().apply { arguments = bundle }
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private var note: NoteEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            note = requireArguments().getParcelable(NOTE_EXTRA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        _binding = DetailFragmentBinding.bind(view)

        if (note != null) {
            populateView()
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        note = collectNoteData()
    }

    private fun collectNoteData(): NoteEntity {
        val title = Objects.requireNonNull(binding.inputTitle.text).toString()
        val description = Objects.requireNonNull(binding.inputDescription.text).toString()
        val date = dateFromDatePicker
        val completed = binding.inputCompleted.isChecked
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
            cal[Calendar.YEAR] = binding.inputDate.year
            cal[Calendar.MONTH] = binding.inputDate.month
            cal[Calendar.DAY_OF_MONTH] = binding.inputDate.dayOfMonth
            return cal.time
        }


    private fun populateView() {
        binding.inputTitle.setText(note!!.title)
        binding.inputDescription.setText(note!!.description)
        binding.inputCompleted.isChecked = note!!.isCompleted
        note!!.date?.let { initDatePicker(it) }
    }

    // Установка даты в DatePicker
    private fun initDatePicker(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        binding.inputDate.init(
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH],
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}