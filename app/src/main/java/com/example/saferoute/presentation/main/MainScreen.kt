package com.example.saferoute.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.saferoute.presentation.navigation.Screen
import com.example.saferoute.presentation.profile.ChangePasswordScreen
import com.example.saferoute.presentation.profile.EditProfileScreen
import com.example.saferoute.presentation.profile.ProfileScreen
import com.example.saferoute.presentation.home.HomeScreen
import com.example.saferoute.presentation.map.MapScreen

// Data class untuk mempermudah manajemen item di Bottom Bar
data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)

// --- Placeholder untuk semua screen agar kode bisa berjalan ---

@Composable fun CommunityScreen() { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("COMING SOON!") } }


@Composable
fun MainScreen(
    // Menerima NavController dari NavGraph (untuk logout)
    navController: NavHostController
) {
    // NavController khusus untuk navigasi di dalam MainScreen (antar tab)
    val mainNavController = rememberNavController()

    val navItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, Screen.HomeTab),
        BottomNavItem("Map", Icons.Default.LocationOn, Screen.MapTab),
        BottomNavItem("Community", Icons.Default.Person, Screen.CommunityTab),
        BottomNavItem("Profile", Icons.Default.Person, Screen.ProfileTab)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            mainNavController.navigate(item.screen.route) {
                                popUpTo(mainNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        // NavHost internal untuk mengatur halaman yang tampil sesuai tab
        NavHost(
            navController = mainNavController,
            startDestination = Screen.HomeTab.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.HomeTab.route) { HomeScreen() }
            composable(Screen.MapTab.route) { MapScreen() }
            composable(Screen.CommunityTab.route) { CommunityScreen() }
            composable(Screen.ProfileTab.route) {
                ProfileScreen(
                    onEditProfile = {
                        mainNavController.navigate(Screen.EditProfile.route)
                    },
                    onChangePassword = {
                        mainNavController.navigate(Screen.ChangePassword.route)
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(onBack = { mainNavController.popBackStack() })
            }

            composable(Screen.ChangePassword.route) {
                ChangePasswordScreen(onBack = { mainNavController.popBackStack() })
            }
        }
    }
}