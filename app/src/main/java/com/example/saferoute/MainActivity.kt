package com.example.saferoute

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.saferoute.presentation.navigation.NavGraph
import com.example.saferoute.core.theme.SafeRouteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeRouteTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}