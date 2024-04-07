package com.koleo.koleoapplication.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koleo.koleoapplication.navigation.ScreenSpec
import com.koleo.koleoapplication.navigation.SplashScreenSpec

@Composable
fun KoleoApp(navController: NavHostController = rememberNavController()) {
    AppNavHost(navController)
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SplashScreenSpec.route) {
        ScreenSpec.allScreens.forEach { screen ->
            composable(
                screen.route,
                screen.arguments,
                screen.deepLinks
            ) {
                screen.Content(navController = navController, navBackStackEntry = it)
            }
        }
    }
}
