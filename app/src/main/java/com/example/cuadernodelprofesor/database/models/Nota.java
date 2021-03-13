package com.example.cuadernodelprofesor.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Nota {

    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nombreTarea")
    @Expose
    private String nombreTarea;
    @SerializedName("nombreAsignatura")
    @Expose
    private String nombreAsignatura;
    @SerializedName("puntuacion")
    @Expose
    private String puntuacion;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public void setNombreTarea(String nombreTarea) {
        this.nombreTarea = nombreTarea;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }

}