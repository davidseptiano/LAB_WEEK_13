package com.example.lab_week_13

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab_week_13.databinding.ActivityMainBinding
import com.example.lab_week_13.model.Movie

class MainActivity : AppCompatActivity() {

    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener {
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil
            .setContentView(this, R.layout.activity_main)

        // Ambil instance ViewModel
        val movieRepository = (application as MovieApplication).movieRepository
        movieViewModel = ViewModelProvider(
            this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieViewModel(movieRepository) as T
                }
            })[MovieViewModel::class.java]

        // === LANGKAH YANG ANDA MINTA SUDAH DILAKUKAN DI SINI ===
        // 1. Menghubungkan viewModel ke binding
        binding.viewModel = movieViewModel
        // 2. Menetapkan lifecycleOwner
        binding.lifecycleOwner = this
        // ========================================================

        // Mengatur adapter untuk RecyclerView
        binding.movieList.adapter = movieAdapter

        // === LANGKAH KETIGA JUGA SUDAH DILAKUKAN ===
        // Tidak ada lagi blok 'lifecycleScope.launch' di sini.
        // Ini sudah benar karena Data Binding menanganinya secara otomatis.
        // ===============================================
    }

    private fun openMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_TITLE, movie.title)
            putExtra(DetailsActivity.EXTRA_RELEASE, movie.releaseDate)
            putExtra(DetailsActivity.EXTRA_OVERVIEW, movie.overview)
            putExtra(DetailsActivity.EXTRA_POSTER, movie.posterPath)
        }
        startActivity(intent)
    }
}
