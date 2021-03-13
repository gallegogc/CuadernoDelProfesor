package com.example.cuadernodelprofesor.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.apiRest.ApiService;
import com.example.cuadernodelprofesor.database.models.LoginResponse;
import com.example.cuadernodelprofesor.database.bodies.LoginBody;
import com.example.cuadernodelprofesor.database.models.LoginUser;
import com.example.cuadernodelprofesor.others.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// CLASE PERTENECIENTE AL ACTIVITY LOGIN
public class Activity2_Login extends AppCompatActivity implements View.OnClickListener {

    TextView lblAqui;
    Button btnLogin;
    EditText txtUser, txtPass;
    View v;

    private static ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_login);

        elementosGraficos();
        comprobarSharedPref();
    }

    // MÉTODO QUE COMPRUEBA SI EXISTE EL FICHERO SHARED PREFERENCES Y SI CONTIENE UN id, CASO EN EL QUE
    // LANZA DIRECTAMENTE EL ACTIVITY MAIN PARA ACCEDER AUTOMÁTICAMENTE A ÉL CUANDO EL USUARIO YA SE HABÍA
    // LOGUEADO PREVIAMENTE EN EL DISPOSTIIVO
    private void comprobarSharedPref() {
        SharedPreferences fichero = getSharedPreferences("fichero", Context.MODE_PRIVATE);
        if (fichero.contains("id")) {
            Intent intent = new Intent(this, Activity4_Main.class);
            Bundle bundle = new Bundle();

            boolean controlSharedPref = true;

            bundle.putBoolean("control", controlSharedPref);
            intent.putExtras(bundle);
            goToLogupMain(true);
        }
    }

    // MÉTODO QUE INSTANCIA LOS ELEMENTOS GRÁFICOS DEL ACTIVITY
    private void elementosGraficos() {
        v = findViewById(R.id.scrollViewLogin);

        // ASOCIAMOS EL VIEW A UN EVENTO OnTouch QUE NOS SIRVE PARA ESCONDER EL TECLADO CUANDO PULSEMOS FUERA DE BOTONES ETC
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.esconderTeclado(v.getContext(), v);
                return false;
            }
        });

        lblAqui = (TextView) findViewById(R.id.lblAqui);
        lblAqui.setOnClickListener(this);

        txtUser = (EditText) findViewById(R.id.txtUserLogin);
        txtPass = (EditText) findViewById(R.id.txtPassLogin);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    // MANEJADOR DEL EVENTO onClick PARA CONTROLAR EL TOQUE EN EL LABEL PARA ACCEDER AL REGISTRO
    // O EN EL BOTÓN LOGIN PARA INTENTAR INICIAR SESIÓN, EN CUYO CASO SE ELIMINAN LOS ESPACIOS ANTERIORES
    // Y POSTERIORES A LOS DATOS INTRODUCIDOS POR EL USUARIO Y SE COMPRUEBAN LOS DATOS EN LA BASE DE DATOS
    // MEDIANTE LA LLAMADA A DOS MÉTODOS CORRESPONDIENTES
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lblAqui:
                goToLogupMain(false);
                break;

            case R.id.btnLogin:
                eliminarEspacios(txtUser);
                eliminarEspacios(txtPass);
                Utils.esconderTeclado(this, v);
                if (txtUser.getText().length() != 0 && txtPass.getText().length() != 0) {
                    login();
                } else {
                    Utils.mostrarSnack(v, "Al menos un campo está vacío", "yellow");
                }
                break;
        }
    }

    // MÉTODO QUE ELIMINA ESPACIOS INICIALES Y FINALES DE LOS INPUT
    // ÚTIL EN ANDROID YA QUE MUCHOS TECLADOS AÑADEN ESPACIOS AUTOMÁTICAMENTE POR PREDICCIÓN DE ESCRITURA
    public void eliminarEspacios(TextView textView) {
        String textoSinEspacios = textView.getText().toString().trim();
        textView.setText(textoSinEspacios);
    }

    // MÉTODO QUE COMPRUEBA LOS DATOS INTRODUCIDOS EN LA BASE DE DATOS Y EN CASO AFIRMATIVO REALIZA EL LOGIN
    // ACCEDIENDO AL ACTIVITY MAIN, EN CASO CONTRARIO MUESTRA UN SNACKBAR, DETALLADO EN LA CLASE Utils
    private void login() {

        final String userIntroducido = txtUser.getText().toString();
        final String passIntroducida = txtPass.getText().toString();

        // INSTANCIAMOS UN OBJETO DEL ApiClient
        ApiClient apiClient = new ApiClient(this);
        // INICIAMOS EL MÉTODO QUE NOS INTERESA, PASÁNDOLE UN OBJETO LoginBody CON LOS PARÁMETROS NECESARIOS
        apiClient.getLogin(new LoginBody(userIntroducido, passIntroducida)).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(@Nullable Call<LoginResponse> call, @Nullable Response<LoginResponse> response) {

                // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                LoginResponse loginResponse = response.body();

                // SI EL ESTADO ES 1, EL LOGIN ES CORRECTO
                if (loginResponse.getEstado() == 1) {
                    // RECOGEMOS EL OBJETO USUARIO LOGUEADO
                    LoginUser usuarioLogin = loginResponse.getLoginUser();
                    // RECOGEMOS SU ID
                    int idUsuario = Integer.parseInt(usuarioLogin.getId());
                    // LO PASAMOS AL SHARED PREFERENCES PARA POSTERIORMENTE PASAR AL LOGIN
                    guardarSharedPref(idUsuario);
                    goToLogupMain(true);
                } else {
                    Utils.mostrarSnack(v, loginResponse.getMensaje(), "rojo");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Utils.mostrarSnack(v, "La conexión ha fallado", "rojo");
        }
        });

    }

    // MÉTODO QUE INSTANCIA EL FICHERO SHARED PREFERENCES Y ESCRIBE EN ÉL EL ID DE USUARIO QUE HA INICIADO SESIÓN
    // JUNTO CON LA CLAVE "id" PARA UTILIZARLO POSTERIORMENTE Y ACCEDER AUTOMÁTICAMENTE COMO YA SE HA INDICADO

    private void guardarSharedPref(int idProfesor) {
        SharedPreferences fichero = getSharedPreferences("fichero", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = fichero.edit();
        editor.putInt("id", idProfesor);
        editor.commit();
    }

    // MËTODO goTo PARA INICIAR UN ACTIVITY
    private void goToLogupMain(Boolean flag) {
        Intent intent;
        if (flag) {
            intent = new Intent(this, Activity4_Main.class);
        } else {
            intent = new Intent(this, Activity3_Registro.class);
        }
        this.startActivity(intent);
        this.finish();
    }
}