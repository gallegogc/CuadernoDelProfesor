package com.example.cuadernodelprofesor.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.models.Profesor;
import com.example.cuadernodelprofesor.database.models.ProfesorOneResponse;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.ui.fragments.AlumnosFragment;
import com.example.cuadernodelprofesor.ui.fragments.ForoPostFragment;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// CLASE QUE MANEJA EL ACTIVITY MAIN
public class Activity4_Main extends AppCompatActivity {

    // ATRIBUTOS DE LA CLASE, TENEMOS EL id DEL USUARIO QUE SE HA LOGUEADO COMO PUBLICO STATIC PARA PODER ACCEDER
    // A ÉL DESDE LOS DISTINTOS FRAGMENTS
    public static int idProfesor;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private View viewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity4_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewMain = (View) findViewById(R.id.viewMain);

        recogerSharedPrefYBundle();
        etiquetaMenu();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_alumnos, R.id.nav_foro, R.id.nav_faltas, R.id.nav_ayuda,R.id.nav_acerca_de)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }

    // MÉTODO QUE RECOGE EL ID DEL PROFESOR LOGUEADO DEL SHARED PREFERENCES, PODRÍA RECOGERSE DEL BUNDLE O CON UNA CONSULTA
    // AL HABER INTRODUCIDO USER Y CONTRASEÑA, PERO POR UTILIZAR TODAS LAS FORMAS POSIBLES
    private void recogerSharedPrefYBundle() {
        SharedPreferences fichero = getSharedPreferences("fichero", Context.MODE_PRIVATE);
        idProfesor = fichero.getInt("id", 0);

        try {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle.getBoolean("control")) {
                Utils.mostrarSnack(findViewById(R.id.nav_view), "Te has logueado automáticamente gracias al fichero shared preferences", "verde");
            }

            // SI EXISTE UNA KEY AVISO Y ES IGUAL A fichaAlumno MOSTRAMOS DIRECTAMENTE EL FRAGMENT ALUMNOS
            if (bundle.getString("aviso").equals("fichaAlumno")) {
                // CREAMOS FRAGMENT Y TRANSACCIÓN
                Fragment fragmentAlumnos = new AlumnosFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // REEMPLAZAMOS UN ELEMENTO EN LA VISTA DEL FRAGMENT INICIAL POR EL FRAGMENT INSTANCIADO
                // LO MEJOR ES QUE NO SEA NI EL DRAWER NI LA VIEW GENERAL DEL ACTIVITY PORQUE ENTONCES NO FUNCIONA EL MENÚ LATERAL
                transaction.replace(R.id.nav_host_fragment, fragmentAlumnos);
                transaction.addToBackStack(null);
                // HACEMOS COMMIT
                transaction.commit();
            }

            // SI EXISTE UNA KEY AVISO Y ES IGUAL A foro MOSTRAMOS DIRECTAMENTE EL FRAGMENT FORO
            if (bundle.getString("aviso").equals("foro")) {
                // CREAMOS FRAGMENT Y TRANSACCIÓN
                Fragment fragmentForo = new ForoPostFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                // REEMPLAZAMOS UN ELEMENTO EN LA VISTA DEL FRAGMENT INICIAL POR EL FRAGMENT INSTANCIADO
                transaction.replace(R.id.nav_host_fragment, fragmentForo);
                transaction.addToBackStack(null);
                // HACEMOS COMMIT
                transaction.commit();
            }
        } catch (Exception e) {
        }
    }


    // MÉTODO QUE MODIFICA LA ETIQUETA DEL MENU LATERAL CON EL NOMBRE DE USUARIO QUE HA INICIADO SESIÓN
    private void etiquetaMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        final TextView lblNombreMenu = (TextView) headerView.findViewById(R.id.lblNombreMenu);


        ApiClient apiClient = new ApiClient(this);
        apiClient.getProfesorID(idProfesor).enqueue(new Callback<ProfesorOneResponse>() {
            @Override
            public void onResponse(@Nullable Call<ProfesorOneResponse> call, @Nullable Response<ProfesorOneResponse> response) {

                ProfesorOneResponse profesorResponse = response.body();

                // SI EL ESTADO ES 1, EL LOGIN ES CORRECTO
                if (profesorResponse.getEstado() == 1) {
                    Profesor profesor = profesorResponse.getProfesor();
                    lblNombreMenu.setText("@"+profesor.getUser());

                } else {
                    Utils.mostrarSnack(viewMain, profesorResponse.getMensaje(), "rojo");
                    lblNombreMenu.setText("Nombre del profesor");
                }
            }

            @Override
            public void onFailure(Call<ProfesorOneResponse> call, Throwable t) {
                Utils.mostrarSnack(viewMain, "La conexión ha fallado", "rojo");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity4__main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // MÉTODO QUE MANEJA EL CLIC SOBRE LAS OPCIONES DEL TOOLBAR SUPERIOR, DONDE TENEMOS CERRAR SESIÓN Y
    // EXPORTAR FICHERO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // CERRAR SESIÓN LLAMA A REINICIAR SHARED PREFENCES Y DEVUELVE AL LOGIN
            case R.id.menuItemCerrarSesion:
                reiniciarSharedPref();
                goTo(new Intent(this, Activity2_Login.class));
                return true;
            case R.id.menuItemExportarFichero:
                // EXPORTAR FICHERO LLAMA AL MÉTODO ESCRIBIR FICHERO
                return true;
        }
        return false;
    }

    // MÉTODO QUE REINICIA EL CONTENIDO DEL SHARED PREFERENCES DE MODO QUE AL VOLVER AL LOGIN NO VA A SALTAR
    // AUTOMÁTICAMENTE AL MAIN
    private void reiniciarSharedPref() {
        SharedPreferences fichero = getSharedPreferences("fichero", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = fichero.edit();
        editor.clear();
        editor.commit();
    }

    // MÉTODO QUE RECIBE UN INTENT DEL LOGIN E INICIALIZA LA ACTIVIDAD
    private void goTo(Intent intent) {
        this.startActivity(intent);
        this.finish();
    }
}