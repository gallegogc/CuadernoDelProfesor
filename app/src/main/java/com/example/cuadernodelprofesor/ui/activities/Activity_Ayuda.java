package com.example.cuadernodelprofesor.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.others.Utils;

public class Activity_Ayuda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);

        TextView ayuda1 = (TextView) findViewById(R.id.txtAyuda1);
        TextView ayuda2 = (TextView) findViewById(R.id.txtAyuda2);

        Utils.justificarTexto(ayuda1);
        Utils.justificarTexto(ayuda2);
    }
}