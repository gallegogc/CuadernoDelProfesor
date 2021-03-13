package com.example.cuadernodelprofesor.ui.adapters;

public class Model_Post {
    private String tituloPost, autorPost, contenidoPost;
    private int idPost;
    public Model_Post(int idPost, String tituloPost, String autorPost, String contenidoPost) {
        this.idPost = idPost;
        this.tituloPost = tituloPost;
        this.autorPost = autorPost;
        this.contenidoPost = contenidoPost;
    }

    public String getTituloPost() {
        return tituloPost;
    }

    public void setTituloPost(String tituloPost) {
        this.tituloPost = tituloPost;
    }

    public String getAutorPost() {
        return autorPost;
    }

    public void setAutorPost(String autorPost) {
        this.autorPost = autorPost;
    }

    public String getContenidoPost() {
        return contenidoPost;
    }

    public void setContenidoPost(String contenidoPost) {
        this.contenidoPost = contenidoPost;
    }


    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }
}