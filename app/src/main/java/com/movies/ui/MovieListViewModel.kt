package com.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.model.MovieDetailUI
import com.movies.model.MovieResultUI
import com.movies.usecase.GetMovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase
) : ViewModel() {

    private val movieListViewStateEmitter = MutableLiveData<MovieListViewState>()
    val movieListViewState: LiveData<MovieListViewState> = movieListViewStateEmitter

    private val movieExceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, exception ->
        Timber.d("Error: " + exception.message)
    }

    // get movie list
    fun getMovieList() {
        movieListViewStateEmitter.postValue(MovieListViewState.Loading(true))
        viewModelScope.launch(movieExceptionHandler) {
            withContext(Dispatchers.IO) {
                movieListViewStateEmitter.postValue(MovieListViewState.Loading(false))
                when (val results = getMovieListUseCase.getTopRatedMovieList()) {
                    is MovieResultUI.Error -> movieListViewStateEmitter.postValue(MovieListViewState.Error(results.errorCode))
                    is MovieResultUI.Success -> movieListViewStateEmitter.postValue(MovieListViewState.MovieList(results.data))
                }
            }
        }
    }

    sealed interface MovieListViewState {
        data class Loading(val isLoading: Boolean) : MovieListViewState
        data class MovieList(val data: List<MovieDetailUI>) : MovieListViewState
        data class Error(val errorCode: String) : MovieListViewState
    }
}

