package com.movies.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.movies.R
import com.movies.ui.model.ErrorUI
import com.movies.ui.model.MovieDetailUI
import com.movies.ui.viewmodel.MovieListViewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun MovieListView(
    movieListViewState: StateFlow<MovieListViewModel.MovieListViewState>,
    onMovieClicked: (Long) -> Unit,
) {

    val viewState by movieListViewState.collectAsStateWithLifecycle()
    var movieList: List<MovieDetailUI> by rememberSaveable { mutableStateOf(emptyList()) }
    var isLoading: Boolean by rememberSaveable { mutableStateOf(false) }

    when (viewState) {
        is MovieListViewModel.MovieListViewState.Error -> {
            val errorStr = when((viewState as MovieListViewModel.MovieListViewState.Error).errorCode){
                ErrorUI.NULL_RESPONSE -> stringResource(R.string.empty_or_null_response)
                ErrorUI.FAILURE_RESPONSE -> stringResource(R.string.api_failed)
            }
            Toast.makeText(
                LocalContext.current,
                stringResource(R.string.error, errorStr),
                Toast.LENGTH_SHORT
            ).show()
        }

        is MovieListViewModel.MovieListViewState.Loading -> isLoading = true
        is MovieListViewModel.MovieListViewState.MovieList -> {
            isLoading = (viewState as MovieListViewModel.MovieListViewState.MovieList).isLoading
            movieList = (viewState as MovieListViewModel.MovieListViewState.MovieList).data
        }

        MovieListViewModel.MovieListViewState.Idle -> {}
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.top_rated_movies),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                items(movieList) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp)
                            .clickable {
                                onMovieClicked(item.id)
                            }
                    ) {
                        //Movie image
                        GlideImage(
                            model = stringResource(R.string.https_image_url, item.posterPath),
                            contentDescription = "",
                            modifier = Modifier
                                .width(92.dp)
                                .height(134.dp)
                                .padding(end = 16.dp)
                                .background(color = colorResource(id = R.color.black))
                        )

                        //movie Name and overview
                        Column(modifier = Modifier.fillMaxWidth()) {
                            //Title
                            Text(
                                text = item.title,
                                modifier = Modifier
                                    .wrapContentSize(),
                                fontSize = 22.sp,
                                lineHeight = 28.sp,
                                color = colorResource(id = R.color.black),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            //overview
                            Text(
                                text = item.overview,
                                modifier = Modifier
                                    .wrapContentSize(),
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = colorResource(id = R.color.black),
                                maxLines = 4,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
