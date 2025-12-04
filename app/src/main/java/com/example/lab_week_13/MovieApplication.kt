package com.example.lab_week_13

import android.app.Application
// 1. Tambahkan import yang diperlukan untuk WorkManager
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.lab_week_13.api.MovieService
import com.example.lab_week_13.database.MovieDatabase
import com.example.lab_week_13.worker.MovieWorker // Pastikan worker di-import
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit // Import untuk TimeUnit

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

        // Buat instance MovieDatabase menggunakan metode getInstance (pola Singleton)
        val movieDatabase = MovieDatabase.getInstance(this)

        // Buat MovieRepository dengan memberikan movieService dan movieDatabase
        movieRepository = MovieRepository(movieService, movieDatabase)

        // --- 2. TAMBAHKAN BLOK KODE PENJADWALAN WORKER DI SINI ---

        // Buat instance Constraints untuk menentukan kondisi agar task worker dapat berjalan.
        val constraints = Constraints.Builder()
            // Hanya jalankan task jika perangkat terhubung ke internet.
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Buat instance WorkRequest untuk menjadwalkan tugas di latar belakang.
        val workRequest = PeriodicWorkRequest
            // Jalankan tugas setiap 1 jam, bahkan jika aplikasi ditutup atau perangkat di-restart.
            .Builder(
                MovieWorker::class.java,
                1,
                TimeUnit.HOURS
            ).setConstraints(constraints)
            .addTag("movie-work") // Memberi tag untuk identifikasi
            .build()

        // Jadwalkan tugas latar belakang.
        WorkManager.getInstance(this)
            .enqueue(workRequest)
    }
}
