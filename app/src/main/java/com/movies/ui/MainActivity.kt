package com.movies.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.movies.R
import com.movies.ui.model.MovieDetailUI
import com.movies.ui.theme.MoviesTheme
import com.movies.ui.viewmodel.MovieDetailsViewModel
import com.movies.ui.viewmodel.MovieListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val movieListViewModel: MovieListViewModel by viewModels()
    private val movieDetailViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieListViewModel.getMovieList()
        setContent {
            MoviesTheme {
                SetupView()
            }
        }
    }

    @Composable
    fun SetupView() {
        val navController = rememberNavController()

        var movieList: List<MovieDetailUI> by rememberSaveable { mutableStateOf(emptyList()) }
        var movieDetail: MovieDetailUI by rememberSaveable { mutableStateOf(MovieDetailUI()) }
        var selectedMovieId: Long by rememberSaveable { mutableLongStateOf(0L) }
        var isLoading: Boolean by rememberSaveable { mutableStateOf(false) }

        when (val viewState = movieListViewModel.movieListViewState.observeAsState().value) {
            is MovieListViewModel.MovieListViewState.Error -> Toast.makeText(this, stringResource(R.string.error, viewState.errorCode), Toast.LENGTH_SHORT).show()
            is MovieListViewModel.MovieListViewState.Loading -> isLoading = true
            is MovieListViewModel.MovieListViewState.MovieList -> {
                isLoading = viewState.isLoading
                movieList = viewState.data
            }

            null -> {}
        }

        when (val viewState = movieDetailViewModel.movieDetailViewState.observeAsState().value) {
            is MovieDetailsViewModel.MovieDetailViewState.Error -> Toast.makeText(this, stringResource(R.string.error, viewState.errorCode), Toast.LENGTH_SHORT).show()
            is MovieDetailsViewModel.MovieDetailViewState.Loading -> isLoading = true
            is MovieDetailsViewModel.MovieDetailViewState.Movie -> {
                isLoading = viewState.isLoading
                movieDetail = viewState.data
            }

            null -> {}
        }

        NavHost(
            navController = navController,
            startDestination = MOVIE_LIST
        ) {
            composable(MOVIE_LIST) {
                MovieListView(
                    isLoading = isLoading,
                    movieList = movieList,
                    onMovieClicked = {
                        selectedMovieId = it
                        navController.navigate(MOVIE_DETAILS)
                        movieDetailViewModel.getMovieDetails(it)
                    }
                )
            }

            composable(MOVIE_DETAILS) {
                MovieDetailsView(
                    isLoading = isLoading,
                    movieDetails = movieDetail,
                    onBackClicked = {
                        if (isLoading) isLoading = false
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
