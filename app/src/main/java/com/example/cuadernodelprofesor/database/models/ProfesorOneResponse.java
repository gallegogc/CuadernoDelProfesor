package com.example.cuadernodelprofesor.database.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfesorOneResponse {

    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("profesor")
    @Expose
    private Profesor profesor;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
