package com.example.cuadernodelprofesor.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.models.Alumno;
import com.example.cuadernodelprofesor.database.models.AlumnosResponse;
import com.example.cuadernodelprofesor.database.models.Asignatura;
import com.example.cuadernodelprofesor.database.models.AsignaturasResponse;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.ui.activities.Activity_FichaAlumno;
import com.example.cuadernodelprofesor.ui.adapters.Adapter_Alumnos;
import com.example.cuadernodelprofesor.ui.adapters.Model_Alumnos;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cuadernodelprofesor.ui.activities.Activity4_Main.idProfesor;

// CLASE ASOCIADA AL FRAGMENT FAVORITAS
public class AlumnosFragment extends Fragment implements Adapter_Alumnos.ListItemClickListener {

    private View viewAlumnos;
    private AlumnosViewModel mViewModel;
    private TextView lblExplicacion;
    private ArrayList<String> valoresSpinner;
    private Spinner spinnerAsignaturas;
    // LISTA DE IDs DE ASIGNATURAS PARA SABER CUAL SE HA SELECCIONADO
    private ArrayList<Integer> asignaturasIDs;

    private RecyclerView RVAlumnos;
    private Adapter_Alumnos adaptadorAlumnos;
    private List<Model_Alumnos> elementosAlumnos;

    public static AlumnosFragment newInstance() {
        return new AlumnosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewAlumnos = inflater.inflate(R.layout.fragment_alumnos, container, false);

        lblExplicacion = (TextView) viewAlumnos.findViewById(R.id.lblExplicacion);
        Utils.justificarTexto(lblExplicacion);

        esconderTeclado();
        rellenarSpinner();
        reciclerViewAlumnos();

        return viewAlumnos;
    }

    // INCLUYE EL MÉTODO ESCONDER TECLADO DE LA CLASE Utils
    private void esconderTeclado() {
        viewAlumnos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.esconderTeclado(v.getContext(), v);
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AlumnosViewModel.class);
        // TODO: Use the ViewModel
    }

    private void rellenarSpinner() {
        spinnerAsignaturas = (Spinner) viewAlumnos.findViewById(R.id.spinnerAsignaturas);
        asignaturasIDs = new ArrayList<Integer>();
        // REINICIAMOS EL ARRAY LIST DE ASIGNATURAS-CLASES
        valoresSpinner = new ArrayList<String>();
        // AÑADIMOS EL VALOR PREDEFINIDO A MODO DE HINT
        valoresSpinner.add("Seleccione una clase-asignatura aquí");
        // RELLENAMOS EL PRIMER DATO DE IDs COMO NULL
        asignaturasIDs.add(null);

        ApiClient apiClient = new ApiClient(this.getContext());
        apiClient.getAsignaturas(idProfesor).enqueue(new Callback<AsignaturasResponse>() {

            @Override
            public void onResponse(@Nullable Call<AsignaturasResponse> call, @Nullable Response<AsignaturasResponse> response) {

                // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                AsignaturasResponse asignaturasResponse = response.body();
                int i = 0;
                if (asignaturasResponse.getEstado() == 1) {
                    // RECOGEMOS LOS CURSOS EN UNA LISTA
                    List<Asignatura> asignaturasList = asignaturasResponse.getAsignaturas();

                    String nombreAsignatura;
                    for (Asignatura asignatura : asignaturasList) {
                        // AÑADIMOS LA ASIGNATURA AL ARRAY LIST DE VALORES
                        valoresSpinner.add(asignatura.getNombreCurso() + " " + asignatura.getNombre());
                        // Y AÑADIMOS EL ID DE LA ASIGNATURA A LA LISTA PARA SABER EN CUAL HA CLICADO
                        asignaturasIDs.add(Integer.parseInt(asignatura.getId()));
                    }
                } else {
                    Utils.mostrarSnack(viewAlumnos, "No tienes asignaturas dadas de alta", "amarillo");
                }
            }

            @Override
            public void onFailure(Call<AsignaturasResponse> call, Throwable t) {
                Utils.mostrarSnack(viewAlumnos, "La conexión ha fallado", "rojo");
            }
        });

        spinnerAsignaturas.setAdapter(new ArrayAdapter<String>(viewAlumnos.getContext(), android.R.layout.simple_spinner_dropdown_item, valoresSpinner));
        spinnerAsignaturas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!spinnerAsignaturas.getSelectedItem().toString().equals("Seleccione una clase-asignatura aquí")) {
                    // CUANDO TOQUE EN UNA ASIGNATURA, SABREMOS LA POSICIÓN DEL SPINNER Y PODREMOS ACCEDER AL ID DE LA ASIGNATURA
                    rellenarReciclerViewAlumnos(asignaturasIDs.get(position));
                    RVAlumnos.setVisibility(View.VISIBLE);
                } else {
                    RVAlumnos.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void reciclerViewAlumnos() {
        RVAlumnos = (RecyclerView) viewAlumnos.findViewById(R.id.RVAlumnosClases);
        // EL MANAGER VA A INDICAR EN QUÉ FORMA SE RELLENA EL RECYCLER:
        // Linear PARA FORMA DE LISTA
        // Grid PARA FORMA DE REGILLA, CUADRADOS
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        RVAlumnos.setLayoutManager(manager);
    }


    private void rellenarReciclerViewAlumnos(int idAsignatura) {
        // REINICIAMOS EL ARRAY LIST DE ALUMNOS
        elementosAlumnos = new ArrayList<>();
        ArrayList<Integer> listaIds = new ArrayList<Integer>();
        // INSTANCIAMOS EL ADAPTADOR FUERA DEL HILO, SINO NO PODREMOS AÑADIR EL CONTEXTO Y EL LISTCLICK
        adaptadorAlumnos = new Adapter_Alumnos(elementosAlumnos, this);

        ApiClient apiClient = new ApiClient(this.getContext());
        apiClient.getAlumnosAsignatura(idAsignatura).enqueue(new Callback<AlumnosResponse>() {
            @Override
            public void onResponse(@Nullable Call<AlumnosResponse> call, @Nullable Response<AlumnosResponse> response) {

                // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                AlumnosResponse alumnoResponse = response.body();
                if (alumnoResponse.getEstado() == 1) {
                    // RECOGEMOS LOS ALUMNOS EN UNA LISTA
                    List<Alumno> asignaturasList = alumnoResponse.getAlumnos();
                    // RECORREMOS LA LISTA AÑADIENDO OBJETOS ALUMNO
                    for (Alumno alumno : asignaturasList) {
                        elementosAlumnos.add(new Model_Alumnos(Integer.parseInt(alumno.getId()), alumno.getNombre(), alumno.getApellidos()));

                    }
                    // CARGAMOS EL Recicler CON EL ADAPTADOR DENTRO DEL HILO
                    RVAlumnos.setAdapter(adaptadorAlumnos);
                } else {
                    Utils.mostrarSnack(viewAlumnos, alumnoResponse.getMensaje(), "amarillo");
                    RVAlumnos.setAdapter(adaptadorAlumnos);
                }
            }

            @Override
            public void onFailure(Call<AlumnosResponse> call, Throwable t) {
                Utils.mostrarSnack(viewAlumnos, "La conexión ha fallado", "rojo");
            }
        });
    }

    @Override
    public void onListItemClick(int position) {
        Intent fichaIntent = new Intent(getActivity(), Activity_FichaAlumno.class);
        Bundle bundle = new Bundle();

        // LE PASO A LA SIGUIENTE ACTIVITY EN UN BUNDLE EL ALUMNO
        bundle.putInt("idAlumno", elementosAlumnos.get(position).getIdAlumno());
        fichaIntent.putExtras(bundle);

        startActivity(fichaIntent);
        getActivity().finish();
    }
}