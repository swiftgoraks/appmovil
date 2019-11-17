package com.example.icv.Chat.Persistencia;

import com.example.icv.Chat.entidades.Base.mensaje;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MensajeriaDAO {

    private static MensajeriaDAO mensajeriaDAO;
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceMensaje;

    public static MensajeriaDAO getInstance()
    {
        if(mensajeriaDAO==null)
        {
            mensajeriaDAO=new MensajeriaDAO();
        }
        return mensajeriaDAO;
    }

    private MensajeriaDAO()
    {
        database=FirebaseDatabase.getInstance();
        databaseReferenceMensaje=database.getReference("mensajes");
        // storage= FirebaseStorage.getInstance();
        // referenceUsu=database.getReference("usuario");
    }

    public void nuevoMensaje(String emisorKey, String receptorKey, mensaje Mensaje)
    {
        DatabaseReference referenceEmisor=databaseReferenceMensaje.child(emisorKey).child(receptorKey);//Emisor
        DatabaseReference referenceReceptopr=databaseReferenceMensaje.child(receptorKey).child(emisorKey);//Receptor
        referenceEmisor.push().setValue(Mensaje);
        referenceReceptopr.push().setValue(Mensaje);
    }
}
