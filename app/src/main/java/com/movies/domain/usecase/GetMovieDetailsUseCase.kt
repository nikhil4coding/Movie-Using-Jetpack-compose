package com.movies.domain.usecase

import com.movies.domain.MovieRepository
import com.movies.data.MovieResult
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    //Business logic comes here
    suspend fun getMovieDetails(movieId: Long): MovieResult {
        return movieRepository.fetchMovieDetails(movieId)
    }
}