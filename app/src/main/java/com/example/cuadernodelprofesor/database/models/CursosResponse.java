package com.example.cuadernodelprofesor.database.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CursosResponse {


    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("posts")
    @Expose
    private List<Curso> cursos = null;

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }
}
