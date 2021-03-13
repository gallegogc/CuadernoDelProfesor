package com.example.cuadernodelprofesor.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.models.Alumno;
import com.example.cuadernodelprofesor.database.models.AlumnosResponse;
import com.example.cuadernodelprofesor.database.models.Falta;
import com.example.cuadernodelprofesor.database.models.FaltasResponse;
import com.example.cuadernodelprofesor.database.models.Nota;
import com.example.cuadernodelprofesor.database.models.NotasResponse;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.ui.adapters.Adapter_Ficha_Faltas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_FichaAlumno extends AppCompatActivity implements View.OnClickListener {

    private Button btnVolverFichaAlumno;
    private TextView txtNombreApellidosAlumno, txtDireccionAlumno, txtTelefonoAlumno, txtEmailAlumno;
    private int idAlumno;

    private View viewFichaAlumno;
    private ListView listViewFaltas, listViewNotas;
    private ArrayList<String> elementosListaFaltas,elementosListaNotas;
    private ArrayAdapter<String> adaptadorNotas, adaptadorFichaFaltasDefecto;
    private Adapter_Ficha_Faltas adaptadorFichaFaltasCustom;

    // LISTA DE IDs DE FALTAS
    private ArrayList<Integer> faltasIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_alumno);

        instanciarElementos();
        recogerBundle();
        cargarDatosAlumno();

        generarListaFaltas();
        generarListaNotas();
    }


    private void instanciarElementos() {
        viewFichaAlumno = (View) findViewById(R.id.viewFichaAlumno);

        txtNombreApellidosAlumno = (TextView) findViewById(R.id.fichaAlumnoNombreApellidos);
        txtDireccionAlumno = (TextView) findViewById(R.id.fichaAlumnoDireccion);
        txtTelefonoAlumno = (TextView) findViewById(R.id.fichaAlumnoTelefono);
        txtEmailAlumno = (TextView) findViewById(R.id.fichaAlumnoEmail);

        btnVolverFichaAlumno = (Button) findViewById(R.id.btnVolverFichaAlumno);
        btnVolverFichaAlumno.setOnClickListener(this);

        listViewNotas = (ListView) findViewById(R.id.listFichaNotas);
    }

    // MÉTODO QUE RELLENA LA PREGUNTA DEL POST CON LOS DATOS DEL BUNDLE DE LA ACTIVITY ANTERIOR
    private void recogerBundle() {
        Bundle bundle = this.getIntent().getExtras();
        idAlumno = bundle.getInt("idAlumno", -1);
    }

    private void cargarDatosAlumno() {

        ApiClient apiClient = new ApiClient(this);
        apiClient.getAlumno(idAlumno).enqueue(new Callback<AlumnosResponse>() {
            @Override
            public void onResponse(@Nullable Call<AlumnosResponse> call, @Nullable Response<AlumnosResponse> response) {

                AlumnosResponse alumnoResponse = response.body();
                if (alumnoResponse.getEstado() == 1) {
                    Alumno alumno = alumnoResponse.getAlumnos().get(0);
                    txtNombreApellidosAlumno.setText("Alumno: " + alumno.getNombre() + " " + alumno.getApellidos());
                    txtDireccionAlumno.setText("Dirección: " + alumno.getDireccion());
                    txtTelefonoAlumno.setText("Teléfono: " + alumno.getTelefono());
                    txtEmailAlumno.setText("Email: " + alumno.getEmail());
                } else {
                    Utils.mostrarSnack(viewFichaAlumno, alumnoResponse.getMensaje(), "amarillo");
                }
            }

            @Override
            public void onFailure(Call<AlumnosResponse> call, Throwable t) {
                Utils.mostrarSnack(viewFichaAlumno, "La conexión ha fallado " + idAlumno, "rojo");
            }
        });

    }


    public void generarListaFaltas() {
        // INSTANCIAMOS EL ARRAY LIST DONDE ALMACENAR LAS FALTAS RECOGIDAS DE LA BD
        elementosListaFaltas = new ArrayList<String>();

        // INSTANCIAMOS EL ARRAY LIST DONDE ALMACENAMOS LOS ID DE FALTAS QUE VA A NECESITAR EL ADAPTER PARA SABER EN CUAL HEMOS HECHO CLIC
        faltasIDs = new ArrayList<Integer>();

        // NECESITAMOS 2 ADAPTADORES DISTINTOS, UNO CUSTOM POR SI HAY FALTAS, PARA AÑADIRLAS Y MOSTRAR LOS BOTONES
        // EL OTRO POR DEFECTO POR SI NO HAY FALTAS PARA AÑADIR UNICAMENTE EL MENSAJE
        adaptadorFichaFaltasCustom = new Adapter_Ficha_Faltas(elementosListaFaltas, faltasIDs, this);
        adaptadorFichaFaltasDefecto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, elementosListaFaltas);

        // INSTANCIAMOS EL LIST VIEW DONDE SE VAN A VISUALIZARL AS FALTAS
        listViewFaltas = (ListView) findViewById(R.id.listFaltasFichaAlumno);

        ApiClient apiClient = new ApiClient(this);
        apiClient.getFaltasAlumno(idAlumno).enqueue(new Callback<FaltasResponse>() {
            @Override
            public void onResponse(@Nullable Call<FaltasResponse> call, @Nullable Response<FaltasResponse> response) {
                FaltasResponse faltasResponse = response.body();
                // SI LA RESPUESTA ES POSITIVA RECOGEMOS LAS FALTAS DE LA RESPUESTA EN UNA LIST
                if (faltasResponse.getEstado() == 1) {
                    List<Falta> faltasList = faltasResponse.getFaltas();
                    // RECORREMOS LAS FALTAS  Y AÑADIMOS AL ARRAY LIST LOS PARÁMETROS QUE NOS INTERESAN DE LA FALTA
                    for (Falta falta : faltasList) {
                        String justFalta;
                        if (falta.getJustificacion().equals("1")){
                                justFalta = "Justificada";
                        } else {
                            justFalta = "No justificada";
                        }
                        // RECORTAMOS EL NOMBRE DE LA ASIGNATURA SI ES MUY LARGO
                        String nombAsignatura = recortarNombre(falta.getNombre());
                        elementosListaFaltas.add(falta.getFecha() + " - " + falta.getHora() + " - " + nombAsignatura + " - " + justFalta);

                        // RELLENAMOS TAMBIÉN EL ARRAY LIST DE ID FALTAS
                        faltasIDs.add(Integer.parseInt(falta.getId()));

                    }
                    // EN ESTE CASO PONEMOS EL CUSTOM
                    listViewFaltas.setAdapter(adaptadorFichaFaltasCustom);

                } else {
                    elementosListaFaltas.add("El alumno no tiene faltas");
                    // EN ESTE CASO EL POR DEFECTO
                    listViewFaltas.setAdapter(adaptadorFichaFaltasDefecto);
                }
            }

            @Override
            public void onFailure(Call<FaltasResponse> call, Throwable t) {
                Utils.mostrarSnack(viewFichaAlumno, "La conexión ha fallado " + idAlumno, "rojo");
            }
        });
    }

    private void generarListaNotas() {
        elementosListaNotas = new ArrayList<String>();
        adaptadorNotas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, elementosListaNotas);

        ApiClient apiClient = new ApiClient(this);
        apiClient.getNotasAlumno(idAlumno).enqueue(new Callback<NotasResponse>() {
            @Override
            public void onResponse(@Nullable Call<NotasResponse> call, @Nullable Response<NotasResponse> response) {
                NotasResponse notasResponse = response.body();
                if (notasResponse.getEstado() == 1) {
                    List<Nota> notasList = notasResponse.getNotas();
                    for (Nota nota : notasList) {
                        // RECORTAMOS EL NOMBRE DE LA TAREA SI ES MUY LARGO
                        String nombTarea = recortarNombre(nota.getNombreTarea());
                        // RECORTAMOS EL NOMBRE DE LA ASIGNATURA SI ES MUY LARGO
                        String nombAsignatura = recortarNombre(nota.getNombreAsignatura());
                        elementosListaNotas.add(nota.getFecha() + " - " + nombTarea + " - " + nombAsignatura + " - Nota: " + nota.getPuntuacion());
                    }
                    listViewNotas.setAdapter(adaptadorNotas);
                } else {
                    elementosListaNotas.add("El alumno no ha recibido aún ninguna nota");
                    listViewNotas.setAdapter(adaptadorNotas);
                }
            }

            @Override
            public void onFailure(Call<NotasResponse> call, Throwable t) {
                Utils.mostrarSnack(viewFichaAlumno, "La conexión ha fallado " + idAlumno, "rojo");
            }
        });
    }

    private String recortarNombre(String cadena){
        if (cadena.length()>19){
            return cadena.substring(0,18)+".";
        } else {
            return cadena;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnVolverFichaAlumno:
                Intent intent = new Intent(this, Activity4_Main.class);
                Bundle bundleAviso = new Bundle();
                bundleAviso.putString("aviso", "fichaAlumno");
                intent.putExtras(bundleAviso);
                startActivity(intent);
                finish();
                break;
            case R.id.btnFichaFaltasJustificar:
                // SI LA FALTA ESTÁ JUSTIFICADA, PREGUNTAR SI QUIERE QUITAR LA JUSTIFICACIÓN
                // SI LA FALTA NO ESTÁ JUSTIFIADA MOSTRARLE UN BOX DIALOG CON EDITTEXT PARA QUE INTRODUZCA EL TEXTO DE LA JUSTIFICACIÓN Y HACER EL
        }

    }
}
