package com.example.saferoute.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    fun startSplash(onFinished: () -> Unit) {
        viewModelScope.launch {
            delay(3000) // durasi splash 3 detik
            onFinished()
        }
    }
}
