package com.example.cuadernodelprofesor.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.bodies.FaltaBody;
import com.example.cuadernodelprofesor.database.models.EstadoMensajeResponse;
import com.example.cuadernodelprofesor.others.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Adapter_Pasar_Lista extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private View view;
    private ArrayList<Integer> alumnosIDs;
    private int idAsignatura;

    // RECIBIMOS DOS ARRAY LIST: EL TEXTO Y LOS ID DE ALUMNOS PARA SABER EN CUAL HEMOS HECHO CLIC
    // TAMBIÉN RECIBIMOS EL idAsignatura PARA PONER LA FALTA
    public Adapter_Pasar_Lista(ArrayList<String> list, ArrayList<Integer> alumnosIDs, int idAsignatura, Context context) {
        this.list = list;
        this.context = context;
        this.alumnosIDs = alumnosIDs;
        this.idAsignatura = idAsignatura;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_pasarlista, null);
        }

        // OBJETOS TEXTVIEW CON MUESTREO DE DATOS STRING
        TextView listItemText = (TextView) view.findViewById(R.id.lblNombreApellidosAlumnoPasarLista);
        listItemText.setText(list.get(position));

        // OBJETO IMAGE BUTTON
        ImageButton btnPonerFalta = (ImageButton) view.findViewById(R.id.btnPonerFalta);

        // MANEJO BOTÓN ELIMINAR
        btnPonerFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Confirmación de falta")
                        .setMessage("¿Desea poner falta al alumno?")
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            // EN ESTE MÉTODO ONCLICK MANEJAMOS LA RESPUESTA AFIRMATIVA
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // COGEMOS EL ID DEL ALUMNO AL QUE HAY QUE PONER FALTA DE LA LISTA DE LOS MISMOS
                                int idAlumno = alumnosIDs.get(position);
                                // HACEMOS EL INSERT EN LA BASE DE DATOS
                                ApiClient apiClient = new ApiClient(context);
                                apiClient.insertFalta(new FaltaBody(idAlumno, idAsignatura)).enqueue(new Callback<EstadoMensajeResponse>() {
                                    @Override
                                    public void onResponse(@Nullable Call<EstadoMensajeResponse> call, @Nullable Response<EstadoMensajeResponse> response) {
                                        EstadoMensajeResponse res = response.body();
                                        if (res.getEstado() == 1) {
                                            // A MODO ACLARATORIO, SE AÑADE A LOS DATOS DEL ALUMNO LA CADENA: AUSENCIA CONFIRMADA
                                            // SIEMPRE QUE SE ENCUENTRE EN LA LISTA DE CLASE, SI SALE DESAPARECERÁ ESTE INDICATIVO
                                            list.set(position, list.get(position) + " * AUSENCIA CONFIRMADA");
                                            // RECARGAMOS LA VISTA DE LA LISTA
                                            notifyDataSetChanged();
                                            // MOSTRAMOS EL MENSAJE
                                            Utils.mostrarSnack(view, res.getMensaje(), "verde");
                                        } else if (res.getEstado() == -2) {
                                            // SI LA RESPUESTA ES -2, EL ALUMNO YA TIENE LA FALTA PUESTA EN ESA HORA Y FECHA
                                            // CAMBIAMOS TAMBIÉN LA STRING PARA QUE EL USUARIO VEA QUE LA AUSENCIA YA LA TIENE PUESTA

                                            // PARA QUE NO SE VAYAN AÑADIENDO CADENAS DE AUSENCIA CONFIRMADA UNA DETRÁS DE OTRA
                                            // SI SE CLICA DE MANERA CONSECUTIVA, VAMOS A VALIDAR QUE NO CONTENGA YA ESA CADENA
                                            if (!list.get(position).contains("AUSENCIA CONFIRMADA")) {
                                                list.set(position, list.get(position) + " * AUSENCIA CONFIRMADA");
                                                // RECARGAMOS LA VISTA DE LA LISTA
                                                notifyDataSetChanged();
                                            }

                                            // Y MOSTRAMOS EL MENSAJE CORRESPONDIENTE PARA QUE CONOZCA LA SITUACIÓN
                                            Utils.mostrarSnack(view, res.getMensaje(), "amarillo");
                                        } else {
                                            // SI SE HA PRODUCIDO OTRO TIPO DE ERROR
                                            Utils.mostrarSnack(view, res.getMensaje(), "rojo");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<EstadoMensajeResponse> call, Throwable t) {
                                        Utils.mostrarSnack(view, "La conexión ha fallado ", "rojo");
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

        return view;
    }


}

