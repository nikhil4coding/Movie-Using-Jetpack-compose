package com.movies.data.api

import com.movies.data.MovieDetailDTO
import com.movies.data.MovieListResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {

    @GET("movie/top_rated")
    suspend fun topRated(): Response<MovieListResponseDTO>

    @GET("movie/{movie_id}")
    suspend fun movieDetails(@Path("movie_id") movieId: Long): Response<MovieDetailDTO>
}