package com.example.cuadernodelprofesor.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.models.Profesor;
import com.example.cuadernodelprofesor.database.models.ProfesorOneResponse;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.ui.activities.Activity4_Main;
import com.example.cuadernodelprofesor.others.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cuadernodelprofesor.ui.activities.Activity4_Main.idProfesor;

// FRAGMENT HOME MOSTRADO EN LA ACTIVITY 4
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View viewHome;
    private TextView lblBienvenida;
    private TextView lbl1, lbl2, lbl3, lbl4, lbl5, lbl6, lbl7;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewHome = inflater.inflate(R.layout.fragment_home, container, false);

        bienvenida();

        // INSTANCIACIÓN DE LOS ELEMENTOS GRÁFICOS, EN ESTE CASO LABELS Y LLAMADA AL MÉTODO JUSTIFICAR TEXTO
        // DE NUESTRA CLASE Utils PARA JUSTIFICAR EL TEXTO A AMBOS LADOS DE SU UBICACIÓN
        lbl1 = (TextView) viewHome.findViewById(R.id.lblFuncionalidades1);
        lbl2 = (TextView) viewHome.findViewById(R.id.lblFuncionalidades2);
        lbl3 = (TextView) viewHome.findViewById(R.id.lblFuncionalidades3);
        Utils.justificarTexto(lbl1);
        Utils.justificarTexto(lbl2);
        Utils.justificarTexto(lbl3);

        return viewHome;
    }

    // MÉTODO QUE MODIFICA EL LABEL BIENVENIDA EN FUNCIÓN DEL NOMBRE Y APELLIDOS DEL USUARIO
    private void bienvenida() {
        lblBienvenida = (TextView) viewHome.findViewById(R.id.lblBienvenido);

        ApiClient apiClient = new ApiClient(this.getContext());
        apiClient.getProfesorID(idProfesor).enqueue(new Callback<ProfesorOneResponse>() {
            @Override
            public void onResponse(@Nullable Call<ProfesorOneResponse> call, @Nullable Response<ProfesorOneResponse> response) {

                ProfesorOneResponse profesorResponse = response.body();

                // SI EL ESTADO ES 1, EL LOGIN ES CORRECTO
                if (profesorResponse.getEstado() == 1) {
                    Profesor profesor = profesorResponse.getProfesor();
                    lblBienvenida.setText("Bienvenid@ " +profesor.getNombre()+" "+profesor.getApellidos());

                } else {
                    Utils.mostrarSnack(viewHome, profesorResponse.getMensaje(), "rojo");
                    lblBienvenida.setText("Bienvenido //Nombre del profesor//");
                }
            }

            @Override
            public void onFailure(Call<ProfesorOneResponse> call, Throwable t) {
                Utils.mostrarSnack(viewHome, "La conexión ha fallado", "rojo");
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }


}