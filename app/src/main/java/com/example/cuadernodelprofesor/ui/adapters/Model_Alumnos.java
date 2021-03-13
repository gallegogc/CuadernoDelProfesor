package com.example.cuadernodelprofesor.ui.adapters;

public class Model_Alumnos {
    private String nombreAlumnos, apellidosAlumnos;
    private int idAlumno;

    public Model_Alumnos(int idAlumno,String nombreAlumnos, String apellidosAlumnos) {
        this.idAlumno = idAlumno;
        this.nombreAlumnos = nombreAlumnos;
        this.apellidosAlumnos = apellidosAlumnos;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public String getNombreAlumnos() {
        return nombreAlumnos;
    }

    public void setNombreAlumnos(String nombreAlumnos) {
        this.nombreAlumnos = nombreAlumnos;
    }

    public String getApellidosAlumnos() {
        return apellidosAlumnos;
    }

    public void setApellidosAlumnos(String apellidosAlumnos) {
        this.apellidosAlumnos = apellidosAlumnos;
    }

}