package com.movies.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            MaterialTheme {
                SetupView()
            }
        }
    }

    @Composable
    fun SetupView() {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = MOVIE_LIST
        ) {
            composable(MOVIE_LIST) {
                MovieListView(
                    movieListViewState = movieListViewModel.movieListView,
                    onMovieClicked = {
                        navController.navigate(MOVIE_DETAILS)
                        movieDetailViewModel.getMovieDetails(it)
                    }
                )
            }

            composable(MOVIE_DETAILS) {
                MovieDetailsView(
                    movieDetailViewState = movieDetailViewModel.movieDetailView,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
