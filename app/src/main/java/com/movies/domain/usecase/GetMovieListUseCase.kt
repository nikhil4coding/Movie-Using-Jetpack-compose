package com.movies.domain.usecase

import com.movies.data.MovieListResponse
import com.movies.domain.MovieRepository
import com.movies.domain.model.MovieDetail
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    //Business logic comes here
    suspend fun getTopRatedMovieList(): MovieListResult {
        return when (val response = movieRepository.fetchTopRatedMovies()) {
            is MovieListResponse.Error -> MovieListResult.Error(response.errorCode)
            is MovieListResponse.Success -> {
                MovieListResult.Success(
                    response.data.results.map {
                        MovieDetail(
                            id = it.id,
                            title = it.title,
                            overview = it.overview,
                            posterPath = it.posterPath
                        )
                    }
                )
            }
        }
    }
}

sealed interface MovieListResult {
    data class Success(val movieDetail: List<MovieDetail>) : MovieListResult
    data class Error(val errorCode: String) : MovieListResult
}