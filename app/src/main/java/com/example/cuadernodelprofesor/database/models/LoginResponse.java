package com.example.cuadernodelprofesor.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("estado")
    @Expose
    private Integer estado;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    @SerializedName("user")
    @Expose
    private LoginUser user;

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LoginUser getLoginUser() {
        return user;
    }

    public void setLoginUser(LoginUser user) {
        this.user = user;
    }

}



