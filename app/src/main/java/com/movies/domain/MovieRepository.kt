package com.movies.domain

import com.movies.data.MovieResult

interface MovieRepository {
    suspend fun fetchTopRatedMovies(): MovieResult
    suspend fun fetchMovieDetails(id: Long): MovieResult
}