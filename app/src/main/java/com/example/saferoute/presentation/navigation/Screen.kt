package com.example.saferoute.presentation.navigation

sealed class Screen(val route: String) {
    // Alur otentikasi dan onboarding
    object Splash : Screen("splash")
    object Walkthrough : Screen("walkthrough")
    object Auth : Screen("auth_graph") // Rute grup untuk Login & Register

    // Rute utama setelah login
    object Main : Screen("main_graph") // Rute grup untuk semua yang ada di dalam MainScreen

    // Rute di dalam alur Auth
    object Login : Screen("login")
    object Register : Screen("register")

    object Home : Screen("home")

    // Rute di dalam alur Main (Bottom Navigation & halaman terkait)
    object HomeTab : Screen("home_tab")
    object MapTab : Screen("map_tab")
    object PanicTab : Screen("panic_tab")
    object CommunityTab : Screen("community_tab")
    object ProfileTab : Screen("profile_tab")


    // Rute di dalam alur Profile
    object ProfileDetails : Screen("profile_details") // Halaman utama profil
    object EditProfile : Screen("edit_profile")
    object ChangePassword : Screen("change_password")
}

