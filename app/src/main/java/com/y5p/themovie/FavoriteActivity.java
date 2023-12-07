package com.y5p.themovie;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieApi movieApi;

    private TextInputLayout cariBox;
    private EditText inputCariFilm;
    private List<Movie> originalMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        navbottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.favoritebar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if((item.getItemId()) == R.id.homebar) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if((item.getItemId()) == R.id.comingbar) {
                    startActivity(new Intent(getApplicationContext(), ComingActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }else if((item.getItemId()) == R.id.favoritebar) {
                    return true;
                }else if((item.getItemId()) == R.id.searchbar){
                    startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
//        navbottom


        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.rc_listFIlm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi adapter untuk RecyclerView
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);

        // Inisialisasi Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieApi = retrofit.create(MovieApi.class);

        // Ambil data dari API dan perbarui RecyclerView setelahnya
        fetchData("");

    }



    private void fetchData(String search) {
        if(search.length() < 1) {
            movieAdapter.resetToOriginalData();
            List<Movie> movies = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
//                now playing
                Call<MovieResponse> callNow = movieApi.getNowPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

                callNow.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Tambahkan data ke adapter
                            for (Movie movie : response.body().getMovies()) {
                                if(FavoriteID.isFavoriteMovieIdExist(FavoriteActivity.this, movie.getId())){
//                                    System.out.println(movie.getTitle()+"    "+movie.getDescription()+"    "+movie.getReleaseDate()+"    "+movie.getGenres()+"    "+movie.getPosterPath());
                                    movies.add(new Movie(movie.getId(),movie.getTitle(),movie.getDescription(),movie.getReleaseDate(),movie.getGenres(),movie.getPosterPath(),movie.getBackground()));
                                    System.out.println(movie.getId());
                                }
                            }

                            System.out.println("API data berhasil diambil!");
                            movieAdapter.setData(movies);
                        } else {
                            System.out.println("API response tidak berhasil atau data null.");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        System.out.println("API panggilan gagal. Error: " + t.getMessage());
                    }
                });
//                coming playing

                Call<MovieResponse> callComing = movieApi.getComingPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

                callComing.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (Movie movie : response.body().getMovies()) {
                                if(FavoriteID.isFavoriteMovieIdExist(FavoriteActivity.this, movie.getId())){
//                                    System.out.println(movie.getTitle()+"    "+movie.getDescription()+"    "+movie.getReleaseDate()+"    "+movie.getGenres()+"    "+movie.getPosterPath());
                                    movies.add(new Movie(movie.getId(),movie.getTitle(),movie.getDescription(),movie.getReleaseDate(),movie.getGenres(),movie.getPosterPath(),movie.getBackground()));
                                    System.out.println(movie.getId());
                                }
                            }

                            System.out.println("API data berhasil diambil!");
                            movieAdapter.setData(movies);
                        } else {
                            System.out.println("API response tidak berhasil atau data null.");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        System.out.println("API panggilan gagal. Error: " + t.getMessage());
                    }
                });
            }
        }else{
            List<Movie> movies = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
//                mow playing
                Call<MovieResponse> callNow = movieApi.getNowPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

                callNow.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Tambahkan data ke adapter
                            for (Movie movie : response.body().getMovies()) {
                                if(movie.getTitle().toLowerCase().contains(search.toLowerCase())){
                                    if(FavoriteID.isFavoriteMovieIdExist(FavoriteActivity.this, movie.getId())) {
//                                    System.out.println(movie.getTitle()+"    "+movie.getDescription()+"    "+movie.getReleaseDate()+"    "+movie.getGenres()+"    "+movie.getPosterPath());
                                        movies.add(new Movie(movie.getId(),movie.getTitle(),movie.getDescription(),movie.getReleaseDate(),movie.getGenres(),movie.getPosterPath(),movie.getBackground()));
                                        System.out.println(movie.getId());
                                    }
                                }
                            }

                            System.out.println("API data berhasil diambil!");
                            movieAdapter.setData(movies);
                        } else {
                            System.out.println("API response tidak berhasil atau data null.");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        System.out.println("API panggilan gagal. Error: " + t.getMessage());
                    }
                });
//                coming playing
                Call<MovieResponse> callComing = movieApi.getComingPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

                callComing.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Tambahkan data ke adapter
                            for (Movie movie : response.body().getMovies()) {
                                if(movie.getTitle().toLowerCase().contains(search.toLowerCase())){
                                    if(FavoriteID.isFavoriteMovieIdExist(FavoriteActivity.this, movie.getId())) {
//                                    System.out.println(movie.getTitle()+"    "+movie.getDescription()+"    "+movie.getReleaseDate()+"    "+movie.getGenres()+"    "+movie.getPosterPath());
                                        movies.add(new Movie(movie.getId(),movie.getTitle(),movie.getDescription(),movie.getReleaseDate(),movie.getGenres(),movie.getPosterPath(),movie.getBackground()));
                                        System.out.println(movie.getId());
                                    }
                                }
                            }

                            System.out.println("API data berhasil diambil!");
                            movieAdapter.setData(movies);
                        } else {
                            System.out.println("API response tidak berhasil atau data null.");
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        System.out.println("API panggilan gagal. Error: " + t.getMessage());
                    }
                });
            }
        }
    }
}
