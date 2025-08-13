package com.example.saferoute.presentation.heatmap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.heatmaps.HeatmapTileProvider

@Composable
fun HeatmapScreen(
    heatmapViewModel: HeatmapViewModel = viewModel()
) {
    val heatmapPoints by heatmapViewModel.heatmapPoints.collectAsState()
    val isLoading by heatmapViewModel.isLoading.collectAsState()

    val bandung = LatLng(-6.9175, 107.6191)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bandung, 12f)
    }

    val provider = remember(heatmapPoints) {
        if (heatmapPoints.isNotEmpty()) {
            HeatmapTileProvider.Builder().data(heatmapPoints).radius(50).build()
        } else { null }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            if (provider != null) {
                TileOverlay(tileProvider = provider)
            }
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}