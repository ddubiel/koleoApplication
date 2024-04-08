package com.koleo.koleoapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.koleo.splash.SplashView

data object SplashScreenSpec : ScreenSpec {
    override val route: String = "splash"

    @Composable
    override fun Content(navController: NavController, navBackStackEntry: NavBackStackEntry) {
        SplashDestination(
            navigateToSearch = {
                navController.popBackStack()
                navController.navigate(SearchScreenSpec.route)
            }
        )
    }

}

@Composable
fun SplashDestination(
    navigateToSearch: () -> Unit,
    viewModel: com.koleo.splash.SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = null) {
        viewModel.uiState.collect {
            if (it.isDataAvailable) {
                navigateToSearch()
            }
        }
    }
    SplashView()
}
