package com.example.lab_week_13

import android.app.Application
import com.example.lab_week_13.api.MovieService
// Import yang ditambahkan
import com.example.lab_week_13.database.MovieDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieApplication : Application() {

    // Properti ini akan diinisialisasi di onCreate
    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        // Persiapan untuk MovieService tetap sama
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val movieService = retrofit.create(MovieService::class.java)

        // --- Blok Kode yang Diperbarui ---

        // 1. Buat instance MovieDatabase menggunakan metode getInstance (pola Singleton)
        val movieDatabase = MovieDatabase.getInstance(this)

        // 2. Buat MovieRepository dengan memberikan movieService dan movieDatabase
        movieRepository = MovieRepository(movieService, movieDatabase)
    }
}
