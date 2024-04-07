package com.koleo.koleoapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.koleo.search.SearchView
import com.koleo.search.SearchViewModel
import com.koleo.search.SearchViewState

data object SearchScreenSpec: ScreenSpec {
    override val route: String
        get() = "search"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        SearchDestination()
    }

}

@Composable
fun SearchDestination(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val viewState : State<SearchViewState> = viewModel.uiState.collectAsState()
    SearchView(viewModel, viewState)
}