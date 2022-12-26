package com.andela.currency.ui.converter

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.andela.currency.databinding.FragmentConverterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    private val converterViewModel: ConverterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentConverterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConverterBinding.inflate(layoutInflater)
        binding.viewmodel = converterViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    private fun observe() {
        converterViewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: ConverterState) {
        when (state) {
            is ConverterState.IsLoading -> changeProgressVisibility(state.isLoading)

            is ConverterState.SuccessLogin -> {
                val symbols = arrayListOf<String>()
                symbols.addAll(state.symbols.keys)
                bindViews(symbols)
            }

            is ConverterState.ErrorLoadingSymbols -> Toast.makeText(
                context,
                state.message,
                Toast.LENGTH_SHORT
            ).show()

            else -> {
                Toast.makeText(context, "Unknown Error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeProgressVisibility(visible: Boolean) {
        if (visible) {
            binding.lytContent?.visibility = View.GONE
            binding.progress?.visibility = View.VISIBLE
        } else {
            binding.lytContent?.visibility = View.VISIBLE
            binding.progress?.visibility = View.GONE
        }
    }

    private fun bindViews(symbols: ArrayList<String>) {
        (binding.spFrom as Spinner).apply {
            adapter =
                context?.let { ArrayAdapter(it, R.layout.simple_spinner_item, symbols) }
            onItemSelectedListener = object : OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    converterViewModel.setFrom(pos)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }

        (binding.spTo as Spinner).apply {
            adapter = context?.let { ArrayAdapter(it, R.layout.simple_spinner_item, symbols) }
            onItemSelectedListener = object : OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    converterViewModel.setTo(pos)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }
}