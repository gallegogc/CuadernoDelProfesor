package com.example.cuadernodelprofesor.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.bodies.ProfesorBody;
import com.example.cuadernodelprofesor.database.models.InsertProfesorResponse;
import com.example.cuadernodelprofesor.others.Utils;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// CLASE QUE MANEJA EL COMPORTAMIENTO DEL ACTIVITY REGISTRO
public class Activity3_Registro extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegistro, btnVolver;
    private TextView txtUser, txtPass1, txtPass2, txtNombre, txtApellido;
    private View v;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3_registro);

        elementosGraficos();
    }

    // MÉTODO QUE INSTANCIA LOS ELEMENTOS GRÁFICOS E IMPLEMENTA EL MÉTODO PARA ESCONDER EL TECLADO
    // AL TOCAR SOBRE LA VIEW
    private void elementosGraficos() {

        v = findViewById(R.id.scrollViewRegistro);

        // ASOCIAMOS EL VIEW A UN EVENTO OnTouch QUE NOS SIRVE PARA ESCONDER EL TECLADO CUANDO PULSEMOS FUERA DE BOTONES ETC
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.esconderTeclado(v.getContext(), v);
                return false;
            }
        });

        txtUser = (TextView) findViewById(R.id.txtUserRegistro);
        txtUser.addTextChangedListener(textWatcher);
        txtPass1 = (TextView) findViewById(R.id.txtPass1Registro);
        txtPass1.addTextChangedListener(textWatcher);
        txtPass2 = (TextView) findViewById(R.id.txtPass2Registro);
        txtPass2.addTextChangedListener(textWatcher);
        txtNombre = (TextView) findViewById(R.id.txtNombreRegistro);
        txtNombre.addTextChangedListener(textWatcher);
        txtApellido = (TextView) findViewById(R.id.txtApellidoRegistro);
        txtApellido.addTextChangedListener(textWatcher);

        btnRegistro = (Button) findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(this);
        btnVolver = (Button) findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(this);

    }

    // MÉTODO QUE MANEJA EL CAMBIO EN EL TEXTO DE LOS DISTINTOS INPUT TEXT DEL REGISTRO
    // DE MANERA QUE LLAME AL MÉTODO COMPROBAR LONGITUD DE CAMPOS
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            comprobarLongitudCampos();
        }
    };

    // MÉTODO QUE COMPRUEBA SI LOS CAMPOS ESTÁN RELLENADOS Y ACTIVA EL BOTÓN DE REGISTRO
    private void comprobarLongitudCampos() {
        if (txtUser.getText().length() != 0 && txtPass1.getText().length() != 0 && txtPass2.getText().length() != 0 && txtNombre.getText().length() != 0 && txtApellido.getText().length() != 0) {
            btnRegistro.setEnabled(true);
            btnRegistro.setBackgroundColor(Color.parseColor("#267575"));
            btnRegistro.setTextColor(Color.parseColor("#03DAC5"));
        } else {
            btnRegistro.setEnabled(false);
            btnRegistro.setBackgroundColor(Color.parseColor("#8F8F8F"));
            btnRegistro.setTextColor(Color.parseColor("#B5B5B5"));
        }
    }

    // MÉTODO QUE MANEJA EL CLIC SOBRE EL BOTÓN REGISTRO Y EL BOTÓN VOLVER
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistro:
                eliminarEspacios();
                comprobarContenidoCampos();
                break;
            case R.id.btnVolver:
                goToLogin();
                break;
        }
    }

    // MÉTODO QUE LLAMA AL MÉTODO QUE ELIMINA ESPACIOS INICIALES Y FINALES DE LOS INPUT
    // ÚTIL EN ANDROID YA QUE MUCHOS TECLADOS AÑADEN ESPACIOS AUTOMÁTICAMENTE POR PREDICCIÓN DE ESCRITURA
    private void eliminarEspacios() {
        eliminarEspacios(txtUser);
        eliminarEspacios(txtPass1);
        eliminarEspacios(txtPass2);
        eliminarEspacios(txtNombre);
        eliminarEspacios(txtApellido);
    }

    public void eliminarEspacios(TextView textView) {
        String textoSinEspacios = textView.getText().toString().trim();
        textView.setText(textoSinEspacios);
    }

    // MÉTODO QUE COMPRUEBA EL CONTENIDO DE LOS CAMPOS, VALIDANDO QUE LOS DATOS INTRODUCIDOS TENGAN UN FORMATO CORRECTO
    private void comprobarContenidoCampos() {
        if (!txtPass1.getText().toString().equals(txtPass2.getText().toString())) {
            Utils.mostrarSnack(v, "Las contraseñas no coinciden", "rojo");
        } else if (!Pattern.matches("[A-z]{3,30}", txtNombre.getText().toString()) || !Pattern.matches("[A-z]{3,30}", txtApellido.getText().toString())) {
            Utils.mostrarSnack(v, "El nombre o el apellido tienen un formato no válido, sólo letras sin tildes", "amarillo");
        } else {
            alta();
        }
    }

    // MÉTODO QUE REALIZA EL ALTA DE USUARIO OBTENIENDO LOS DATOS INTRODUCIDOS POR EL USUARIO
    private void alta() {
        // NO ES NECESARIO COMPROBAR SI EL USUARIO YA EXISTE EN LA BASE DE DATOS O EL ID INCREMENTAL
        // AMBAS TAREAS SE REALIZAN EN EL BACKEND

        String userIntroducido = txtUser.getText().toString();
        String passIntroducida = txtPass1.getText().toString();
        String nombreIntroducido = txtNombre.getText().toString();
        String apellidosIntroducidos = txtApellido.getText().toString();

        // INSTANCIAMOS UN OBJETO DEL ApiClient
        ApiClient apiClient = new ApiClient(this);
        // INICIAMOS EL MÉTODO QUE NOS INTERESA, PASÁNDOLE UN OBJETO LoginBody CON LOS PARÁMETROS NECESARIOS
        apiClient.insertProfesor(new ProfesorBody(userIntroducido, passIntroducida, nombreIntroducido, apellidosIntroducidos)).enqueue(new Callback<InsertProfesorResponse>() {

            @Override
            public void onResponse(@Nullable Call<InsertProfesorResponse> call, @Nullable Response<InsertProfesorResponse> response) {

                // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                InsertProfesorResponse insertProfesorResponse = response.body();
                // SI EL ESTADO ES 1, EL REGISTRO HA SIDO CORRECTO
                if (insertProfesorResponse.getEstado() == 1) {
                    Utils.mostrarSnack(v, insertProfesorResponse.getMensaje(), "verde");
                    limpiarCampos();
                } else {
                    Utils.mostrarSnack(v, insertProfesorResponse.getMensaje(), "amarillo");
                }
            }

            @Override
            public void onFailure(Call<InsertProfesorResponse> call, Throwable t) {
                Utils.mostrarSnack(v, "La conexión ha fallado", "rojo");
            }
        });
    }

    // MÉTODO QUE LIMPIA LOS CAMPOS UNA VEZ REGISTRADO EL USUARIO
    private void limpiarCampos() {
        txtUser.setText("");
        txtPass1.setText("");
        txtPass2.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        btnRegistro.setEnabled(false);
        btnRegistro.setBackgroundColor(Color.parseColor("#8F8F8F"));
        btnRegistro.setTextColor(Color.parseColor("#B5B5B5"));
    }


    // MÉTODO PARA CAMBIAR DE ACTIVITY
    private void goToLogin() {
        Intent intent = new Intent(this, Activity2_Login.class);
        this.startActivity(intent);
        this.finish();
    }
}