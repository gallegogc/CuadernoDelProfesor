package com.example.cuadernodelprofesor.database.bodies;

import com.google.gson.annotations.SerializedName;

public class ProfesorBody {

    @SerializedName("user")
    private String user;

    @SerializedName("pass")
    private String pass;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellidos")
    private String apellidos;

    public ProfesorBody(String user, String pass, String nombre, String apellidos) {
        this.user = user;
        this.pass = pass;
        this.nombre = nombre;
        this.apellidos=apellidos;
    }

}
