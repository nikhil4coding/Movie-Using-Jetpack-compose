package com.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.ui.model.MovieDetailUI
import com.movies.data.MovieResult
import com.movies.domain.usecase.GetMovieDetailsUseCase
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
        movieDetailViewStateEmitter.postValue(MovieDetailViewState.Loading)
        viewModelScope.launch(movieExceptionHandler) {
            withContext(Dispatchers.IO) {
                when (val result = getMovieDetailsUseCase.getMovieDetails(movieId)) {
                    is MovieResult.Error -> movieDetailViewStateEmitter.postValue(MovieDetailViewState.Error(result.errorCode))
                    is MovieResult.Success -> {
                        val movieDetailList = result.data.map {
                            MovieDetailUI(
                                id = it.id,
                                title = it.title,
                                overview = it.overview,
                                posterPath = it.posterPath
                            )
                        }
                        movieDetailViewStateEmitter.postValue(MovieDetailViewState.Movie(movieDetailList.first()))
                    }
                }
            }
        }
    }

    sealed interface MovieDetailViewState {
        data object Loading : MovieDetailViewState
        data class Movie(val data: MovieDetailUI, val isLoading: Boolean = false) : MovieDetailViewState
        data class Error(val errorCode: String) : MovieDetailViewState
    }
}

