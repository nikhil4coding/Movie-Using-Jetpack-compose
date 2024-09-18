package com.movies.repository

import com.movies.model.MovieDetailDTO
import javax.inject.Inject

class MovieMapper @Inject constructor() {
    fun toMovieList(movieList: List<MovieDetailDTO>): List<MovieDetail> {
        return movieList.map {
            toMovie(it)
        }
    }

    fun toMovie(movieDetail: MovieDetailDTO): MovieDetail {
        return MovieDetail(
            id = movieDetail.id,
            title = movieDetail.title,
            overview = movieDetail.overview,
            posterPath = movieDetail.posterPath
        )
    }
}