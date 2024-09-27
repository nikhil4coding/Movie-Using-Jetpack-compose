package com.movies.domain.usecase

import com.movies.data.MovieDetailDTO
import com.movies.data.MovieListResponse
import com.movies.data.MovieListResponseDTO
import com.movies.domain.MovieRepository
import com.movies.domain.model.MovieDetail
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMovieListUseCaseTest {

    private val repository: MovieRepository = mock()
    private lateinit var useCase: GetMovieListUseCase

    @Before
    fun setUp() {
        useCase = GetMovieListUseCase(repository)
    }

    @Test
    fun `get movie list returns error return error`() = runTest {
        whenever(repository.fetchTopRatedMovies()).thenReturn(MovieListResponse.Error("error"))

        val result = useCase.getTopRatedMovieList()
        assertEquals(MovieListResult.Error("error"), result)
    }

    @Test
    fun `get movie list returns empty list return list`() = runTest {
        whenever(repository.fetchTopRatedMovies()).thenReturn(MovieListResponse.Success(MovieListResponseDTO(emptyList())))

        val result = useCase.getTopRatedMovieList()
        assertEquals(MovieListResult.Success(emptyList()), result)
    }

    @Test
    fun `get movie list return list`() = runTest {
        val movieListResponseDTO = MovieListResponseDTO(
            listOf(
                MovieDetailDTO(
                    id = 1L,
                    title = "Movie 1",
                    overview = "Movie 1 overview",
                    posterPath = "movie.jpg"
                )
            )
        )
        val expected = listOf(
            MovieDetail(
                id = 1L,
                title = "Movie 1",
                overview = "Movie 1 overview",
                posterPath = "movie.jpg"
            )
        )
        whenever(repository.fetchTopRatedMovies()).thenReturn(MovieListResponse.Success(movieListResponseDTO))

        val result = useCase.getTopRatedMovieList()
        assertEquals(MovieListResult.Success(expected), result)
    }
}