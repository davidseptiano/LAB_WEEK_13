package com.example.lab_week_13.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.lab_week_13.MovieApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieWorker(
    // Context diperlukan untuk mendapatkan akses ke Application dan Repository
    appContext: Context,
    params: WorkerParameters
) : Worker(appContext, params) {

    // doWork() secara otomatis dipanggil di background thread oleh WorkManager.
    override fun doWork(): Result {
        // Dapatkan referensi ke repository dari kelas Application.
        val movieRepository = (applicationContext as MovieApplication).movieRepository

        // Karena fetchMoviesFromNetwork adalah suspend function,
        // kita perlu menjalankannya di dalam sebuah coroutine.
        CoroutineScope(Dispatchers.IO).launch {
            movieRepository.fetchMoviesFromNetwork()
        }

        // Menandakan bahwa pekerjaan telah berhasil dijadwalkan untuk dieksekusi.
        // Catatan: Ini tidak menunggu coroutine selesai, hanya menandakan
        // bahwa pekerjaan telah berhasil dimulai.
        return Result.success()
    }
}
