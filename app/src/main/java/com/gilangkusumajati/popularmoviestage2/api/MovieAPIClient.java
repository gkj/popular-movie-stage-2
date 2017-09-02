package com.gilangkusumajati.popularmoviestage2.api;

import com.gilangkusumajati.popularmoviestage2.BuildConfig;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gilang Kusuma Jati on 7/29/17.
 */

public class MovieAPIClient {
    private static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private static MovieAPIClient client = new MovieAPIClient();

    private MovieAPI api;

    private MovieAPIClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(MovieAPI.class);
    }

    public static MovieAPIClient getInstance() {
        return client;
    }

    public Call<MovieResponse> getPopularMovie() {
        return api.getPopularMovies(API_KEY);
    }

    public Call<MovieResponse> getTopRatedMovie() {
        return api.getTopRatedMovies(API_KEY);
    }

    public Call<MovieReviewResponse> getMovieReview(final int movieId) {
        return api.getMovieReview(movieId, API_KEY);
    }

    public Call<MovieTrailerResponse> getMovieTrailer(final int movieId) {
        return api.getMovieTrailer(movieId, API_KEY);
    }
}
