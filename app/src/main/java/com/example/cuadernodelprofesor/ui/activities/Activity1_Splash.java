package com.example.cuadernodelprofesor.ui.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cuadernodelprofesor.R;

// CLASE PERTENECIENTE AL ACTIVITY SPLASH
public class Activity1_Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1_splash);

        Intent main = new Intent(this, Activity2_Login.class);
        startActivity(main);
        finish();
    }

}







