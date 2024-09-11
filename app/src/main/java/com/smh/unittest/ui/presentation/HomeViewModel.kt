package com.smh.unittest.ui.presentation

import android.util.Log
import androidx.compose.ui.util.trace
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smh.unittest.di.IoDispatcher
import com.smh.unittest.domain.CoinModel
import com.smh.unittest.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun fetchCoins() {
        setLoading(true)
        viewModelScope.launch {
            repository.getCoins().tap { data ->
                setLoading(false)
                _uiState.update { it.copy(coins = data) }
            }.tapLeft {
                setLoading(false)
                viewModelScope.launch { _uiEvent.emit(HomeUiEvent.Error("Error")) }
                Log.e("HomeViewModel", "fetchCoins: ${it.message}")
            }
        }
    }

    private fun setLoading(value: Boolean) {
        _uiState.update {
            it.copy(
                loading = value
            )
        }
    }
}

data class HomeUiState(
    val loading: Boolean = false,
    val coins: List<CoinModel> = emptyList()
)

sealed interface HomeUiEvent {
    data class Error(val message: String): HomeUiEvent
}