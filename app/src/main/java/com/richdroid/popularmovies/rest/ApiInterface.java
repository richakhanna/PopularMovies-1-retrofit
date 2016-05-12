package com.richdroid.popularmovies.rest;

/**
 * Created by richa.khanna on 5/11/16.
 */

import com.richdroid.popularmovies.model.AllMovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<AllMovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/top_rated?api_key=1234

    @GET("movie/popular")
    Call<AllMovieResponse> getPopularMovies(@Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/popular?api_key=1234

    @GET("movie/{id}")
    Call<AllMovieResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
    // http://api.themoviedb.org/3/movie/244786?api_key=1234
}
