package com.movies.usecase

import com.movies.repository.MovieRepository
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    //Business logic comes here
    suspend fun getTopRatedMovieList() = movieRepository.fetchTopRatedMovies()
}