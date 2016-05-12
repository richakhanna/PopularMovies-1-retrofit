package com.richdroid.popularmovies.rest;

import android.content.Context;
import android.util.Log;

import com.richdroid.popularmovies.model.AllMovieResponse;
import com.richdroid.popularmovies.utils.Constants;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by richa.khanna on 5/11/16.
 */
public class ApiManager {

    private static final String TAG = ApiManager.class.getSimpleName();
    //Base Url for TMDB
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String BASE_URL_IMAGE_POSTER = "http://image.tmdb.org/t/p/w185";
    public static final String BASE_URL_IMAGE_BACKDROP = "http://image.tmdb.org/t/p/w780";

    //Key to access TMDB
    public static final String API_KEY = "";

    private static ApiManager mInstance;
    private Context mContext;
    private ApiInterface mApiService;

    private ApiManager(Context context) {
        mContext = context;
    }

    public static synchronized ApiManager getInstance(Context context) {
        if (mInstance == null) {
            Log.v(TAG, "Creating api manager instance");
            mInstance = new ApiManager(context.getApplicationContext());
        }
        return mInstance;
    }

    public void init() {
        mApiService = getApiService();
    }

    private ApiInterface getApiService() {
        if (mApiService == null) {
            mApiService = ApiClient.getClient().create(ApiInterface.class);
        }
        return mApiService;
    }

    /**
     * Get the list of movies from The Movie Database(tmdb). This list refreshes every day.
     *
     * @param wRequester
     */
    public void getMovies(final WeakReference<ApiRequester> wRequester, String movieSortFilter) {
        Log.v(TAG, "Api call : get movies");

        final Callback<AllMovieResponse> objectCallback = new Callback<AllMovieResponse>() {
            @Override
            public void onResponse(Call<AllMovieResponse> call, Response<AllMovieResponse> response) {
                Log.v(TAG, "onResponse : get movies returned a response");

                ApiRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }

                if (req != null) {
                    // response.isSuccessful() is true if the response code is 2xx
                    if (response != null && response.isSuccessful()) {
                        req.onSuccess(response);
                    } else {
                        int statusCode = response.code();
                        // handle response errors yourself
                        ResponseBody errorBody = response.errorBody();
                        try {
                            Log.e(TAG, "onResponse status code : " + statusCode + " , error message : " + errorBody.string());
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse exception message : " + e.getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<AllMovieResponse> call, Throwable t) {
                // handle execution failures like no internet connectivity
                // timeout exception here IOException or SocketTOException
                Log.v(TAG, "onFailure : get movies api failed");

                ApiRequester req = null;
                if (wRequester != null) {
                    req = wRequester.get();
                }
                if (req != null) {
                    req.onFailure(t);
                }
            }
        };

        Call<AllMovieResponse> call;

        if (movieSortFilter.equals(Constants.HIGHEST_RATED)) {
            Log.v(TAG, "Calling : get top rated movies api");
            call = mApiService.getTopRatedMovies(API_KEY);
            call.enqueue(objectCallback);
        } else if (movieSortFilter.equals(Constants.MOST_POPULAR)) {
            Log.v(TAG, "Calling : get popular movies api");
            call = mApiService.getPopularMovies(API_KEY);
            call.enqueue(objectCallback);
        }
    }
}
