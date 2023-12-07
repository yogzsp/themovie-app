package com.y5p.themovie;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private MovieApi movieApi;

    private TextInputLayout cariBox;
    private ScrollView listFilm;
    private ImageView buttonSearch;
    private AutoCompleteTextView inputCariFilm;
    private List<Movie> originalMovieList;

    private SearchHistoryManager searchHistoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        navbottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.searchbar);

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
                    startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }else if((item.getItemId()) == R.id.searchbar){
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


        // Tambahkan listener untuk TextInputLayout atau EditText
        cariBox = findViewById(R.id.cariBox);
        inputCariFilm = findViewById(R.id.inputCariFilm);
        listFilm = findViewById(R.id.menuFilm);
        buttonSearch = findViewById(R.id.buttonSearch);
        searchHistoryManager = new SearchHistoryManager(this);
// ...

        inputCariFilm.setOnItemClickListener((adapterView, view, position, id) -> {
            // Lakukan sesuatu dengan item yang dipilih
            Toast.makeText(SearchActivity.this, "Selected: ", Toast.LENGTH_SHORT).show();
        });



        inputCariFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchHistoryDropdown();
            }
        });

        inputCariFilm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(inputCariFilm.getText().toString().length() < 3) {
                    showSearchHistoryDropdown();
                }
            }
        });

        inputCariFilm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    saveHistory(inputCariFilm.getText().toString().trim());
                    String query = inputCariFilm.getText().toString();
                    System.out.println(query);
                    fetchData(query);
                    return true;
                }
                return false;
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputCariFilm.getText().toString().length() >= 3) {
                    saveHistory(inputCariFilm.getText().toString().trim());
                    String query = inputCariFilm.getText().toString();
                    System.out.println(query);
                    fetchData(query);
                }
            }
        });

    }



    private void fetchData(String search) {
        if(search.length() < 1) {
            movieAdapter.resetToOriginalData();
            for (int i = 1; i <= 3; i++) {
                Call<MovieResponse> call = movieApi.getNowPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

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
                Call<MovieResponse> call = movieApi.getNowPlayingMovies("8b928f53dfdbee6e37d2f099e93db32f", i, "en-US", "US");

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

    private void saveHistory(String dataHistory){
        System.out.println("berhasil save");
        searchHistoryManager.saveSearchHistory(dataHistory);
    }

    private void showSearchHistoryDropdown() {
        List<String> searchHistoryList = searchHistoryManager.getSearchHistory();
        Collections.reverse(searchHistoryList); // Balik urutan list

        // Buat adapter untuk AutoCompleteTextView
        ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchHistoryList);

        // Set adapter pada AutoCompleteTextView
        inputCariFilm.setAdapter(historyAdapter);

        // Tampilkan dropdown
        inputCariFilm.showDropDown();
    }


}
