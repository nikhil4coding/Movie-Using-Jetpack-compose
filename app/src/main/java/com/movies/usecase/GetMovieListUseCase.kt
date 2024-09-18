package com.movies.usecase

import com.movies.model.MovieDetailUI
import com.movies.model.MovieResultUI
import com.movies.repository.MovieRepository
import com.movies.repository.MovieResult
import javax.inject.Inject

class GetMovieListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend fun getTopRatedMovieList(): MovieResultUI {
        return when (val result = movieRepository.fetchTopRatedMovies()) {
            is MovieResult.Success -> {
                MovieResultUI.Success(
                    result.data.map {
                        MovieDetailUI(
                            id = it.id,
                            title = it.title,
                            overview = it.overview,
                            posterPath = it.posterPath
                        )
                    }
                )
            }

            is MovieResult.Error -> MovieResultUI.Error(result.errorCode)
        }
    }
}