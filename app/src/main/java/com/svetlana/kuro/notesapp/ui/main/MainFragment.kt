package com.svetlana.kuro.notesapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.svetlana.kuro.notesapp.R
import com.svetlana.kuro.notesapp.databinding.MainFragmentBinding
import com.svetlana.kuro.notesapp.domain.repo.RepositoryImpl
import com.svetlana.kuro.notesapp.ui.showSnackBar

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private val adapter: ItemAdapter by lazy { ItemAdapter() }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        _binding = MainFragmentBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner, { state -> renderData(state) })

        viewModel.getDataFromLocalSource()

    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                binding.recyclerView.layoutManager = GridLayoutManager(this.context, 2)
                binding.recyclerView.adapter = adapter
                adapter.noteData = RepositoryImpl().getNoteFromLocalStorage()

            }
            is AppState.Error -> {
                binding.mainFragment.showSnackBar(
                    "Error: ${state.error}",
                    "Reload",
                    { viewModel.getDataFromLocalSource() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}