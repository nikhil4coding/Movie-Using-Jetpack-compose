package com.movies.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movies.domain.usecase.GetMovieDetailsUseCase
import com.movies.domain.usecase.MovieDetailResult
import com.movies.ui.model.MovieDetailUI
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
                    is MovieDetailResult.Error -> movieDetailViewStateEmitter.postValue(MovieDetailViewState.Error(result.errorCode))
                    is MovieDetailResult.Success -> {
                        val movieDetailUI = MovieDetailUI(
                            id = result.movieDetail.id,
                            title = result.movieDetail.title,
                            overview = result.movieDetail.overview,
                            posterPath = result.movieDetail.posterPath
                        )

                        movieDetailViewStateEmitter.postValue(MovieDetailViewState.Movie(data = movieDetailUI))
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