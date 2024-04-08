package com.koleo.search

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleo.common.model.StationKeywordsModel
import com.koleo.common.model.StationModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel(), SearchScreenActions {

    private val _uiState = MutableStateFlow(SearchViewState())
    val uiState: StateFlow<SearchViewState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            val stationsList = searchRepository.getStations()
            val keywordsList = searchRepository.getStationKeywords()
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
                    endDestinationSearchInProgress = false,
                    results = emptyList()
                )
            }
        }
    }

    override fun onEndDestinationClicked() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    startDestinationSearchInProgress = false,
                    endDestinationSearchInProgress = true,
                    results = emptyList()
                )
            }
        }
    }

    override fun onSearchInputChange(input: String) {
        if (input.isBlank()) {
            _uiState.update { currentState ->
                currentState.copy(
                    results = emptyList(),
                    searchMessage = ""
                )
            }
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                _uiState.update { currentState ->
                    delay(500)
                    val matchingKeywordsIds =
                        currentState.keywords.filter {
                            it.keyword.contains(
                                input,
                                ignoreCase = true
                            )
                        }
                            .map { it.stationId }
                    val results =
                        currentState.stations.filter { it.id in matchingKeywordsIds }
                            .sortedByDescending { it.hits }
                    val message = if (results.isEmpty()) "No results found" else ""
                    currentState.copy(
                        results = results,
                        searchMessage = message
                    )
                }
            }
        }

    }

    override fun onKeywordSelected(result: StationModel) {
        searchJob?.cancel()
        _uiState.update { currentState ->
            val startDestination = if (currentState.startDestinationSearchInProgress) {
                currentState.stations.first { it.id == result.id }
            } else currentState.startDestination
            val endDestination = if (currentState.endDestinationSearchInProgress) {
                currentState.stations.first { it.id == result.id }
            } else currentState.endDestination
            val distance = if (startDestination != null && endDestination != null) {
                getDistance(startDestination, endDestination)
            } else currentState.distance
            currentState.copy(
                startDestination = startDestination,
                endDestination = endDestination,
                distance = distance,
                startDestinationSearchInProgress = false,
                endDestinationSearchInProgress = false,
                results = emptyList()
            )
        }
    }

    override fun onClearInput() {
        _uiState.update { currentState ->
            currentState.copy(
                results = emptyList(),
                searchMessage = "",
            )
        }
    }

    override fun onBackFromSearch() {
        _uiState.update { currentState ->
            currentState.copy(
                startDestinationSearchInProgress = false,
                endDestinationSearchInProgress = false,
                results = emptyList(),
                searchMessage = "",
            )
        }
    }

    private fun getDistance(
        startDestination: StationModel,
        endDestination: StationModel,
    ): Double {
        val startPoint = Location(null).apply {
            latitude = startDestination.latitude
            longitude = startDestination.longitude
        }

        val endPoint = Location(null).apply {
            latitude = endDestination.latitude
            longitude = endDestination.longitude
        }
        val distance = BigDecimal.valueOf(startPoint.distanceTo(endPoint).div(1000).toDouble())
        return distance.setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}

data class SearchViewState(
    val startDestination: StationModel? = null,
    val endDestination: StationModel? = null,
    val distance: Double? = null,
    val startDestinationSearchInProgress: Boolean = false,
    val endDestinationSearchInProgress: Boolean = false,
    val keywords: List<StationKeywordsModel> = emptyList(),
    val stations: List<StationModel> = emptyList(),
    val results: List<StationModel> = emptyList(),
    val searchMessage: String = "",
)
