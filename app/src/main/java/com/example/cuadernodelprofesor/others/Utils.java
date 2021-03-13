package com.example.cuadernodelprofesor.others;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

// CLASE UTILS PARA QUE CONTIENE 3 MÉTODOS HERRAMIENTA PARA MEJORAR LA PRESENTACIÓN DE LA APP
public class Utils {

    // MÉTODO PARA MOSTRAR UNA BARRA DE MENSAJE EN LUGAR DE USAR TOAST, QUE PUEDEN PASAR MÁS DESAPERCIBIDOS
    // LE PASO LA VISTA DEL LAYOUT DESDE EL QUE SE UTILICE, UNA CADENA DEL TEXTO A MOSTRAR Y UNA CADENA DEL COLOR
    // QUE SE VALORARÁ SI ES ROJO, VERDE O AMARILLO
    public static void mostrarSnack(View v,String texto, String color) {

        final Snackbar snackbar = Snackbar.make(v, texto, Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.BLACK);

        snackbar.setActionTextColor(Color.BLACK);
        snackbar.setAction("De acuerdo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });

        View snackbarView = snackbar.getView();
        if (color.equals("rojo")) {
            snackbarView.setBackgroundColor(Color.parseColor("#FF5353"));
        } else if (color.equals("verde")) {
            snackbarView.setBackgroundColor(Color.parseColor("#93D980"));
        } else {
            snackbarView.setBackgroundColor(Color.parseColor("#C8C153"));
        }
        snackbar.show();
    }

    // MÉTODO PARA ESCONDER EL TECLADO A TOCAR SOBRE UNA VISTA, POR LO QUE TENEMOS QUE PASARLE LA VISTA
    // Y EL CONTEXTO DE DONDE QUERAMOS IMPLEMENTARLO
    public static void esconderTeclado(Context context, View v) {
        // LA VIEW v ESTA DEFINIDA EN LOS ATRIBUTOS
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    // MÉTODO QUE PERMITE JUSTIFICAR EL TEXTO AL ELEMENTO GRÁFICO QUE LO CONTIENE
    // INCLUYE UN CONDICIONAL PARA UTILIZARLO SOLO SI LA VERSIÓN DE LA API DE ANDROID DEL DISPOSITIVO
    // EN EL QUE SE EJECUTE LO ADMITE YA QUE ES SUPERIOR A LA API BASE 16 QUE UTILIZAMOS EN ESTE PROYECTO
    public static void justificarTexto(TextView textView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }

}
