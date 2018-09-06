package com.gopalkrath.retrofitsample;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gopalkrath.retrofitsample.Utility.API_KEY;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ApiInterface apiService;
    private static final int PAGE_INDEX = 1;

    private RecyclerView rcvMovies;
    private MoviesAdapter moviesAdapter;
    private List<Movie> movieslst;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        showProgressBar();

        initializeView();

        apiCall(PAGE_INDEX);

    }


    private void initializeView() {
        movieslst = new ArrayList<>();
        rcvMovies = (RecyclerView) findViewById(R.id.movies_rcv);
        rcvMovies.setLayoutManager(new LinearLayoutManager(this));
        moviesAdapter = new MoviesAdapter(movieslst, getApplicationContext());
        rcvMovies.setAdapter(moviesAdapter);
    }

    private void apiCall(int pageIndex) {

        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY, pageIndex);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();
                movieslst.addAll(movies);
                moviesAdapter.notifyDataSetChanged();
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                hideProgressBar();
                Log.e(TAG, t.toString());
                Snackbar.make(rcvMovies,t.toString(),Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void showProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.updateProgressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}
