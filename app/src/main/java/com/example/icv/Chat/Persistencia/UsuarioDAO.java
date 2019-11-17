package com.example.icv.Chat.Persistencia;

import androidx.annotation.NonNull;

import com.example.icv.Chat.entidades.Base.usuario;
import com.example.icv.Chat.entidades.Logica.LUsuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UsuarioDAO {

    public interface IDevolverUsu
    {
        public void devolverUsuario(LUsuario lUsuario);
        public void devolverError(String error);
    }

    private static UsuarioDAO usuarioDAO;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private DatabaseReference referenceUsu;
    private StorageReference referenceFoto;
    public FirebaseFirestore db;

    private UsuarioDAO()
    {
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        referenceUsu=database.getReference("usuario");
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios");
    }


    public static UsuarioDAO getInstance()
    {
        if(usuarioDAO==null)
        {
            usuarioDAO=new UsuarioDAO();
        }
        return usuarioDAO;
    }

    public  String getKeyUsuario()
    {
        return FirebaseAuth.getInstance().getUid();
    }

    public long fechaUltimoLogin()
    {
        return  FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }

    public void InfoUsuPorLlave(final String key,final IDevolverUsu iDevolverUsu)
    {
        referenceUsu.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario Usuario=dataSnapshot.getValue(usuario.class);
                LUsuario lUsuario= new LUsuario(key,Usuario);
                iDevolverUsu.devolverUsuario(lUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iDevolverUsu.devolverError("Error");
            }
        });

    }

    public boolean usuarioLogeado()
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        return user!=null;
    }

}
