package com.y5p.themovie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LoadActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 2000; // Durasi splash screen dalam milidetik (2 detik)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load); // Gantilah dengan layout splash screen Anda

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadActivity.this, MainActivity.class); // Gantilah dengan aktivitas tujuan setelah splash screen
                startActivity(mainIntent);
                finish(); // Tutup splash screen agar tidak dapat diakses lagi
            }
        }, SPLASH_TIMEOUT);
    }
}
