package com.example.cuadernodelprofesor.database.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostsResponse {

    @SerializedName("estado")
    @Expose
    private Integer estado;
    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

}