package com.y5p.themovie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movieList;
    private List<Movie> originalMovieList; // Tambahkan ini
    private Context context; // Deklarasikan variabel context

    public MovieAdapter(Context context) {
        this.context = context;
        this.movieList = new ArrayList<>();
        this.originalMovieList = new ArrayList<>(); // Tambahkan ini
    }

    public void setData(List<Movie> movies) {
        this.movieList = movies;
        this.originalMovieList = new ArrayList<>(movies); // Tambahkan ini
        notifyDataSetChanged();
    }

    public void addData(List<Movie> movies) {
        this.movieList.addAll(movies);
        this.originalMovieList = new ArrayList<>(movies); // Tambahkan ini
        notifyDataSetChanged();
    }

    public void resetToOriginalData() {
        this.movieList = new ArrayList<>(originalMovieList);
        notifyDataSetChanged();
    }

    public void performSearch(String query) {
        if (query.isEmpty()) {
            resetToOriginalData();
        } else {
            List<Movie> filteredList = filterDataBasedOnQuery(query);
            setData(filteredList);
        }
    }

    private List<Movie> filterDataBasedOnQuery(String query) {
        List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : originalMovieList) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(movie);
            }
        }
        return filteredList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.namaFilm.setText(movie.getTitle());
        holder.descFilm.setText(movie.getDescription());

        // Gunakan Picasso untuk memuat gambar
        Picasso.get().load(movie.getImageUrl()).into(holder.gambarFilm);

        // Tambahkan onClickListener untuk membuka halaman detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Panggil metode atau fungsi untuk membuka halaman detail
                openDetailPage(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gambarFilm;
        TextView namaFilm;
        TextView descFilm;
        TextView tanggalFilm;
        TextView genreFilm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gambarFilm = itemView.findViewById(R.id.gambarFilm);
            namaFilm = itemView.findViewById(R.id.namaFilm);
            descFilm = itemView.findViewById(R.id.descFilm);
            tanggalFilm = itemView.findViewById(R.id.tanggalFilm);
        }
    }

    // Metode untuk membuka halaman detail
    private void openDetailPage(Movie movie) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("description", movie.getDescription());
        intent.putExtra("tanggal", movie.getReleaseDate());
        intent.putExtra("gambar", movie.getBackground());
        intent.putExtra("genre", movie.getGenres());
        intent.putExtra("idFilm",movie.getId());
        context.startActivity(intent);
    }
}
