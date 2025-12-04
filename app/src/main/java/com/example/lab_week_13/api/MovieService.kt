package com.example.lab_week_13.api

import com.example.lab_week_13.model.PopularMoviesResponse // Make sure to import your response model
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String // <-- This is the corrected line
    ): PopularMoviesResponse
}