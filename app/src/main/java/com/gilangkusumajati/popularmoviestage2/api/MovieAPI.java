package com.gilangkusumajati.popularmoviestage2.api;

import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Gilang Kusuma Jati on 7/29/17.
 */

public interface MovieAPI {
    String BASE_URL = "https://api.themoviedb.org/3/movie/";

    @GET("popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<MovieReviewResponse> getMovieReview(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<MovieTrailerResponse> getMovieTrailer(@Path("id") int movieId, @Query("api_key") String apiKey);

    enum ImageSize {
        W92 ("w92"),
        W154 ("w154"),
        W185 ("w185"),
        W342 ("w342"),
        W500 ("w500"),
        W780 ("w780"),
        ORIGINAL ("original");

        public static Map<String, ImageSize> typeMapping = new LinkedHashMap<>();
        static {
            typeMapping.put(W92.toString(), W92);
            typeMapping.put(W154.toString(), W154);
            typeMapping.put(W185.toString(), W185);
            typeMapping.put(W342.toString(), W342);
            typeMapping.put(W500.toString(), W500);
            typeMapping.put(W780.toString(), W780);
            typeMapping.put(ORIGINAL.toString(), ORIGINAL);
        }

        public static ImageSize getType(String typeName) {
            if (typeMapping.get(typeName) == null) {
                throw new RuntimeException(String.format("There is no Type mapping with name (%s)"));
            }
            return typeMapping.get(typeName);
        }

        private final String name;

        ImageSize(String s) {
            name = s;
        }

        public boolean equals(String other) {
            return name.toString().equals(other);
        }

        public boolean equals(ImageSize other) {
            return name.toString().equals(other.toString());
        }

        public String toString() {
            return this.name;
        }
    }

}
