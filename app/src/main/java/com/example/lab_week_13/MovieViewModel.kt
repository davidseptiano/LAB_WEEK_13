package com.example.lab_week_13

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_week_13.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

class MovieViewModel(movieRepository: MovieRepository) : ViewModel() {

    // StateFlow untuk menangani error, tidak perlu diubah.
    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    // --- PERUBAHAN UTAMA DI SINI ---
    // Logika untuk filter dan sort sekarang berada di dalam ViewModel.

    val popularMovies: StateFlow<List<Movie>> = movieRepository.fetchMovies()
        .map { movies ->
            // Gunakan .map untuk mengubah data mentah menjadi data yang sudah diproses.
            val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
            movies
                // 1. Filter film berdasarkan tahun rilis saat ini.
                .filter { movie -> movie.releaseDate?.startsWith(currentYear) == true }
                // 2. Urutkan hasilnya berdasarkan popularitas secara menurun.
                .sortedByDescending { it.popularity }
        }
        // Jalankan operasi filter dan sort di thread yang berbeda (Default) agar tidak membebani Main thread.
        .flowOn(Dispatchers.Default)
        // Tangkap error yang mungkin terjadi selama proses fetch atau map.
        .catch { exception ->
            _error.value = "An error occurred: ${exception.message}"
        }
        // Ubah 'cold Flow' menjadi 'hot StateFlow' yang efisien.
        // Data akan tetap ada bahkan setelah rotasi layar.
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}