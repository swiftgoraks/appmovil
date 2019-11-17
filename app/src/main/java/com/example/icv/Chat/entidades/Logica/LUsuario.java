package com.example.icv.Chat.entidades.Logica;


import com.example.icv.Chat.Persistencia.UsuarioDAO;
import com.example.icv.Chat.entidades.Base.usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LUsuario {

    private usuario Usuario;
    private String key;

    public LUsuario(String key, usuario Usuario) {
        this.key = key;
        this.Usuario=Usuario;
    }

    public usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(usuario usuario) {
        Usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String obtenerFechaUltimaLogin()
    {
        Date d= new Date(UsuarioDAO.getInstance().fechaUltimoLogin());
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(d);
    }
}
