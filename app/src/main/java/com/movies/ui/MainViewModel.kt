package com.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.model.MovieDetailUI
import com.movies.model.MovieResultUI
import com.movies.usecase.GetMovieDetailsUseCase
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
class MainViewModel @Inject constructor(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel() {

    private val viewStateEmitter = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = viewStateEmitter

    private val movieExceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, exception ->
        Timber.d("Error: " + exception.message)
    }

    // get movie list
    fun getMovieList() {
        viewStateEmitter.postValue(ViewState.Loading(true))
        viewModelScope.launch(movieExceptionHandler) {
            withContext(Dispatchers.IO) {
                viewStateEmitter.postValue(ViewState.Loading(false))
                when (val results = getMovieListUseCase.getTopRatedMovieList()) {
                    is MovieResultUI.Error -> viewStateEmitter.postValue(ViewState.Error(results.errorCode))
                    is MovieResultUI.Success -> viewStateEmitter.postValue(ViewState.MovieList(results.data))
                }
            }
        }
    }

    // get movie details for given movie ID
    fun getMovieDetails(movieId: Long) {
        viewStateEmitter.postValue(ViewState.Loading(true))
        viewModelScope.launch(movieExceptionHandler) {
            withContext(Dispatchers.IO) {
                viewStateEmitter.postValue(ViewState.Loading(false))
                when (val result = getMovieDetailsUseCase.getMovieDetails(movieId)) {
                    is MovieResultUI.Error -> viewStateEmitter.postValue(ViewState.Error(result.errorCode))
                    is MovieResultUI.Success -> viewStateEmitter.postValue(ViewState.Movie(result.data.first()))
                }
            }
        }
    }

    sealed interface ViewState {
        data class Loading(val isLoading: Boolean) : ViewState
        data class MovieList(val data: List<MovieDetailUI>) : ViewState
        data class Movie(val data: MovieDetailUI) : ViewState
        data class Error(val errorCode: String) : ViewState
    }
}

