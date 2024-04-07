package com.koleo.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleo.cache.room.dao.StationDao
import com.koleo.cache.room.dao.StationKeywordDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val stationsDao: StationDao,
    private val stationsKeywordDao: StationKeywordDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashViewState())
    val uiState: StateFlow<SplashViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val areStationsPopulated = stationsDao.getStations().isNotEmpty()
            val areKeywordsPopulated = stationsKeywordDao.getStations().isNotEmpty()
            _uiState.update { currentState ->
                currentState.copy(isDataAvailable = areStationsPopulated && areKeywordsPopulated)
            }
        }
    }
}

data class SplashViewState(
    val isDataAvailable: Boolean = false
)