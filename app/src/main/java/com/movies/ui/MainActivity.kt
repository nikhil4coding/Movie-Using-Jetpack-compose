package com.movies.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.movies.R
import com.movies.model.MovieDetailUI
import com.movies.ui.theme.MoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMovieList()
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
        var movieDetail: MovieDetailUI? by rememberSaveable { mutableStateOf(null) }
        var isLoading: Boolean by rememberSaveable { mutableStateOf(false) }


        when (val viewState = viewModel.viewState.observeAsState().value) {
            is MainViewModel.ViewState.MovieList -> {
                movieList = viewState.data
            }

            is MainViewModel.ViewState.Error -> {
                Toast.makeText(this, stringResource(R.string.error, viewState.errorCode), Toast.LENGTH_SHORT).show()
            }

            is MainViewModel.ViewState.Movie -> {
                movieDetail = viewState.data
                navController.navigate(MOVIE_DETAILS)
            }

            is MainViewModel.ViewState.Loading -> isLoading = viewState.isLoading
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
                        viewModel.getMovieDetails(it)
                    }
                )
            }

            composable(MOVIE_DETAILS) {
                movieDetail?.let {
                    MovieDetailsView(
                        isLoading = isLoading,
                        movieDetails = it,
                        onBackClicked = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
