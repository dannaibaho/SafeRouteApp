package com.example.saferoute.presentation.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.saferoute.presentation.heatmap.HeatmapViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    //  menggunakan kembali ViewModel yang sudah ada
    heatmapViewModel: HeatmapViewModel = viewModel()
) {
    // Logika untuk mengambil data dan status loading sama persis seperti di HeatmapScreen
    val points by heatmapViewModel.heatmapPoints.collectAsState()
    val isLoading by heatmapViewModel.isLoading.collectAsState()

    val bandung = LatLng(-6.9175, 107.6191)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bandung, 12f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Kita akan melakukan loop pada setiap titik dan membuat sebuah Marker (pin)
            points.forEach { point ->
                Marker(
                    state = MarkerState(position = point),
                    title = "Lokasi Rawan Begal", // Judul saat pin di-tap
                    snippet = "Koordinat: ${point.latitude}, ${point.longitude}", // Teks tambahan
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED) // Pin warna merah
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}