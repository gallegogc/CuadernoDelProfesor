package com.example.cuadernodelprofesor.database.bodies;

import com.google.gson.annotations.SerializedName;

public class ReplyBody {

    @SerializedName("idPost")
    private int idPost;

    @SerializedName("idAutor")
    private int idAutor;

    @SerializedName("contenido")
    private String contenido;

    public ReplyBody(int idPost, int idAutor, String contenido)  {
        this.idPost = idPost;
        this.idAutor = idAutor;
        this.contenido = contenido;
    }
}
