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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.models.EstadoMensajeResponse;
import com.example.cuadernodelprofesor.database.models.Nota;
import com.example.cuadernodelprofesor.database.models.NotasResponse;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.ui.activities.Activity_FichaAlumno;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Adapter_Ficha_Faltas extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private View view;
    private ArrayList<Integer> faltasIDs;

    // RECIBIMOS EL ARRAY LIST DE TEXTO DE LA FALTA, EL ARRAY LIST DE ID DE LAS FALTAS PARA SABER EN CUAL HEMOS HECHO CLIC
    public Adapter_Ficha_Faltas(ArrayList<String> list, ArrayList<Integer> faltasIDs, Context context) {
        this.list = list;
        this.context = context;
        this.faltasIDs = faltasIDs;
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
            view = inflater.inflate(R.layout.list_faltas, null);
        }

        // OBJETOS TEXTVIEW CON MUESTREO DE DATOS STRING
        TextView listItemText = (TextView) view.findViewById(R.id.fichaFichaFaltasTexto);
        listItemText.setText(list.get(position));

        // OBJETOS IMAGEBUTTON
        ImageButton btnEliminarFalta = (ImageButton) view.findViewById(R.id.btnFichaFaltasEliminar);
        ImageButton btnJustificarFalta = (ImageButton) view.findViewById(R.id.btnFichaFaltasJustificar);


        // MANEJO BOTÓN ELIMINAR
        btnEliminarFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Confirmación de eliminación de falta")
                        .setMessage("¿Desea eliminar la falta?")
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            // EN ESTE MÉTODO ONCLICK MANEJAMOS LA RESPUESTA AFIRMATIVA
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // EL ID DE LA FALTA QUE TENEMOS QUE ELIMINAR ES EL DE LA LISTA DE IDs EN LA MISMA POSICIÓN DE LA FALTA EN CUYO TEXTO HEMOS HECHO CLIC
                                int idFalta = faltasIDs.get(position);
                                // HACEMOS EL DELETE EN LA BASE DE DATOS
                                ApiClient apiClient = new ApiClient(context);
                                apiClient.eliminarFalta(idFalta).enqueue(new Callback<EstadoMensajeResponse>() {
                                    @Override
                                    public void onResponse(@Nullable Call<EstadoMensajeResponse> call, @Nullable Response<EstadoMensajeResponse> response) {
                                        EstadoMensajeResponse res = response.body();
                                        if (res.getEstado() == 1) {
                                            // ELIMINAMOS EL ELEMENTO DE LA LISTA VISUALMENTE
                                            list.remove(position);
                                            // RECARGAMOS LA VISTA DE LA LISTA
                                            notifyDataSetChanged();
                                            // MOSTRAMOS EL MENSAJE
                                            Utils.mostrarSnack(view, res.getMensaje(), "verde");

                                        } else {
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


        // MANEJO BOTÓN JUSTIFICAR / DESJUSTIFICAR
        btnJustificarFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmación de edición de falta")
                        .setMessage("¿Desea modificar la justificación de  la falta?")
                        .setIcon(android.R.drawable.ic_menu_save)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            // EN ESTE MÉTODO ONCLICK MANEJAMOS LA RESPUESTA AFIRMATIVA
                            public void onClick(DialogInterface dialog, int whichButton) {

                                // EL ID DE LA FALTA QUE TENEMOS QUE MODIFICAR ES EL DE LA LISTA DE IDs EN LA MISMA POSICIÓN DE LA FALTA EN CUYO TEXTO HEMOS HECHO CLIC
                                int idFalta = faltasIDs.get(position);

                                ApiClient apiClient = new ApiClient(context);
                                apiClient.justificarFalta(idFalta).enqueue(new Callback<EstadoMensajeResponse>() {
                                    @Override
                                    public void onResponse(@Nullable Call<EstadoMensajeResponse> call, @Nullable Response<EstadoMensajeResponse> response) {
                                        EstadoMensajeResponse res = response.body();
                                        // SI EL ESTADO ES 1 O 2 (JUSTIFICACIÓN, DESJUSTIFICACIÓN)
                                        if (res.getEstado() == 1 || res.getEstado() == 2) {
                                            // PARA ACTUALIZAR VISUALMENTE LA FALTA SIN TENER QUE RECARGAR EL ACTIVITY
                                            // HACEMOS UN SPLIT PARA RECOGER LA JUSTIFICACIÓN DE LA FALTA Y LA CAMBIAMOS SEGÚN EL VALOR QUE TENÍA
                                            String[] elementosTextoPrevio = list.get(position).split(" - ");
                                            if (elementosTextoPrevio[3].equals("Justificada")) {
                                                elementosTextoPrevio[3] = "No justificada";
                                            } else {
                                                elementosTextoPrevio[3] = "Justificada";
                                            }
                                            // NUEVO TEXTO FALTA
                                            String textoFalta = elementosTextoPrevio[0] + " - " + elementosTextoPrevio[1] + " - " + elementosTextoPrevio[2] + " - " + elementosTextoPrevio[3];
                                            list.set(position, textoFalta);
                                            // RECARGAMOS LA VISTA DE LA LISTA
                                            notifyDataSetChanged();
                                            // MOSTRAMOS EL MENSAJE
                                            Utils.mostrarSnack(view, res.getMensaje(), "verde");

                                        } else {
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

