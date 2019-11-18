package com.example.icv.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.icv.Chat.Holder.usuarioViewHolder;
import com.example.icv.Chat.entidades.Base.usuario;
import com.example.icv.Chat.entidades.Logica.LUsuario;
import com.example.icv.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listadoUsuarioActivity extends AppCompatActivity {

    private RecyclerView rvUsuario;
   // private FirebaseRecyclerAdapter adapter;
    FirestoreRecyclerAdapter adapter;
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_usuario);

        rvUsuario=findViewById(R.id.rvUsuarios);
        mAuth = FirebaseAuth.getInstance();


        LinearLayoutManager linear=new LinearLayoutManager(listadoUsuarioActivity.this);
        rvUsuario.setLayoutManager(linear);
        db = FirebaseFirestore.getInstance();

        Query query = FirebaseFirestore.getInstance().collection("usuarios");
              //  .whereLessThan("Correo",mAuth.getCurrentUser().getEmail());
        //Query query= FirebaseDatabase.getInstance().getReference().child("usuario");

       // FirebaseRecyclerOptions<usuario> options= new FirebaseRecyclerOptions.Builder<usuario>().setQuery(query,usuario.class).build();

        FirestoreRecyclerOptions<usuario> options = new FirestoreRecyclerOptions.Builder<usuario>()
                .setQuery(query, usuario.class)
                .build();

       // Toast.makeText(listadoUsuarioActivity.this, mAuth.getCurrentUser().getEmail(),Toast.LENGTH_LONG).show();

         adapter = new FirestoreRecyclerAdapter<usuario, usuarioViewHolder>(options) {
            @Override
            public usuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cardviewusuario, parent, false);

                return new usuarioViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final usuarioViewHolder holder, int position, final usuario model) {
                Glide.with(listadoUsuarioActivity.this).load(model.getFotoPerfil()).into(holder.getImg());
                holder.getTxtnombre().setText(model.getNombre());
                final LUsuario lUsuario= new LUsuario(getSnapshots().getSnapshot(position).getId(),model);
                holder.getLinear().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(listadoUsuarioActivity.this,MensajeriaActivity.class);
                        intent.putExtra("key_receptor",lUsuario.getKey());
                        intent.putExtra("nombre_receptor",holder.getTxtnombre().getText());
                        startActivity(intent);
                    }
                });
            }
        };

        rvUsuario.setAdapter(adapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
