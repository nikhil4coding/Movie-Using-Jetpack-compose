package com.movies.di

import com.google.gson.Gson
import com.movies.data.MovieRepositoryImpl
import com.movies.data.api.MovieService
import com.movies.domain.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideMovieRepository(movieService: MovieService): MovieRepository {
        return MovieRepositoryImpl(movieService)
    }

    @Provides
    @Singleton
    fun provideMovieService(client: OkHttpClient): MovieService {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(client)
            .build()
            .create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(createAuthInterceptor())
            .addInterceptor(interceptor)
            .build()
    }

    private fun createAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader(
                        "Authorization",
                        "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkZjc1NmNmNjQwNjNmY2U2Y2I0YjRmMjI3YmI2MDA3OSIsIm5iZiI6MTcyNjY3MjkyMi4yODU4ODUsInN1YiI6IjY2ZTNmYjBlMDAwMDAwMDAwMGI5ODQzOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.hQqooGOy-na8eGUSJsgTMvY9TQP7trcpO4V8zGzdD4w"
                    )
                    .build()
            )
        }
    }
}