package com.example.icv.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.icv.Chat.Holder.usuarioViewHolder;
import com.example.icv.Chat.Persistencia.UsuarioDAO;
import com.example.icv.Chat.entidades.Base.usuario;
import com.example.icv.Chat.entidades.Logica.LUsuario;
import com.example.icv.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class listadoUsuarioActivity extends AppCompatActivity {

    private RecyclerView rvUsuario;
    private FirebaseRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_usuario);

        rvUsuario=findViewById(R.id.rvUsuarios);


        LinearLayoutManager linear=new LinearLayoutManager(listadoUsuarioActivity.this);
        rvUsuario.setLayoutManager(linear);
        Query query= FirebaseDatabase.getInstance().getReference().child("usuario");

        FirebaseRecyclerOptions<usuario> options= new FirebaseRecyclerOptions.Builder<usuario>()
                .setQuery(query,usuario.class).build();

        adapter = new FirebaseRecyclerAdapter<usuario, usuarioViewHolder>(options) {
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
                final LUsuario lUsuario= new LUsuario(getSnapshots().getSnapshot(position).getKey(),model);
                holder.getLinear().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(listadoUsuarioActivity.this, UsuarioDAO.getInstance().getKeyUsuario()+"  :  "+lUsuario.getKey(),Toast.LENGTH_SHORT).show();
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
