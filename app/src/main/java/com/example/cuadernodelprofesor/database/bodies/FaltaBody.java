package com.example.cuadernodelprofesor.database.bodies;

import com.google.gson.annotations.SerializedName;

public class FaltaBody {

    @SerializedName("idAlumno")
    private int idAlumno;

    @SerializedName("idAsignatura")
    private int idAsignatura;


    public FaltaBody(int idAlumno, int idAsignatura) {
        this.idAlumno = idAlumno;
        this.idAsignatura = idAsignatura;
    }
}
