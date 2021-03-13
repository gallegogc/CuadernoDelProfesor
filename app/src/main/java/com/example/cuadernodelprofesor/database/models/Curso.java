package com.example.cuadernodelprofesor.database.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Curso {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("idProfesor")
    @Expose
    private String idProfesor;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("grado")
    @Expose
    private String grado;
    @SerializedName("nivel")
    @Expose
    private String nivel;
    @SerializedName("letra")
    @Expose
    private String letra;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

}
