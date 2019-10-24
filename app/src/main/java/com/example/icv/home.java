package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icv.publicacion.MapsActivity;
import com.example.icv.publicacion.publicar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Maps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.internal.DiskLruCache;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {


    public FirebaseAuth mAuth;
    public TextView textBienvienido;
    private Button btPublicar;



    RecyclerView myRecyclerView;

    FirebaseFirestore db;

    String idU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btPublicar=findViewById(R.id.btPublicar);

        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            idU = extras.getString("idU");
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

       // textBienvienido = findViewById(R.id.txtBienvenido);

       myRecyclerView = findViewById(R.id.myRecycleViewView);

        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       // textBienvienido.setText(mAuth.getCurrentUser().getEmail());

        cargarAnuncios();

        btPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home.this, publicar.class));
               // finish();

            }
        });

    }

    public void cargarAnuncios(){

        db.collection("publicacion").whereLessThan("id_usuario", idU)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String id_anuncio[] = new String[task.getResult().size()];
                            String titulo[] = new String[task.getResult().size()];
                            Integer year[] = new Integer[task.getResult().size()];
                            String modelo[] = new String[task.getResult().size()];
                            String marca [] = new String[task.getResult().size()];
                            String id_usuario [] = new String[task.getResult().size()];
                            Double precio[] = new Double[task.getResult().size()];
                            String estado [] = new String[task.getResult().size()];
                            String descripcion [] = new String[task.getResult().size()];
                            ArrayList<String> imgs[] = new ArrayList <>().toArray(new ArrayList[task.getResult().size()]);
                            String fecha[] = new String[task.getResult().size()];
                            int contador = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {


                                    id_anuncio[contador] = document.getId();
                                    titulo[contador] = document.get("titulo").toString();
                                    year[contador] =Integer.parseInt(document.get("año").toString());
                                    modelo[contador] = document.get("modelo").toString();
                                    marca[contador] = document.get("marca").toString();
                                    id_usuario[contador] = document.get("id_usuario").toString();
                                    precio[contador] =  Double.parseDouble(document.get("precio").toString());
                                    estado[contador] = document.get("estado").toString();
                                    descripcion[contador] = document.get("descripcion").toString();
                                    imgs[contador] = (ArrayList<String>) document.get("list_img");
                                    fecha[contador] = document.get("fecha_publicacion").toString();
                                    contador = contador + 1;




                            }





                            Anuncio anunciosLista[] = new Anuncio[titulo.length];
                            for(int i = 0; i <=titulo.length - 1; i++)
                            {
            Anuncio anuncios =  new Anuncio(id_anuncio[i],titulo[i],year[i], modelo[i], marca[i], id_usuario[i], precio[i], estado[i], descripcion[i], imgs[i], fecha[i]);

                                anunciosLista[i] = anuncios;
                            }


                           MyAdapter adaptador = new MyAdapter(home.this, anunciosLista, idU);
                            myRecyclerView.setAdapter(adaptador);

                        } else {
                            Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }


                    }
                });

    }

    public void cerrarSesion(View view){

        mAuth.signOut();
        startActivity(new Intent(home.this, Login.class));
        finish();
    }


}
