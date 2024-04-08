package com.koleo.search

import com.koleo.common.model.StationModel

interface SearchScreenActions {
    fun onSearchInputChange(input: String)
    fun onStartDestinationClicked()
    fun onEndDestinationClicked()
    fun onKeywordSelected(result: StationModel)
    fun onClearInput()
    fun onBackFromSearch()
}