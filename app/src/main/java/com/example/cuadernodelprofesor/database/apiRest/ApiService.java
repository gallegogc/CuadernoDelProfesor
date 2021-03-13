package com.example.cuadernodelprofesor.database.apiRest;

import com.example.cuadernodelprofesor.database.bodies.FaltaBody;
import com.example.cuadernodelprofesor.database.bodies.ProfesorBody;
import com.example.cuadernodelprofesor.database.bodies.PostBody;
import com.example.cuadernodelprofesor.database.bodies.ReplyBody;
import com.example.cuadernodelprofesor.database.models.AlumnosResponse;
import com.example.cuadernodelprofesor.database.models.AsignaturasResponse;
import com.example.cuadernodelprofesor.database.models.CursosResponse;
import com.example.cuadernodelprofesor.database.models.EstadoMensajeResponse;
import com.example.cuadernodelprofesor.database.models.FaltasResponse;
import com.example.cuadernodelprofesor.database.models.InsertPostReplyResponse;
import com.example.cuadernodelprofesor.database.models.NotasResponse;
import com.example.cuadernodelprofesor.database.models.PostsResponse;
import com.example.cuadernodelprofesor.database.models.RepliesResponse;
import com.example.cuadernodelprofesor.database.models.LoginResponse;
import com.example.cuadernodelprofesor.database.bodies.LoginBody;
import com.example.cuadernodelprofesor.database.models.InsertProfesorResponse;
import com.example.cuadernodelprofesor.database.models.Profesor;
import com.example.cuadernodelprofesor.database.models.ProfesorOneResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/profesores")
    Call<Profesor> getProfesores();

    @GET("/profesores/id/{id}")
    Call<ProfesorOneResponse> getProfesorID(@Path("id") int idProfesor);

    @Headers({"Accept: application/json"})
    @POST("/profesores/login")
    Call<LoginResponse> getLogin(@Body LoginBody body);

    @Headers({"Accept: application/json"})
    @POST("/profesores/logup")
    Call<InsertProfesorResponse> insertProfesor(@Body ProfesorBody body);


    ///////////////////////////////// CURSOS ////////////////////////////////////

    @GET("/cursos/profesor/{idProfesor}")
    Call<CursosResponse> getCursos(@Path("idProfesor") int idProfesor);


    ///////////////////////////////// ASIGNATURAS ////////////////////////////////////
    // ASIGNATURAS DEL PROFESOR
    @GET("/asignaturas/profesor/{idProfesor}")
    Call<AsignaturasResponse> getAsignaturas(@Path("idProfesor") int idProfesor);

    ///////////////////////////////// ALUMNOS ////////////////////////////////////

    // ALUMNO INDIVIDUAL POR ID
    @GET("/alumnos/alumno/{idAlumno}")
    Call<AlumnosResponse> getAlumno(@Path("idAlumno") int idAlumno);

    // ALUMNOS DEL PROFESOR
    @GET("/alumnos/profesor/{idProfesor}")
    Call<AlumnosResponse> getAlumnosProfesor(@Path("idProfesor") int idProfesor);

    // ALUMNOS DE UNA ASIGNATURA
    @GET("/alumnos/asignatura/{idAsignatura}")
    Call<AlumnosResponse> getAlumnosAsignatura(@Path("idAsignatura") int idAsignatura);

    ///////////////////////////////// NOTAS ////////////////////////////////////

    // NOTAS DE UN ALUMNO
    @GET("/notas/alumno/{idAlumno}")
    Call<NotasResponse> getNotasAlumno(@Path("idAlumno") int idAlumno);

    ///////////////////////////////// FALTAS ////////////////////////////////////

    // FALTAS DE UN ALUMNO
    @GET("/faltas/alumno/{idAlumno}")
    Call<FaltasResponse> getFaltasAlumno(@Path("idAlumno") int idAlumno);

    // JUSTIFICAR UNA FALTA
    @GET("/faltas/justificarDesjustificar/{id}")
    Call<EstadoMensajeResponse> justificarFalta(@Path("id") int idFalta);

    // ELIMINAR UNA FALTA
    @GET("/faltas/eliminar/{id}")
    Call<EstadoMensajeResponse> eliminarFalta(@Path("id") int idFalta);

    // PONER FALTA A UN ALUMNO
    @Headers({"Accept: application/json"})
    @POST("/faltas/")
    Call<EstadoMensajeResponse> insertFalta(@Body FaltaBody body);

    ////////////////////////////////// FORO /////////////////////////////

    @GET("/foropost")
    Call<PostsResponse> getForoPost();

    @GET("/fororeplies/{idPost}")
    Call<RepliesResponse> getForoRepliesID(@Path("idPost") int idForoPost);

    @Headers({"Accept: application/json"})
    @POST("/foropost")
    Call<InsertPostReplyResponse> insertPost(@Body PostBody postBody);

    @Headers({"Accept: application/json"})
    @POST("/fororeplies")
    Call<InsertPostReplyResponse> insertReply(@Body ReplyBody replyBody);

}

