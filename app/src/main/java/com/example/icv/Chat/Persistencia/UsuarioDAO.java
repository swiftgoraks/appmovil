package com.example.icv.Chat.Persistencia;

import androidx.annotation.NonNull;

import com.example.icv.Chat.entidades.Base.usuario;
import com.example.icv.Chat.entidades.Logica.LUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
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
    public FirebaseAuth mAuth;

    private UsuarioDAO()
    {
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        //referenceUsu=database.getReference("usuario");
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
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
        //return  mAuth.getCurrentUser().getUid();
    }

    public long fechaUltimoLogin()
    {
        return  FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }

    public void InfoUsuPorLlave(final String key,final IDevolverUsu iDevolverUsu)
    {
       /* referenceUsu.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
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
        });*/

        db.collection("usuarios").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    usuario Usuario=new usuario();
                    Usuario.setCorreo(task.getResult().get("Correo").toString());
                    Usuario.setNombre(task.getResult().get("Nombre").toString());
                    Usuario.setFotoPerfil(task.getResult().get("UrlImagen").toString());

                    LUsuario lUsuario= new LUsuario(key,Usuario);
                    iDevolverUsu.devolverUsuario(lUsuario);


                }
                else
                {
                    iDevolverUsu.devolverError("Error");
                }
            }
        });


    }

    public boolean usuarioLogeado()
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        return user!=null;
    }

}

