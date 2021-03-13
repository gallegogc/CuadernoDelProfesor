package com.example.cuadernodelprofesor.database.bodies;

import com.google.gson.annotations.SerializedName;

public class PostBody {

    @SerializedName("idProfesor")
    private int idProfesor;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("contenido")
    private String contenido;

    public PostBody(int idProfesor, String titulo, String contenido)  {
        this.idProfesor = idProfesor;
        this.titulo = titulo;
        this.contenido = contenido;
    }
}
