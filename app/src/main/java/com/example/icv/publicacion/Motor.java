package com.example.icv.publicacion;

public class Motor {


    public String motor;
    public String id;

    public Motor() {
    }

    public Motor(String marca) {
        this.motor = marca;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public Motor(String motor, String id) {
        this.motor = motor;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
