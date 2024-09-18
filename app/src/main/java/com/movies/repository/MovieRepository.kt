package com.movies.repository

interface MovieRepository {
    suspend fun fetchTopRatedMovies(): MovieResult
    suspend fun fetchMovieDetails(id: Long): MovieResult
}