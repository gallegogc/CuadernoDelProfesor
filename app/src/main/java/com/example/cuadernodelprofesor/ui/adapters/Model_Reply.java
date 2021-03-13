package com.example.cuadernodelprofesor.ui.adapters;

public class Model_Reply {
    private String autorReply, contenidoReply, fechaReply;

    public Model_Reply(String autorReply, String contenidoReply) {
        this.autorReply = autorReply;
        this.contenidoReply = contenidoReply;
        this.fechaReply = fechaReply;
    }



    public String getAutorReply() {
        return autorReply;
    }

    public void setAutorReply(String autorReply) {
        this.autorReply = autorReply;
    }

    public String getContenidoReply() {
        return contenidoReply;
    }

    public void setContenidoReply(String contenidoReply) {
        this.contenidoReply = contenidoReply;
    }



}