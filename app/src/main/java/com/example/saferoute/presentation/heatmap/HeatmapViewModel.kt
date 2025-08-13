package com.example.saferoute.presentation.heatmap

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HeatmapViewModel(application: Application) : AndroidViewModel(application) {

    private val _heatmapPoints = MutableStateFlow<List<LatLng>>(emptyList())
    val heatmapPoints: StateFlow<List<LatLng>> = _heatmapPoints

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // ✅ Sekarang, ViewModel hanya menjalankan satu fungsi ini.
        fetchHeatmapDataFromFirestore()
    }

    /**
     * Fungsi utama aplikasi: Mengambil data koordinat yang sudah jadi dari Firestore.
     */
    private fun fetchHeatmapDataFromFirestore() {
        _isLoading.value = true
        Firebase.firestore.collection("lokasi_rawan_begal")
            .get()
            .addOnSuccessListener { result ->
                val coordinates = result.mapNotNull { document ->
                    // Mengambil data GeoPoint dan mengubahnya menjadi LatLng
                    document.getGeoPoint("koordinat")?.let { geoPoint ->
                        LatLng(geoPoint.latitude, geoPoint.longitude)
                    }
                }
                _heatmapPoints.value = coordinates
                _isLoading.value = false
                Log.d("FIRESTORE_SUCCESS", "Berhasil mengambil ${coordinates.size} titik dari Firestore.")
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                Log.w("FIRESTORE_ERROR", "Gagal mengambil data dari Firestore.", exception)
            }
    }
}