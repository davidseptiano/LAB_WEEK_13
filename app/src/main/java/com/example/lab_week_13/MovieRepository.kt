package com.example.lab_week_13

import com.example.lab_week_13.api.MovieService
import com.example.lab_week_13.database.MovieDao // <-- Pastikan ini di-import jika belum
import com.example.lab_week_13.database.MovieDatabase
import com.example.lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase
) {
    private val apiKey = "84977e7fc52e586f021ff81dad32c61d"

    // Ganti fungsi fetchMovies yang lama dengan yang ini
    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            // Dapatkan akses ke DAO dari instance database
            val movieDao = movieDatabase.movieDao()
            // Coba ambil film yang tersimpan dari database
            val savedMovies = movieDao.getMovies()

            // Jika database kosong...
            if (savedMovies.isEmpty()) {
                // ...ambil daftar film populer dari API
                val movies = movieService.getPopularMovies(apiKey).results

                // Simpan daftar film tersebut ke dalam database
                movieDao.addMovies(movies)
                // Kirimkan (emit) daftar film dari API
                emit(movies)
            } else {
                // Jika ada film yang tersimpan di database...
                // ...langsung kirimkan (emit) daftar film dari database
                emit(savedMovies)
            }
            // Jalankan semua operasi di dalam flow ini pada thread I/O
        }.flowOn(Dispatchers.IO)
    }
}
