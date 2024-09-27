package com.movies.domain

import com.movies.data.MovieDetailResponse
import com.movies.data.MovieListResponse

interface MovieRepository {
    suspend fun fetchTopRatedMovies(): MovieListResponse
    suspend fun fetchMovieDetails(id: Long): MovieDetailResponse
}