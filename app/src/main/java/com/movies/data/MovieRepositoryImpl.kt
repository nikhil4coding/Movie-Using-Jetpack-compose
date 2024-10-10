package com.movies.data

import com.movies.data.api.MovieService
import com.movies.domain.MovieRepository
import javax.inject.Inject

internal class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService
) : MovieRepository {
    override suspend fun fetchTopRatedMovies(): MovieListResponse {
        val response = movieService.topRated()
        return if (response.isSuccessful) {
            response.body()?.let {
                MovieListResponse.Success(it)
            } ?: MovieListResponse.Error(MovieApiError.NULL_RESPONSE)
        } else {
            MovieListResponse.Error(MovieApiError.FAILURE_RESPONSE)
        }
    }

    override suspend fun fetchMovieDetails(id: Long): MovieDetailResponse {
        val response = movieService.movieDetails(id)
        return if (response.isSuccessful) {
            response.body()?.let {
                MovieDetailResponse.Success(it)
            } ?: MovieDetailResponse.Error(errorCode = MovieApiError.NULL_RESPONSE)
        } else {
            MovieDetailResponse.Error(MovieApiError.FAILURE_RESPONSE)
        }
    }
}

enum class MovieApiError { NULL_RESPONSE, FAILURE_RESPONSE }
sealed interface MovieListResponse {
    data class Success(val data: MovieListResponseDTO) : MovieListResponse
    data class Error(val errorCode: MovieApiError) : MovieListResponse
}

sealed interface MovieDetailResponse {
    data class Success(val data: MovieDetailDTO) : MovieDetailResponse
    data class Error(val errorCode: MovieApiError) : MovieDetailResponse
}
