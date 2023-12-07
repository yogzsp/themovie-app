package com.y5p.themovie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private String title;
    private String description;
    private String tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {
            File cachePath = new File(getCacheDir(), "share.jpg");
            cachePath.createNewFile();
            // Lakukan sesuatu dengan file cachePath di sini
        } catch (IOException e) {
            e.printStackTrace();
            // Tangani eksepsi jika terjadi kesalahan
        }

        Intent intent = getIntent();
        tanggal = intent.getStringExtra("tanggal");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        String gambar = intent.getStringExtra("gambar");
        int idMovie = intent.getIntExtra("idFilm", -1);

        System.out.println("id filmnya : " + idMovie);

        TextView titleTextView = findViewById(R.id.title);
        TextView descriptionTextView = findViewById(R.id.desc);
        TextView tanggalView = findViewById(R.id.tanggalFilm);
        ImageView gambarView = findViewById(R.id.gambarPoster);
        ImageView likeButton = findViewById(R.id.likeButton);
        ImageView shareButton = findViewById(R.id.shareButton);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        tanggalView.setText(tanggal);
        Picasso.get().load(gambar).into(gambarView);

        boolean isFavorite = FavoriteID.isFavoriteMovieIdExist(this, idMovie);
        if (isFavorite) {
            System.out.println("Film favorit!");
            likeButton.setImageResource(R.drawable.ic_love_red);
        } else {
            System.out.println("Bukan film favorit.");
            likeButton.setImageResource(R.drawable.ic_love);
        }

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareTask().execute(gambar);
            }
        });

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = FavoriteID.isFavoriteMovieIdExist(DetailActivity.this, idMovie);
                if (isFavorite) {
                    System.out.println("unlike film!");
                    FavoriteID.removeFavoriteMovieId(DetailActivity.this, idMovie);
                    likeButton.setImageResource(R.drawable.ic_love);
                } else {
                    System.out.println("like film");
                    FavoriteID.addFavoriteMovieId(DetailActivity.this, idMovie);
                    likeButton.setImageResource(R.drawable.ic_love_red);
                }
            }
        });
    }

    private class ShareTask extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... params) {
            String imageUrl = params[0];

            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                File cachePath = new File(getCacheDir(), "images");
                cachePath.mkdirs();
                File tempFile = new File(cachePath, "share.jpg");
                tempFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(tempFile);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = input.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                return tempFile;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                shareImage(file);
            } else {
                Toast.makeText(DetailActivity.this, "Gagal membagikan gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void shareImage(File file) {
        try {
            Uri imageUri = FileProvider.getUriForFile(DetailActivity.this, getPackageName() + ".provider", file);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hi aku mengajakmu nonton Film " + title + " yang rilis pada tanggal " + tanggal);

            startActivity(Intent.createChooser(shareIntent, "Bagikan ke:"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DetailActivity.this, "Gagal membagikan gambar", Toast.LENGTH_SHORT).show();
        }
    }
}
