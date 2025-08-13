package com.example.saferoute.presentation.walkthrough

import androidx.lifecycle.ViewModel

class WalkthroughViewModel : ViewModel() {
    val pages = listOf(
        WalkthroughPage(
            title = "💡 Selamat Datang di SafeRoute",
            description = "SafeRoute membantumu bepergian dengan lebih aman, terutama saat malam hari. Dapatkan informasi lokasi rawan kejahatan dan tips pencegahan secara real-time."
        ),
        WalkthroughPage(
            title = "🛡️ Lindungi Dirimu dengan Fitur Keamanan",
            description = "Gunakan Panic Button saat darurat, lihat peta Heatmap area rawan kejahatan, dan akses rute aman untuk perjalanan malam hari. Semua dalam satu aplikasi."
        ),
        WalkthroughPage(
            title = "🤝 Bersama Kita Lebih Aman",
            description = "Bergabunglah dengan komunitas, laporkan kejadian, dan pelajari tips pencegahan kejahatan. SafeRoute hadir untuk menciptakan lingkungan yang lebih aman."
        )
    )
}
