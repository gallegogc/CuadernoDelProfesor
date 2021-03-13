package com.example.cuadernodelprofesor.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.bodies.ReplyBody;
import com.example.cuadernodelprofesor.database.models.InsertPostReplyResponse;
import com.example.cuadernodelprofesor.database.models.Reply;
import com.example.cuadernodelprofesor.database.models.RepliesResponse;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.ui.adapters.Adapter_Reply;
import com.example.cuadernodelprofesor.ui.adapters.Model_Reply;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cuadernodelprofesor.ui.activities.Activity4_Main.idProfesor;

public class Activity_ForoReply extends AppCompatActivity implements View.OnClickListener {

    // ATRIBUTOS DE LA CARTA POST
    private TextView tituloPost_Reply, autorPost_Reply, autorReply, contenidoPost_Reply, contenidoReply;
    private int idPost_Reply;
    private EditText txtRespuesta;

    private Button btnVolverForoReply, btnAddReply;

    private RecyclerView RVReplies;
    private Adapter_Reply adapterReplies;
    private List<Model_Reply> elementosReply;

    private View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro_reply);

        instanciarElementos();
        recogerBundle();

        reciclerViewReplies();
        rellenarReciclerViewReplies();
    }


    private void instanciarElementos() {
        tituloPost_Reply = (TextView) findViewById(R.id.tituloPost_Reply);
        autorPost_Reply = (TextView) findViewById(R.id.autorPost_Reply);
        contenidoPost_Reply = (TextView) findViewById(R.id.contenidoPost_Reply);

        autorReply = (TextView) findViewById(R.id.autorReply);
        contenidoReply = (TextView) findViewById(R.id.contenidoReply);

        btnVolverForoReply = (Button) findViewById(R.id.btnVolverForoReply);
        btnVolverForoReply.setOnClickListener(this);

        btnAddReply = (Button) findViewById(R.id.btnAddReply);
        btnAddReply.setOnClickListener(this);

        txtRespuesta = (EditText) findViewById(R.id.txtRespuesta);

        v = (View) findViewById(R.id.viewForoReply);
    }

    // MÉTODO QUE RELLENA LA PREGUNTA DEL POST CON LOS DATOS DEL BUNDLE DE LA ACTIVITY ANTERIOR
    private void recogerBundle() {
        try {
            Bundle bundle = this.getIntent().getExtras();
            idPost_Reply = bundle.getInt("idPost", 0);
            tituloPost_Reply.setText(bundle.getString("tituloPost", null));
            autorPost_Reply.setText(bundle.getString("autorPost"), null);
            contenidoPost_Reply.setText(bundle.getString("contenidoPost"), null);
        } catch (Exception e) {
        }
    }

    private void reciclerViewReplies() {
        RVReplies = (RecyclerView) findViewById(R.id.RVReplies);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        RVReplies.setLayoutManager(manager);
    }

    private void rellenarReciclerViewReplies() {
        // REINICIAMOS EL ARRAY LIST DE REPLIES
        elementosReply = new ArrayList<>();

        // INSTANCIAMOS EL ADAPTADOR FUERA DEL HILO, SINO NO PODREMOS AÑADIR EL CONTEXTO Y EL LISTCLICK
        adapterReplies = new Adapter_Reply(elementosReply);

        ApiClient apiClient = new ApiClient(this);
        apiClient.getForoRepliesID(idPost_Reply).enqueue(new Callback<RepliesResponse>() {

            @Override
            public void onResponse(@Nullable Call<RepliesResponse> call, @Nullable Response<RepliesResponse> response) {

                // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                RepliesResponse replyResponse = response.body();
                int i = 0;
                if (replyResponse.getEstado() == 1) {
                    List<Reply> repliesList = replyResponse.getReplies();
                    String autorReply, contenidoReply;

                    for (Reply reply : repliesList) {
                        autorReply = "@" + reply.getUser();
                        contenidoReply = reply.getContenido();
                        elementosReply.add(new Model_Reply(autorReply, contenidoReply));
                    }
                    // CARGAMOS EL Recicler CON EL ADAPTADOR DENTRO DEL HILO
                    RVReplies.setAdapter(adapterReplies);

                } else {
                    Utils.mostrarSnack(v, "No hay respuestas publicadas", "amarillo");
                }
            }

            @Override
            public void onFailure(Call<RepliesResponse> call, Throwable t) {
                Utils.mostrarSnack(v, "La conexión ha fallado", "rojo");
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnVolverForoReply:
                Intent intent = new Intent(this, Activity4_Main.class);
                // MANDAMOS UN BUNDLE CON aviso a foro PARA CARGAR DIRECTAMENTE EL FRAGMENT FORO Y VOLVER A ÉL
                Bundle bundleAviso = new Bundle();
                bundleAviso.putString("aviso", "foro");
                intent.putExtras(bundleAviso);
                startActivity(intent);
                finish();
                break;
            case R.id.btnAddReply:
                String respuesta = txtRespuesta.getText().toString();
                // SUBIR LA RESPUESTA A LA BD
                altaRespuesta();
        }
    }

    private void altaRespuesta() {
        String textoRespuesta = txtRespuesta.getText().toString();
        if (textoRespuesta.length() < 2) {
            Utils.mostrarSnack(v, "Respuesta no escrita o demasiado corta", "amarillo");
        } else {

            int idAutorReply = idProfesor;


            ApiClient apiClient = new ApiClient(this);
            apiClient.insertReply(new ReplyBody(idPost_Reply, idAutorReply, textoRespuesta)).enqueue(new Callback<InsertPostReplyResponse>() {

                @Override
                public void onResponse(@Nullable Call<InsertPostReplyResponse> call, @Nullable Response<InsertPostReplyResponse> response) {

                    // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                    InsertPostReplyResponse insertReplyReponse = response.body();
                    // SI EL ESTADO ES 1, EL REGISTRO HA SIDO CORRECTO
                    if (insertReplyReponse.getEstado() == 1) {
                        Utils.mostrarSnack(v, insertReplyReponse.getMensaje(), "verde");
                        txtRespuesta.setText("");
                    } else {
                        Utils.mostrarSnack(v, insertReplyReponse.getMensaje(), "amarillo");
                    }
                    // RECARGAMOS RECICLERVIEWS
                    rellenarReciclerViewReplies();
                }

                @Override
                public void onFailure(Call<InsertPostReplyResponse> call, Throwable t) {
                    Utils.mostrarSnack(v, "La conexión ha fallado", "rojo");
                }
            });
        }
    }
}
