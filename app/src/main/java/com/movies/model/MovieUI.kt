package com.movies.model

import java.io.Serializable

data class MovieDetailUI(
    val id: Long = 0L,
    val title: String = "",
    val overview: String = "",
    val posterPath: String = ""
) : Serializable

sealed interface MovieResultUI {
    data class Success(val data: List<MovieDetailUI>) : MovieResultUI
    data class Error(val errorCode: String) : MovieResultUI
}