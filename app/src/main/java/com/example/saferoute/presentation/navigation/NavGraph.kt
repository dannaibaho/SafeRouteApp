package com.example.saferoute.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.saferoute.presentation.splash.SplashScreen
import com.example.saferoute.presentation.walkthrough.WalkthroughScreen
import com.example.saferoute.presentation.auth.LoginScreen
import com.example.saferoute.presentation.auth.RegisterScreen
import com.example.saferoute.presentation.main.MainScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Walkthrough.route) {
            WalkthroughScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Home.route) {
            MainScreen(navController = navController)
        }
    }
}

