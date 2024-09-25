package com.movies.domain.usecase

import com.movies.domain.MovieRepository
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    //Business logic comes here
    suspend fun getTopRatedMovieList() = movieRepository.fetchTopRatedMovies()
}