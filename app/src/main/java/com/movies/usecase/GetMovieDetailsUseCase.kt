package com.movies.usecase

import com.movies.repository.MovieRepository
import com.movies.repository.MovieResult
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    //Business logic comes here
    suspend fun getMovieDetails(movieId: Long): MovieResult {
        return movieRepository.fetchMovieDetails(movieId)
    }
}