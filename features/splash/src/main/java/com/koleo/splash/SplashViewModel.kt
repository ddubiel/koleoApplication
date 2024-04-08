package com.koleo.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleo.cache.datasource.CacheStationKeywordsDataSource
import com.koleo.cache.datasource.CacheStationsDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val stationsDataSource: CacheStationsDataSource,
    private val stationKeywordsDataSource: CacheStationKeywordsDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashViewState())
    val uiState: StateFlow<SplashViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            stationsDataSource.getStationsFlow()
                .zip(stationKeywordsDataSource.getStationKeywordsFlow()) { stations, keywords ->
                    val areStationsPopulated = stations?.isNotEmpty() ?: false
                    val areKeywordsPopulated = keywords?.isNotEmpty() ?: false
                    _uiState.update { currentState ->
                        currentState.copy(isDataAvailable = areStationsPopulated && areKeywordsPopulated)
                    }
                }.collect()
        }
    }
}

data class SplashViewState(
    val isDataAvailable: Boolean = false
)