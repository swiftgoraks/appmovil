package com.example.icv.publicacion;

public class modelo {

    public String modelo;
    public String id;

    public modelo() {
    }

    public modelo(String modelo, String id) {
        this.modelo = modelo;
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
