package com.example.cuadernodelprofesor.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.others.Utils;

public class Activity_Acerca_De extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        TextView acercade = (TextView) findViewById(R.id.txtAcercaDe1);
        Utils.justificarTexto(acercade);
    }
}