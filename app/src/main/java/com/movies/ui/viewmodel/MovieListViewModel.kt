package com.movies.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.domain.usecase.GetMovieListUseCase
import com.movies.domain.usecase.MovieListResult
import com.movies.ui.model.MovieDetailUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase
) : ViewModel() {

    private val movieListViewEmitter = MutableStateFlow<MovieListViewState>(MovieListViewState.Idle)
    val movieListView = movieListViewEmitter.asStateFlow()

    private val movieExceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, exception ->
        Timber.d("Error: " + exception.message)
    }

    // get movie list
    fun getMovieList() {
        movieListViewEmitter.value = MovieListViewState.Loading
        viewModelScope.launch(movieExceptionHandler) {
            when (val results = getMovieListUseCase.getTopRatedMovieList()) {
                is MovieListResult.Error -> {
                    movieListViewEmitter.value = MovieListViewState.Error(results.errorCode)
                }

                is MovieListResult.Success -> {
                    val movieList = results.movieDetail.map {
                        MovieDetailUI(
                            id = it.id,
                            title = it.title,
                            overview = it.overview,
                            posterPath = it.posterPath
                        )
                    }
                    movieListViewEmitter.value = MovieListViewState.MovieList(movieList)
                }
            }

        }
    }

    sealed interface MovieListViewState {
        data object Idle : MovieListViewState
        data object Loading : MovieListViewState
        data class MovieList(val data: List<MovieDetailUI>, val isLoading: Boolean = false) : MovieListViewState
        data class Error(val errorCode: String) : MovieListViewState
    }
}