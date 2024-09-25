package com.movies.data

import com.movies.data.api.MovieService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MovieRepositoryImplTest {

    private val movieService: MovieService = mock()
    private val movieMapper: MovieMapper = mock()

    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        repository = MovieRepositoryImpl(movieService, movieMapper)
    }

    @Test
    fun `when fetch TopRated Movies returns empty list`() = runTest {
        whenever(movieService.topRated()).thenReturn(Response.success(MovieListResponseDTO(emptyList())))
        whenever(movieMapper.toMovieList(emptyList())).thenReturn(emptyList())

        launch {
            val result = repository.fetchTopRatedMovies()
            assertEquals(MovieResult.Error(errorCode = "Empty List"), result)
        }
    }

    @Test
    fun `when fetch TopRated Movies returns list of items`() = runTest {
        val movieListDto = listOf(
            MovieDetailDTO(
                id = 1L,
                title = "Movie 1",
                overview = "Movie 1 overview",
                posterPath = "movie.jpg"
            )
        )

        val movieList = listOf(
            MovieDetail(
                id = 1L,
                title = "Movie 1",
                overview = "Movie 1 overview",
                posterPath = "movie.jpg"
            )
        )
        whenever(movieService.topRated()).thenReturn(Response.success(MovieListResponseDTO(movieListDto)))
        whenever(movieMapper.toMovieList(movieListDto)).thenReturn(movieList)

        launch {
            val result = repository.fetchTopRatedMovies()
            assertEquals(MovieResult.Success(movieList), result)
        }
    }

    @Test
    fun `fetch movie details for given id returns value`() = runTest {
        val movieDto = MovieDetailDTO(
            id = 1L,
            title = "Movie 1",
            overview = "Movie 1 overview",
            posterPath = "movie.jpg"
        )

        val movieDetails =
            MovieDetail(
                id = 1L,
                title = "Movie 1",
                overview = "Movie 1 overview",
                posterPath = "movie.jpg"
            )

        val movieId = 123L
        whenever(movieService.movieDetails(movieId)).thenReturn(Response.success(movieDto))
        whenever(movieMapper.toMovie(movieDto)).thenReturn(movieDetails)

        launch {
            val result = repository.fetchMovieDetails(movieId)
            assertEquals(MovieResult.Success(listOf(movieDetails)), result)
        }
    }
}