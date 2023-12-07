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

public class ComingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieApi movieApi;

    private TextInputLayout cariBox;
    private EditText inputCariFilm;
    private List<Movie> originalMovieList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.comingbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if((item.getItemId()) == R.id.homebar) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }else if((item.getItemId()) == R.id.comingbar) {
                    return true;
                }else if((item.getItemId()) == R.id.favoritebar) {
                    startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if((item.getItemId()) == R.id.searchbar){
                    startActivity(new Intent(getApplicationContext(),SearchActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case :
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case  2:
//                        return true;
//                    case  3:
////                        startActivity(new Intent(getApplicationContext(),ProfileUser.class));
////                        overridePendingTransition(0,0);
//                        return true;
//                    case 4:
//                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });

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
            for (int i = 1; i <= 3; i++) {
                Call<MovieResponse> call = movieApi.getComingPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Tambahkan data ke adapter
                            movieAdapter.addData(response.body().getMovies());

                            System.out.println("API data berhasil diambil!");
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
                Call<MovieResponse> call = movieApi.getComingPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Tambahkan data ke adapter
                            for (Movie movie : response.body().getMovies()) {
                                if(movie.getTitle().toLowerCase().contains(search.toLowerCase())){
//                                    System.out.println(movie.getTitle()+"    "+movie.getDescription()+"    "+movie.getReleaseDate()+"    "+movie.getGenres()+"    "+movie.getPosterPath());
                                    movies.add(new Movie(movie.getId(),movie.getTitle(),movie.getDescription(),movie.getReleaseDate(),movie.getGenres(),movie.getPosterPath(),movie.getBackground()));
                                    System.out.println(movie.getTitle());
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