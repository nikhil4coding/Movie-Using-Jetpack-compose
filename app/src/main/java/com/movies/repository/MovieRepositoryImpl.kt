package com.movies.repository

import com.movies.api.MovieService
import javax.inject.Inject

internal class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieMapper: MovieMapper
) : MovieRepository {
    override suspend fun fetchTopRatedMovies(): MovieResult {
        val response = movieService.topRated()
        return if (response.isSuccessful) {
            response.body()?.let {
                if(it.results.isEmpty()){
                    MovieResult.Error("Empty List")
                }else{
                    MovieResult.Success(movieMapper.toMovieList(it.results))
                }
            } ?: MovieResult.Error("Null List")
        } else {
            MovieResult.Error("Something went Wrong")
        }
    }

    override suspend fun fetchMovieDetails(id: Long): MovieResult {
        val response = movieService.movieDetails(id)
        return if (response.isSuccessful) {
            response.body()?.let {
                MovieResult.Success(listOf(movieMapper.toMovie(it)))
            } ?: MovieResult.Error("No details found")
        } else {
            MovieResult.Error("Something went Wrong")
        }
    }
}

data class MovieDetail(
    val id: Long,
    val title: String,
    val overview: String,
    val posterPath: String
)

sealed interface MovieResult {
    data class Success(val data: List<MovieDetail>) : MovieResult
    data class Error(val errorCode: String) : MovieResult
}
