package com.movies.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
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
import com.movies.ui.model.MovieDetailUI
import com.movies.ui.viewmodel.MovieDetailsViewModel
import kotlinx.coroutines.flow.StateFlow

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun MovieDetailsView(
    movieDetailViewState: StateFlow<MovieDetailsViewModel.MovieDetailViewState>,
    onBackClicked: () -> Unit,
) {

    val state by movieDetailViewState.collectAsStateWithLifecycle()
    var movieDetails: MovieDetailUI by rememberSaveable { mutableStateOf(MovieDetailUI()) }
    var isLoading: Boolean by rememberSaveable { mutableStateOf(false) }

    when (state) {
        is MovieDetailsViewModel.MovieDetailViewState.Error -> {
            Toast.makeText(LocalContext.current, stringResource(R.string.error, (state as MovieDetailsViewModel.MovieDetailViewState.Error).errorCode), Toast.LENGTH_SHORT).show()
        }

        MovieDetailsViewModel.MovieDetailViewState.Idle -> {}
        MovieDetailsViewModel.MovieDetailViewState.Loading -> {
            isLoading = true
        }

        is MovieDetailsViewModel.MovieDetailViewState.Movie -> {
            isLoading = (state as MovieDetailsViewModel.MovieDetailViewState.Movie).isLoading
            movieDetails = (state as MovieDetailsViewModel.MovieDetailViewState.Movie).data
        }
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
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if(isLoading) isLoading = false
                        onBackClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {

                //movie image
                GlideImage(
                    contentScale = ContentScale.Fit,
                    model = stringResource(R.string.https_image_url, movieDetails.posterPath),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = colorResource(id = R.color.black))
                )
                //movie title
                (Text(
                    text = movieDetails.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    color = colorResource(id = R.color.black)
                ))
                //movie overview
                Text(
                    text = movieDetails.overview,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = colorResource(id = R.color.black)
                )
            }
        }
    }
}