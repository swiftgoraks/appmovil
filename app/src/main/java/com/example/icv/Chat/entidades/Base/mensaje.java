package com.example.icv.Chat.entidades.Base;

import com.google.firebase.database.ServerValue;

public class mensaje {

    private String mensaje,urlfoto,keyEmisor;
    private Boolean contieneFoto;
    private Object createdTimestamp;

    public mensaje() {
        createdTimestamp= ServerValue.TIMESTAMP;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUrlfoto() {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto) {
        this.urlfoto = urlfoto;
    }

    public String getKeyEmisor() {
        return keyEmisor;
    }

    public void setKeyEmisor(String keyEmisor) {
        this.keyEmisor = keyEmisor;
    }

    public Boolean getContieneFoto() {
        return contieneFoto;
    }

    public void setContieneFoto(Boolean contieneFoto) {
        this.contieneFoto = contieneFoto;
    }

    public Object getCreatedTimestamp() {
        return createdTimestamp;
    }
}

