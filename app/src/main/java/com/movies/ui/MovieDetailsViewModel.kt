package com.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.model.MovieDetailUI
import com.movies.model.MovieResultUI
import com.movies.usecase.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel() {

    private val movieDetailViewStateEmitter = MutableLiveData<MovieDetailViewState>()
    val movieDetailViewState: LiveData<MovieDetailViewState> = movieDetailViewStateEmitter

    private val movieExceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, exception ->
        Timber.d("Error: " + exception.message)
    }

    // get movie details for given movie ID
    fun getMovieDetails(movieId: Long) {
        movieDetailViewStateEmitter.postValue(MovieDetailViewState.Loading(true))
        viewModelScope.launch(movieExceptionHandler) {
            withContext(Dispatchers.IO) {
                movieDetailViewStateEmitter.postValue(MovieDetailViewState.Loading(false))
                when (val result = getMovieDetailsUseCase.getMovieDetails(movieId)) {
                    is MovieResultUI.Error -> movieDetailViewStateEmitter.postValue(MovieDetailViewState.Error(result.errorCode))
                    is MovieResultUI.Success -> movieDetailViewStateEmitter.postValue(MovieDetailViewState.Movie(result.data.first()))
                }
            }
        }
    }

    sealed interface MovieDetailViewState {
        data class Loading(val isLoading: Boolean) : MovieDetailViewState
        data class Movie(val data: MovieDetailUI) : MovieDetailViewState
        data class Error(val errorCode: String) : MovieDetailViewState
    }
}

