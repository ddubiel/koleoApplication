package com.koleo.search

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleo.cache.room.dao.StationDao
import com.koleo.cache.room.dao.StationKeywordDao
import com.koleo.cache.room.entities.StationEntity
import com.koleo.cache.room.entities.StationKeywordEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val stationsDao: StationDao,
    private val stationsKeywordDao: StationKeywordDao,
) : ViewModel(), SearchScreenActions {

    private val _uiState = MutableStateFlow(SearchViewState())
    val uiState: StateFlow<SearchViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val stationsList = stationsDao.getStations()
            val keywordsList = stationsKeywordDao.getStations()
            _uiState.update { currentState ->
                currentState.copy(
                    keywords = keywordsList,
                    stations = stationsList,
                )
            }
        }
    }

    override fun onStartDestinationClicked() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    startDestinationSearchInProgress = true,
                    endDestinationSearchInProgress = false
                )
            }
        }
    }

    override fun onEndDestinationClicked() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    startDestinationSearchInProgress = false,
                    endDestinationSearchInProgress = true
                )
            }
        }
    }

    override fun onSearchInputChange(input: String) {
        _uiState.update { currentState ->
            val results =
                currentState.keywords.filter { it.keyword.contains(input, ignoreCase = true) }
            currentState.copy(
                keywordsResults = results
            )
        }
    }

    override fun onKeywordSelected(result: StationKeywordEntity) {
        _uiState.update { currentState ->
            val startDestination = if (currentState.startDestinationSearchInProgress) {
                currentState.stations.first { it.id == result.stationId }
            } else currentState.startDestination
            val endDestination = if (currentState.endDestinationSearchInProgress) {
                currentState.stations.first { it.id == result.stationId }
            } else currentState.startDestination
            val distance = if (startDestination != null && endDestination != null) {
                getDistance(startDestination, endDestination)
            } else currentState.distance
            currentState.copy(
                startDestination = startDestination,
                endDestination = endDestination,
                distance = distance,
                startDestinationSearchInProgress = false,
                endDestinationSearchInProgress = false,
                keywordsResults = emptyList()
            )
        }
    }

    private fun getDistance(
        startDestination: StationEntity,
        endDestination: StationEntity
    ): Double {
        val startPoint = Location("").apply {
            latitude = startDestination.latitude
            longitude = startDestination.longitude
        }

        val endPoint = Location("").apply {
            latitude = endDestination.latitude
            longitude = endDestination.longitude
        }

        return String.format("%.1f", startPoint.distanceTo(endPoint) / 1000).toDouble()
    }
}

data class SearchViewState(
    val startDestination: StationEntity? = null,
    val endDestination: StationEntity? = null,
    val distance: Double? = null,
    val startDestinationSearchInProgress: Boolean = false,
    val endDestinationSearchInProgress: Boolean = false,
    val keywords: List<StationKeywordEntity> = emptyList(),
    val stations: List<StationEntity> = emptyList(),
    val keywordsResults: List<StationKeywordEntity> = emptyList(),
)
