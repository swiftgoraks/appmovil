package com.example.icv.publicacion;

public class marca {

    public String marca;
    public String id;

    public marca() {
    }

    public marca(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public marca(String marca, String id) {
        this.marca = marca;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


