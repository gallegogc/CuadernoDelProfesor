package com.example.cuadernodelprofesor.database.apiRest;

import android.content.Context;

import com.example.cuadernodelprofesor.database.bodies.FaltaBody;
import com.example.cuadernodelprofesor.database.bodies.ProfesorBody;
import com.example.cuadernodelprofesor.database.bodies.PostBody;
import com.example.cuadernodelprofesor.database.models.AlumnosResponse;
import com.example.cuadernodelprofesor.database.models.AsignaturasResponse;
import com.example.cuadernodelprofesor.database.models.CursosResponse;
import com.example.cuadernodelprofesor.database.models.EstadoMensajeResponse;
import com.example.cuadernodelprofesor.database.models.FaltasResponse;
import com.example.cuadernodelprofesor.database.models.InsertPostReplyResponse;
import com.example.cuadernodelprofesor.database.models.NotasResponse;
import com.example.cuadernodelprofesor.database.models.PostsResponse;
import com.example.cuadernodelprofesor.database.bodies.ReplyBody;
import com.example.cuadernodelprofesor.database.models.RepliesResponse;
import com.example.cuadernodelprofesor.database.models.LoginResponse;
import com.example.cuadernodelprofesor.database.bodies.LoginBody;
import com.example.cuadernodelprofesor.database.models.InsertProfesorResponse;
import com.example.cuadernodelprofesor.database.models.ProfesorOneResponse;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://4a7a63a664.gclientes.com";
    private Retrofit retrofit;
    private Context context;


    public ApiClient(Context context) {
        this.context = context;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<LoginResponse> getLogin(LoginBody body) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getLogin(body);
    }

    public Call<InsertProfesorResponse> insertProfesor(ProfesorBody body) {
        ApiService service = retrofit.create(ApiService.class);
        return service.insertProfesor(body);
    }

    public Call<ProfesorOneResponse> getProfesorID(int idProfesor) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getProfesorID(idProfesor);
    }


    ///////////////////////////////// CURSOS ////////////////////////////////////
    public Call<CursosResponse> getCursos(int idProfesor) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getCursos(idProfesor);
    }


    ///////////////////////////////// ASIGNATURAS ////////////////////////////////////
    public Call<AsignaturasResponse> getAsignaturas(int idProfesor) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getAsignaturas(idProfesor);
    }


    ///////////////////////////////// ALUMNOS ////////////////////////////////////

    // ALUMNO POR ID
    public Call<AlumnosResponse> getAlumno(int idAlumno) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getAlumno(idAlumno);
    }

    // ALUMNOS DEL PROFESOR
    public Call<AlumnosResponse> getAlumnosProfesor(int idProfesor) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getAlumnosProfesor(idProfesor);
    }

    // ALUMNOS DE UNA ASIGNATURA
    public Call<AlumnosResponse> getAlumnosAsignatura(int idAsignatura) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getAlumnosAsignatura(idAsignatura);
    }


    ///////////////////////////////// NOTAS ////////////////////////////////////

    // NOTAS POR ID DE ALUMNO
    public Call<NotasResponse> getNotasAlumno(int idAlumno) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getNotasAlumno(idAlumno);
    }

    ///////////////////////////////// FALTAS ////////////////////////////////////

    // FALTAS POR ID DE ALUMNO
    public Call<FaltasResponse> getFaltasAlumno(int idAlumno) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getFaltasAlumno(idAlumno);
    }

    public Call<EstadoMensajeResponse> insertFalta(FaltaBody body) {
        ApiService service = retrofit.create(ApiService.class);
        return service.insertFalta(body);
    }

    public Call<EstadoMensajeResponse> justificarFalta(int idFalta) {
        ApiService service = retrofit.create(ApiService.class);
        return service.justificarFalta(idFalta);
    }

    public Call<EstadoMensajeResponse> eliminarFalta(int idFalta) {
        ApiService service = retrofit.create(ApiService.class);
        return service.eliminarFalta(idFalta);
    }

    ///////////////////////////////////     FORO   ///////////////////////////////////

    public Call<PostsResponse> getForoPost() {
        ApiService service = retrofit.create(ApiService.class);
        return service.getForoPost();
    }

    public Call<RepliesResponse> getForoRepliesID(int idForoPost) {
        ApiService service = retrofit.create(ApiService.class);
        return service.getForoRepliesID(idForoPost);
    }

    public Call<InsertPostReplyResponse> insertPost(PostBody postBody) {
        ApiService service = retrofit.create(ApiService.class);
        return service.insertPost(postBody);
    }

    public Call<InsertPostReplyResponse> insertReply(ReplyBody replyBody) {
        ApiService service = retrofit.create(ApiService.class);
        return service.insertReply(replyBody);
    }


}