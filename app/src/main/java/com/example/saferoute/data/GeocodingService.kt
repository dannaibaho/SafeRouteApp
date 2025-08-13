package com.example.saferoute.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class GeocodingResponse(val lat: String?, val lon: String?)

interface GeocodingApiService {
    @GET("search")
    suspend fun getCoordinates(
        @Query("q") address: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 1
    ): List<GeocodingResponse>
}

// Object Retrofit Client
object RetrofitClient {
    private const val GEOCODING_API_BASE_URL = "https://nominatim.openstreetmap.org/"

    private val httpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val originalRequest = chain.request()
            val requestWithUserAgent = originalRequest.newBuilder()
                .header("User-Agent", "SafeRouteApp/1.0 (danielnaibaho36@gmail.com)")
                .build()
            chain.proceed(requestWithUserAgent)
        }
    }.build()

    val geocodingApi: GeocodingApiService by lazy {
        Retrofit.Builder()
            .baseUrl(GEOCODING_API_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApiService::class.java)
    }
}