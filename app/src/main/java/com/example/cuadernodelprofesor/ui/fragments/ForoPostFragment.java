package com.example.cuadernodelprofesor.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuadernodelprofesor.database.apiRest.ApiClient;
import com.example.cuadernodelprofesor.database.bodies.PostBody;
import com.example.cuadernodelprofesor.database.models.InsertPostReplyResponse;
import com.example.cuadernodelprofesor.database.models.PostsResponse;

import com.example.cuadernodelprofesor.database.models.Post;
import com.example.cuadernodelprofesor.others.Utils;
import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.ui.activities.Activity_ForoReply;
import com.example.cuadernodelprofesor.ui.adapters.Adapter_Post;
import com.example.cuadernodelprofesor.ui.adapters.Model_Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.cuadernodelprofesor.ui.activities.Activity4_Main.idProfesor;

// CLASE ASOCIADA AL FRAGMENT BUSCADOR QUE PERMITE BUSCAR JUEGOS ESCRIBIENDO SU NOMBRE EN UN INPUTTEXT
public class ForoPostFragment extends Fragment implements Adapter_Post.ListItemClickListener, View.OnClickListener {

    private ForoPostViewModel mViewModel;
    private View viewForoPost;
    private TextView txtDescripcion;
    private EditText txtTitulo, txtContenido;
    private Button btnAddPost;
    private RecyclerView RV;
    private Adapter_Post adaptador;
    private List<Model_Post> elementos;

    public static ForoPostFragment newInstance() {
        return new ForoPostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // SE CREA UN ContextThemeWrapper DEL CONTEXTO ORIGINAL DEL ACTIVITY CON EL TEMA ELEGIDO
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        // SE CLONA EL INFLATER UTILIZANDO EL ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        viewForoPost = localInflater.inflate(R.layout.fragment_foro_post, container, false);


        txtTitulo = (EditText) viewForoPost.findViewById(R.id.txtTitulo);
        txtContenido = (EditText) viewForoPost.findViewById(R.id.txtContenido);

        txtDescripcion = (TextView) viewForoPost.findViewById(R.id.txtDescripcionForoPost);
        Utils.justificarTexto(txtDescripcion);

        btnAddPost = (Button) viewForoPost.findViewById(R.id.btnAddPost);
        btnAddPost.setOnClickListener(this);

        esconderTeclado();
        reciclerViewPost();
        rellenarReciclerViewPost();

        return viewForoPost;
    }

    // ASOCIAMOS EL VIEW A UN EVENTO OnTouch QUE NOS SIRVE PARA ESCONDER EL TECLADO CUANDO PULSEMOS FUERA DE BOTONES ETC
    private void esconderTeclado() {
        viewForoPost.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.esconderTeclado(v.getContext(), v);
                return false;
            }
        });
    }


    private void reciclerViewPost() {
        RV = (RecyclerView) viewForoPost.findViewById(R.id.RVPost);
        // EL MANAGER VA A INDICAR EN QUÉ FORMA SE RELLENA EL RECYCLER:
        // Linear PARA FORMA DE LISTA
        // Grid PARA FORMA DE REGILLA, CUADRADOS
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        RV.setLayoutManager(manager);
    }


    private void rellenarReciclerViewPost() {
        // REINICIAMOS EL ARRAY LIST DE POSTS
        elementos = new ArrayList<Model_Post>();

        // INSTANCIAMOS EL ADAPTADOR FUERA DEL HILO, SINO NO PODREMOS AÑADIR EL CONTEXTO Y EL LISTCLICK
        adaptador = new Adapter_Post(elementos, this);

        ApiClient apiClient = new ApiClient(this.getContext());
        apiClient.getForoPost().enqueue(new Callback<PostsResponse>() {

            @Override
            public void onResponse(@Nullable Call<PostsResponse> call, @Nullable Response<PostsResponse> response) {

                // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                PostsResponse postsResponse = response.body();
                int i = 0;
                if (postsResponse.getEstado() == 1) {
                    List<Post> postsList = postsResponse.getPosts();

                    String tituloPost, autorPost, contenidoPost;
                    int idPost;

                    for (Post post : postsList) {
                        idPost = Integer.parseInt(post.getId());
                        tituloPost = post.getTitulo();
                        autorPost = "@" + post.getUser();
                        contenidoPost = post.getContenido();
                        elementos.add(new Model_Post(idPost, tituloPost, autorPost, contenidoPost));
                        i++;
                    }
                    // CARGAMOS EL Recicler CON EL ADAPTADOR DENTRO DEL HILO
                    RV.setAdapter(adaptador);
                } else {
                    Utils.mostrarSnack(viewForoPost, "No hay post publicados", "amarillo");
                }
            }

            @Override
            public void onFailure(Call<PostsResponse> call, Throwable t) {
                Utils.mostrarSnack(viewForoPost, "La conexión ha fallado", "rojo");
            }
        });

    }


    @Override
    public void onListItemClick(int position) {

        Intent fichaIntent = new Intent(getActivity(), Activity_ForoReply.class);
        Bundle bundle = new Bundle();

        // LE PASO A LA SIGUIENTE ACTIVITY EN UN BUNDLE EL POST
        bundle.putInt("idPost", elementos.get(position).getIdPost());
        bundle.putString("tituloPost", elementos.get(position).getTituloPost());
        bundle.putString("autorPost", elementos.get(position).getAutorPost());
        bundle.putString("contenidoPost", elementos.get(position).getContenidoPost());

        fichaIntent.putExtras(bundle);
        startActivity(fichaIntent);
        getActivity().finish();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ForoPostViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAddPost:
                altaPost();
                break;
        }
    }

    private void altaPost() {
        final String titulo = txtTitulo.getText().toString();
        String contenido = txtContenido.getText().toString();
        if (titulo.length() < 3 || contenido.length() < 3) {
            Utils.mostrarSnack(viewForoPost, "Campos sin rellenar o demasiado cortos", "amarillo");
        } else {
            ApiClient apiClient = new ApiClient(this.getContext());
            apiClient.insertPost(new PostBody(idProfesor, titulo, contenido)).enqueue(new Callback<InsertPostReplyResponse>() {

                @Override
                public void onResponse(@Nullable Call<InsertPostReplyResponse> call, @Nullable Response<InsertPostReplyResponse> response) {

                    // IGUALAMOS A UN OBJETO DE NUESTRA CLASE MODELO LOGIN EL CUERPO DE LA RESPUESTA
                    InsertPostReplyResponse insertReplyReponse = response.body();
                    // SI EL ESTADO ES 1, EL REGISTRO HA SIDO CORRECTO
                    if (insertReplyReponse.getEstado() == 1) {
                        Utils.mostrarSnack(viewForoPost, insertReplyReponse.getMensaje(), "verde");
                        txtTitulo.setText("");
                        txtContenido.setText("");
                    } else {
                        Utils.mostrarSnack(viewForoPost, insertReplyReponse.getMensaje(), "amarillo");
                    }
                    // RECARGAMOS RECICLERVIEWS
                    rellenarReciclerViewPost();
                }

                @Override
                public void onFailure(Call<InsertPostReplyResponse> call, Throwable t) {
                    Utils.mostrarSnack(viewForoPost, "La conexión ha fallado", "rojo");
                }
            });
        }
    }

}