package com.example.icv.Chat.entidades.Logica;

import com.example.icv.Chat.entidades.Base.mensaje;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class LMensaje {

    private mensaje Mensaje;
    private String key;
    private LUsuario lusuario;


    public LMensaje(mensaje mensaje, String key) {
        Mensaje = mensaje;
        this.key = key;
    }

    public mensaje getMensaje() {
        return Mensaje;
    }

    public void setMensaje(mensaje mensaje) {
        Mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCreatedTimesamp()
    {
        return (long)Mensaje.getCreatedTimestamp();
    }

    public LUsuario getLusuario() {
        return lusuario;
    }

    public void setLusuario(LUsuario lusuario) {
        this.lusuario = lusuario;
    }

    public String FechaMensaje()
    {

        Date date= new Date(getCreatedTimesamp());
        PrettyTime prettyTime = new PrettyTime(new Date(), Locale.getDefault());
        //SimpleDateFormat sdf= new SimpleDateFormat("hh:mm:ss a");
        //return sdf.format(d);
        return prettyTime.format(date);
    }
}