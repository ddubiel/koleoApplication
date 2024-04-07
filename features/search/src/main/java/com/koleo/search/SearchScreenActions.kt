package com.koleo.search

import com.koleo.cache.room.entities.StationKeywordEntity

interface SearchScreenActions {
    fun onSearchInputChange(input: String)
    fun onStartDestinationClicked()
    fun onEndDestinationClicked()
    fun onKeywordSelected(result: StationKeywordEntity)
}