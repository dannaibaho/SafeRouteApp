package com.example.saferoute.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.saferoute.R
import com.example.saferoute.presentation.heatmap.HeatmapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.TileOverlay
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(
    heatmapViewModel: HeatmapViewModel = viewModel()
) {
    val context = LocalContext.current
    val drivingTips = listOf(
        "Gunakan Rute Aman",
        "Hindari Berkendara Sendirian",
        "Hindari Jam Rawan antara pukul 22:00 - 03:00",
        "Waspadai Titik Rawan",
        "Hindari jalan yang gelap meskipun lebih cepat"
    )

    val heatmapPoints by heatmapViewModel.heatmapPoints.collectAsState()
    val isLoading by heatmapViewModel.isLoading.collectAsState()

    val bandung = LatLng(-6.9175, 107.6191)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bandung, 12f)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Safe Route",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Heatmap Section
        item {
            Text(
                text = "Heatmap",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    if (heatmapPoints.isNotEmpty()) {
                        val provider = HeatmapTileProvider.Builder()
                            .data(heatmapPoints)
                            .radius(50)
                            .build()

                        TileOverlay(
                            tileProvider = provider
                        )
                    }
                }

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Tips Section
        item {
            Text(
                text = "Tips Berkendara Aman",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(drivingTips.size) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(text = "${index + 1}. ", fontWeight = FontWeight.SemiBold)
                Text(text = drivingTips[index])
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

//        // Panic Button
//        item {
//            Button(
//                onClick = {
//                    Toast.makeText(context, "Tombol Panik Ditekan!", Toast.LENGTH_LONG).show()
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text(
//                    text = "PANIC BUTTON",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//            }
//        }
    }
}
