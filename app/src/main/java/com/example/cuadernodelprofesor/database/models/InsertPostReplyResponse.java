package com.example.cuadernodelprofesor.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsertPostReplyResponse {

    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
