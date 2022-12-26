package com.andela.currency.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currency.domain.common.base.BaseResult
import com.andela.currency.domain.symbols.usecase.GetAllSymbolsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val symbolsUseCase: GetAllSymbolsUseCase,
    private val saveStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val KEY_FROM = "from"
    private val KEY_TO = "to"
    private val KEY_INPUT = "input"
    private val KEY_DEFAULT_FROM = "USD"
    private val KEY_DEFAULT_TO = "PKR"
    private val DEFAULT_INPUT = "1"

    private val state = MutableStateFlow<ConverterState>(ConverterState.Init)
    val mState: StateFlow<ConverterState> get() = state

    private val _convertedValue = MutableLiveData("1")
    val convertedValue = _convertedValue

    init {
        loadSymbols()
    }

    private fun setLoading() {
        state.value = ConverterState.IsLoading(true)
    }

    private fun hideLoading() {
        state.value = ConverterState.IsLoading(false)
    }

    private fun showToast(message: String) {
        state.value = ConverterState.ErrorLoadingSymbols(message)
    }

    fun setFrom(from: Int) {
        saveStateHandle[KEY_FROM] = from
    }

    fun getFrom()=  saveStateHandle.getLiveData<Int>(KEY_FROM) /* LiveData<Int> {
        return saveStateHandle.getLiveData<Int>(KEY_FROM)
    }*/

    fun setTo(from: Int) {
        saveStateHandle[KEY_TO] = from
    }

    fun getTo(): LiveData<Int> {
        return saveStateHandle.getLiveData<Int>(KEY_TO)
    }

    fun setInput(inputValue: String) {
        saveStateHandle[KEY_INPUT] = inputValue
    }

    fun getInput(): LiveData<String> {
        return saveStateHandle.getLiveData<String>(KEY_INPUT)
    }

    private fun loadSymbols() {
        viewModelScope.launch {
            symbolsUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { baseResult ->
                    hideLoading()
                    when (baseResult) {
                        is BaseResult.Error -> state.value =
                            ConverterState.ErrorLoadingSymbols(baseResult.rawResponse.error?.message!!)
                        is BaseResult.Success -> {
                            state.value = ConverterState.SuccessLogin(baseResult.data.symbols)
                            setDefaults(baseResult.data.symbols.keys.indexOf(KEY_DEFAULT_FROM),baseResult.data.symbols.keys.indexOf(KEY_DEFAULT_TO))
                        }

                        else ->
                            ConverterState.ErrorLoadingSymbols("Unknown Error Occurred")
                    }
                }
        }
    }

    private fun setDefaults(from: Int, to: Int) {
        setFrom(from)
        setTo(to)
        setInput(DEFAULT_INPUT)
    }


}

sealed class ConverterState {
    object Init : ConverterState()
    data class IsLoading(val isLoading: Boolean) : ConverterState()
    data class ErrorLoadingSymbols(val message: String) : ConverterState()
    data class SuccessLogin(val symbols: Map<String, String>) : ConverterState()
}