package com.andela.currency.ui.converter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andela.currency.domain.common.base.BaseResult
import com.andela.currency.domain.exchange.usecase.GetAllExchangeRatesUseCase
import com.andela.currency.domain.symbols.usecase.GetAllSymbolsUseCase
import com.andela.currency.util.tryGetDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val symbolsUseCase: GetAllSymbolsUseCase,
    private val exchangeRatesUseCase: GetAllExchangeRatesUseCase,
    private val saveStateHandle: SavedStateHandle
) : ViewModel() {
    private val KEY_FROM = "from"
    private val KEY_TO = "to"
    private val KEY_INPUT = "input"
    private val KEY_DEFAULT_FROM = "USD"
    private val KEY_DEFAULT_TO = "PKR"
    private val DEFAULT_INPUT = "1"
    private val _state = MutableStateFlow<ConverterState>(ConverterState.Init)
    val state = _state

    private val _symbols = MutableStateFlow<Map<String, String>>(mapOf())
    val symbols = _symbols

    private val _exchangeRatesState = MutableStateFlow<Map<String, Double>>(mapOf())
    val exchangeRatesState = _exchangeRatesState

    private val _convertedValue = MutableLiveData("1")
    val convertedValue = _convertedValue

    init {
        setInput(DEFAULT_INPUT)
        getAllSymbols()
    }

    private fun setLoading() {
        _state.value = ConverterState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = ConverterState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _state.value = ConverterState.ErrorLoading(message)
    }

    fun setFrom(from: Int) {
        saveStateHandle[KEY_FROM] = from
        convertValue()
    }

    fun getFrom() = saveStateHandle.getLiveData<Int>(KEY_FROM)

    fun setTo(from: Int) {
        saveStateHandle[KEY_TO] = from
        convertValue()
    }

    fun getTo() = saveStateHandle.getLiveData<Int>(KEY_TO)

    fun setInput(inputValue: String) {
        saveStateHandle[KEY_INPUT] = inputValue
    }

    fun getInput(): LiveData<String> {
        return saveStateHandle.getLiveData<String>(KEY_INPUT)
    }

    private fun getAllExchangeRates() {
        viewModelScope.launch {
            exchangeRatesUseCase.invoke(KEY_DEFAULT_FROM).onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                showToast(exception.message.toString())
            }.collect { result ->
                hideLoading()

                when (result) {
                    is BaseResult.Error -> _state.value =
                        ConverterState.ErrorLoading(result.rawResponse.error?.message!!)

                    is BaseResult.Success -> {
                        _exchangeRatesState.value = result.data.rates
                        _state.value = ConverterState.SuccessLoading(true)
                        setInput(DEFAULT_INPUT)
                    }

                    else -> ConverterState.ErrorLoading("Unknown Error Occurred")
                }
            }
        }
    }

    fun convertValue() {
        val exchanges = _exchangeRatesState.value
        val symbolsMap = _symbols.value
        if (_exchangeRatesState.value.isNotEmpty()) {

            val from = symbolsMap.keys.toList()[getFrom().value!!]
            from.let {
                val to = symbolsMap.keys.toList()[getTo().value!!]
                val input = getInput().value?.tryGetDouble(1.0)
                to.let {
                    if (exchanges[to] != null && exchanges[from] != null) {
                        _convertedValue.value =
                            (input!! * exchanges[to]!! / exchanges[from]!!).toString()
                    }
                }
            }
        }

    }

    fun swapValues() {
        getFrom().value.let {
            val from = it
            setFrom(getTo().value!!)
            setTo(from!!)
        }
    }

    private fun getAllSymbols() {
        viewModelScope.launch {
            symbolsUseCase.invoke().onStart {
                setLoading()
            }.catch { exception ->
                hideLoading()
                showToast(exception.message.toString())
            }.collect { baseResult ->
                hideLoading()
                when (baseResult) {

                    is BaseResult.Error -> _state.value =
                        ConverterState.ErrorLoading(baseResult.rawResponse.error?.message!!)

                    is BaseResult.Success -> {
                        getAllExchangeRates()
                        _state.value = ConverterState.SuccessLoading(true)
                        symbols.value = baseResult.data.symbols
                        setDefaults(
                            baseResult.data.symbols.keys.indexOf(KEY_DEFAULT_FROM),
                            baseResult.data.symbols.keys.indexOf(KEY_DEFAULT_TO)
                        )
                    }

                    else -> ConverterState.ErrorLoading("Unknown Error Occurred")
                }
            }
        }
    }

    private fun setDefaults(from: Int, to: Int) {
        setFrom(from)
        setTo(to)
    }
}

sealed class ConverterState {
    object Init : ConverterState()
    data class IsLoading(val isLoading: Boolean) : ConverterState()
    data class ErrorLoading(val message: String) : ConverterState()
    data class SuccessLoading(val success: Boolean) : ConverterState()
}