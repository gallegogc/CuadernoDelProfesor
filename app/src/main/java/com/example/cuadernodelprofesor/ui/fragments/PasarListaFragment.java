package com.example.cuadernodelprofesor.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.models.Alumno;
import com.example.cuadernodelprofesor.database.models.AlumnosResponse;
import com.example.cuadernodelprofesor.database.models.Asignatura;
import com.example.cuadernodelprofesor.database.models.AsignaturasResponse;
import com.example.cuadernodelprofesor.database.models.Falta;
import com.example.cuadernodelprofesor.database.models.FaltasResponse;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.ui.adapters.Adapter_Ficha_Faltas;
import com.example.cuadernodelprofesor.ui.adapters.Adapter_Pasar_Lista;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cuadernodelprofesor.ui.activities.Activity4_Main.idProfesor;

// CLASE ASOCIADA AL FRAGMENT DESARROLLADORAS
public class PasarListaFragment extends Fragment {

    private PasarListaViewModel mViewModel;
    private View viewPasarLista;

    ///////////////// ATRIBUTOS RELACIONADOS CON SPINNER SELECTOR DE CLASES ////////////////////////

    // OBJETO DEL SPINNER
    private Spinner spinnerClases;
    // LISTA CON LOS VALORES STRING DEL SPINNER
    private ArrayList<String> valoresSpinner;
    // LISTA CON LOS ID DE LA CLASE SELECCIONADA, PARA SABER CUÁL HA SELECCIONADO EL USUARIO Y
    // RECOGER LOS ALUMNOS DE LA MISMA SIN TENER QUE MOSTRAR EL ID EN EL SPINNER
    private ArrayList<Integer> clasesIDs;


    ////////////////////       ATRIBUTOS RELACIONADOS CON LISTA PASAR LISTA    /////////////////////

    // LAYOUT QUE CONTIENE LA LISTA, NECESITAMOS EL OBJETO PARA MOSTRARLO Y OCULTARLO
    private ConstraintLayout layoutListaPasarLista;
    // OBJETO LIST VIEW QUE CONTENDRÁ LA LISTA PASAR LISTA
    private ListView listViewPasarLista;
    // ADAPTADOR CUSTOM PARA PASAR LSITA
    private Adapter_Pasar_Lista adaptadorListaPasarListaCUSTOM;
    // ADAPTADOR POR DEFECTO PARA PASAR LISTA, CUANDO NO SE RECOJAN ALUMNOS MATRICULADOS
    private ArrayAdapter<String> adaptadorListaPasarListaDEFECTO;
    // LISTA QUE CONTENDRÁ NOMBRES Y APELLIDOS A MOSTRAR EN LA LISTA DE PASAR LISTA
    private ArrayList<String> elementosListaPasarLista;
    // LISTA DE IDs DE ALUMNOS PARA SABER CUAL SE HA SELECCIONADO
    private ArrayList<Integer> alumnosIDs;

    public static PasarListaFragment newInstance() {
        return new PasarListaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewPasarLista = inflater.inflate(R.layout.fragment_pasar_lista, container, false);
        layoutListaPasarLista = viewPasarLista.findViewById(R.id.layoutListaPasarListaFaltas);

        // TEXTVIEW DESCRIPCIÓN Y LLAMADA A LA FUNCIÓN JUSTIFICAR TEXTO
        TextView descripcion = (TextView) viewPasarLista.findViewById(R.id.lblDescripcionPasarLista);
        Utils.justificarTexto(descripcion);

        esconderLayoutLista();
        rellenarSpinner();
        return viewPasarLista;
    }

    private void esconderLayoutLista() {
        layoutListaPasarLista.setVisibility(View.INVISIBLE);
    }

    private void mostrarLayoutLista() {
        layoutListaPasarLista.setVisibility(View.VISIBLE);
    }

    // MÉTODO QUE RELLENAR EL SPINNER CON EL NOMBRE DE LAS ASIGNATURAS-CLASES
    private void rellenarSpinner() {
        spinnerClases = (Spinner) viewPasarLista.findViewById(R.id.spinnerClasesPasarLista);
        clasesIDs = new ArrayList<Integer>();
        // REINICIAMOS EL ARRAY LIST DE ASIGNATURAS-CLASES
        valoresSpinner = new ArrayList<String>();
        // AÑADIMOS EL VALOR PREDEFINIDO A MODO DE HINT
        valoresSpinner.add("Seleccione una clase-asignatura aquí");
        // RELLENAMOS EL PRIMER DATO DE IDs COMO NULL DEBIDO AL HINT AÑADIDO
        clasesIDs.add(null);

        ApiClient apiClient = new ApiClient(this.getContext());
        apiClient.getAsignaturas(idProfesor).enqueue(new Callback<AsignaturasResponse>() {

            @Override
            public void onResponse(@Nullable Call<AsignaturasResponse> call, @Nullable Response<AsignaturasResponse> response) {

                AsignaturasResponse asignaturasResponse = response.body();
                int i = 0;
                if (asignaturasResponse.getEstado() == 1) {
                    List<Asignatura> asignaturasList = asignaturasResponse.getAsignaturas();
                    String nombreAsignatura;
                    for (Asignatura asignatura : asignaturasList) {
                        // AÑADIMOS LA ASIGNATURA AL ARRAY LIST DE VALORES
                        valoresSpinner.add(asignatura.getNombreCurso() + " " + asignatura.getNombre());
                        // Y AÑADIMOS EL ID DE LA ASIGNATURA A LA LISTA PARA SABER EN CUAL HA CLICADO
                        clasesIDs.add(Integer.parseInt(asignatura.getId()));
                    }
                } else {
                    Utils.mostrarSnack(viewPasarLista, "No tienes asignaturas dadas de alta", "amarillo");
                }
            }

            @Override
            public void onFailure(Call<AsignaturasResponse> call, Throwable t) {
                Utils.mostrarSnack(viewPasarLista, "La conexión ha fallado", "rojo");
            }
        });

        // UNA VEZ REALIZADA LA CONSULTA ASOCIAMOS EL ADAPTADOR AL SPINNER
        spinnerClases.setAdapter(new ArrayAdapter<String>(viewPasarLista.getContext(), android.R.layout.simple_spinner_dropdown_item, valoresSpinner));
        // LE DAMOS FUNCIONALIDAD AL CLIC
        spinnerClases.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!spinnerClases.getSelectedItem().toString().equals("Seleccione una clase-asignatura aquí")) {
                    // CUANDO TOQUE EN UNA CLASE, SABREMOS LA POSICIÓN DEL SPINNER Y PODREMOS ACCEDER AL ID DE LA ASIGNATURA
                    mostrarLayoutLista();
                    // RELLENAMOS LA LISTA DE ALUMNOS CON EL ID DE LA CLASE SELECCIONADA
                    rellenarListPasarLista(clasesIDs.get(position));
                } else {
                    esconderLayoutLista();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                esconderLayoutLista();
            }

        });
    }

    // MÉTODO QUE RELLENA LA LISTA CON LOS ALUMNOS DE LA CLASE, TAMBIÉN NOS SIRVE EL idAsignatura PARA SABER EN CUÁL SE PONE LA FALTA
    private void rellenarListPasarLista(int idAsignatura) {
        // INSTANCIAMOS EL ARRAY LIST DONDE ALMACENAR LOS NOMBRES DE LOS ALUMNOS DE LA CLASE RECOGIDOS DE LA BD
        elementosListaPasarLista = new ArrayList<String>();

        // INSTANCIAMOS EL ARRAY LIST DONDE ALMACENAMOS LOS ID DE ALUMNOS, QUE VA A NECESITAR EL ADAPTER PARA SABER EN QUÉ ALUMNO HEMOS HECHO CLIC PARA PONER LA FALTA
        alumnosIDs = new ArrayList<Integer>();

        // NECESITAMOS 2 ADAPTADORES DISTINTOS, UNO CUSTOM POR SI HAY FALTAS, PARA AÑADIRLAS Y MOSTRAR LOS BOTONES
        // EL OTRO POR DEFECTO POR SI NO HAY FALTAS PARA AÑADIR UNICAMENTE EL MENSAJE
        adaptadorListaPasarListaCUSTOM = new Adapter_Pasar_Lista(elementosListaPasarLista, alumnosIDs, idAsignatura, this.getContext());
        adaptadorListaPasarListaDEFECTO = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, elementosListaPasarLista);

        // INSTANCIAMOS EL LIST VIEW DONDE SE VAN A VISUALIZARL AS FALTAS
        listViewPasarLista = (ListView) viewPasarLista.findViewById(R.id.listPasarLista);

        ApiClient apiClient = new ApiClient(this.getContext());
        apiClient.getAlumnosAsignatura(idAsignatura).enqueue(new Callback<AlumnosResponse>() {
            @Override
            public void onResponse(@Nullable Call<AlumnosResponse> call, @Nullable Response<AlumnosResponse> response) {
                AlumnosResponse alumnosResponse = response.body();
                // SI LA RESPUESTA ES POSITIVA RECOGEMOS LOS ALUMNOS DE LA RESPUESTA EN UNA LIST
                if (alumnosResponse.getEstado() == 1) {
                    List<Alumno> alumnosList = alumnosResponse.getAlumnos();
                    // RECORREMOS LOS ALUMNOS Y AÑADIMOS AL ARRAY LIST LOS PARÁMETROS QUE NOS INTERESAN DEL ALUMNO
                    for (Alumno alumno : alumnosList) {
                        // RELLENAMOS EL ARRAY DE STRINGS QUE VAMOS A MOSTRAR EN LA LISTA OCN EL NOMBRE Y EL APELLIDO DEL ALUMNO
                        elementosListaPasarLista.add(alumno.getNombre() + " " + alumno.getApellidos());
                        // RELLENAMOS TAMBIÉN EL ARRAY LIST DE ID ALUMNOS, PASAR SABER EN CÚAL SE CLICA Y SE PONE LA FALTA
                        alumnosIDs.add(Integer.parseInt(alumno.getId()));
                    }
                    // ASIGNAMOS EL ADAPTADOR CUSTOM AL SPINNER PARA QUE APAREZCAN LOS NOMBRES Y APELLIDOS JUNTO CON EL BOTÓN ICONO PARA PONER LA FALTA
                    listViewPasarLista.setAdapter(adaptadorListaPasarListaCUSTOM);
                } else {
                    // SI NO SE HAN RECOGIDO ALUMNOS MATRICULADOS AÑADIMOS UN MENSAJE A LA LISTA
                    elementosListaPasarLista.add("La clase no tiene alumnos matriculados");
                    // Y UTILIZAMOS EL ADAPTADOR POR DEFECTO
                    listViewPasarLista.setAdapter(adaptadorListaPasarListaDEFECTO);
                }
            }

            @Override
            public void onFailure(Call<AlumnosResponse> call, Throwable t) {
                Utils.mostrarSnack(viewPasarLista, "La conexión ha fallado", "rojo");
            }
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PasarListaViewModel.class);
        // TODO: Use the ViewModel
    }


}