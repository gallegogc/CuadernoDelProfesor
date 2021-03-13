package com.example.cuadernodelprofesor.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Falta {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idAlumno")
    @Expose
    private String idAlumno;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("hora")
    @Expose
    private String hora;
    @SerializedName("justificacion")
    @Expose
    private String justificacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

}