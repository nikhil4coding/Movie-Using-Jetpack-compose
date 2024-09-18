package com.movies.usecase

import com.movies.model.MovieDetailUI
import com.movies.model.MovieResultUI
import com.movies.repository.MovieDetail
import com.movies.repository.MovieRepository
import com.movies.repository.MovieResult
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
    fun `when error code from Repo return error code to UI`() = runTest {
        whenever(repository.fetchTopRatedMovies()).thenReturn(MovieResult.Error(""))
        val expectedResult = MovieResultUI.Error("")

        val result = useCase.getTopRatedMovieList()
        assertEquals(expectedResult, result)
    }

    @Test
    fun `when successful from Repo return data to UI`() = runTest {

        val movieDetails = MovieDetail(
            id = 1L,
            title = "Movie 1",
            overview = "Movie 1 overview",
            posterPath = "movie.jpg"
        )

        whenever(repository.fetchTopRatedMovies())
            .thenReturn(MovieResult.Success(listOf(movieDetails)))
        val expectedResult = MovieResultUI.Success(
            listOf(
                MovieDetailUI(
                    id = 1L,
                    title = "Movie 1",
                    overview = "Movie 1 overview",
                    posterPath = "movie.jpg"
                )
            )
        )

        val result = useCase.getTopRatedMovieList()
        assertEquals(expectedResult, result)
    }
}