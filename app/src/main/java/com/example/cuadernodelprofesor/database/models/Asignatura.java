package com.example.cuadernodelprofesor.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Asignatura {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nombreCurso")
    @Expose
    private String nombreCurso;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("hora1")
    @Expose
    private String hora1;
    @SerializedName("hora2")
    @Expose
    private String hora2;
    @SerializedName("hora3")
    @Expose
    private String hora3;
    @SerializedName("hora4")
    @Expose
    private String hora4;
    @SerializedName("hora5")
    @Expose
    private String hora5;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora1() {
        return hora1;
    }

    public void setHora1(String hora1) {
        this.hora1 = hora1;
    }

    public String getHora2() {
        return hora2;
    }

    public void setHora2(String hora2) {
        this.hora2 = hora2;
    }

    public String getHora3() {
        return hora3;
    }

    public void setHora3(String hora3) {
        this.hora3 = hora3;
    }

    public String getHora4() {
        return hora4;
    }

    public void setHora4(String hora4) {
        this.hora4 = hora4;
    }

    public String getHora5() {
        return hora5;
    }

    public void setHora5(String hora5) {
        this.hora5 = hora5;
    }
}
