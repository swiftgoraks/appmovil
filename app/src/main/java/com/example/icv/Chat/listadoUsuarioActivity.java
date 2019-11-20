package com.example.icv.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.icv.Chat.Holder.usuarioViewHolder;
import com.example.icv.Chat.Persistencia.UsuarioDAO;
import com.example.icv.Chat.entidades.Base.usuario;
import com.example.icv.Chat.entidades.Logica.LUsuario;
import com.example.icv.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
                //.where("Correo",mAuth.getCurrentUser().getEmail());
        //Query query= FirebaseDatabase.getInstance().getReference().child("usuario");

       // FirebaseRecyclerOptions<usuario> options= new FirebaseRecyclerOptions.Builder<usuario>().setQuery(query,usuario.class).build();

        final FirestoreRecyclerOptions<usuario> options = new FirestoreRecyclerOptions.Builder<usuario>()
                .setQuery(query, usuario.class)
                .build();




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
            protected void onBindViewHolder(final usuarioViewHolder holder, final int position, final usuario model) {

                FirebaseDatabase.getInstance().getReference().child("mensajes").child(UsuarioDAO.getInstance().getKeyUsuario()).child(getSnapshots().getSnapshot(position).getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot data) {
                        if(data.exists())
                        {
                            holder.getTxtnombre().setText(model.getNombre());
                            if(!model.getUrlImagen().isEmpty())
                            {
                                Glide.with(getApplicationContext()).load(model.getUrlImagen()).into(holder.getImg());
                            }

                            //  }
                            /*if (!(listadoUsuarioActivity.this.isDestroyed()))
                            {
                                holder.getTxtnombre().setText(model.getNombre());
                                Glide.with(getApplicationContext()).load(model.getUrlImagen()).into(holder.getImg());
                            }

                             */
                        }
                        else
                        {
                            holder.getLinear().setVisibility(View.GONE);
                            holder.getLinear().removeAllViews();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
