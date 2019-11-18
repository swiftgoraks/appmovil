package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class catalogo_filtro extends AppCompatActivity {

    String id_marca;
    String modelo;

    ImageView imageMarca, imgExterior, imgInterior;
    TextView textViewNombre, textViewDescripcion, textInterior, textExterior,txtModelo;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_filtro);

        Bundle extras  = getIntent().getExtras();

        imageMarca = findViewById(R.id.logoMarca2);
        textViewNombre = findViewById(R.id.nombreMarca2);
        textViewDescripcion = findViewById(R.id.txtDescripcionModelo2);

        txtModelo = findViewById(R.id.txtmodeloS);
        imgExterior = findViewById(R.id.imgExterior2);
        imgInterior = findViewById(R.id.imgInterior2);

        textExterior = findViewById(R.id.txtExterior2);

        textInterior = findViewById(R.id.txtInterior2);

        db = FirebaseFirestore.getInstance();

        if (extras != null){
            //idU = extras.getString("idU");

            id_marca = extras.getString("marca");
            modelo = extras.getString("modelo");
            obtnerInfoMarca(id_marca);
            obtenerInfoModelo(modelo);

        }
    }

    public void obtenerInfoModelo(final String id_modeloSelect){
        db.collection("modelo").whereEqualTo("modelo", id_modeloSelect).get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    ArrayList <String> imgs[] = new ArrayList <>().toArray(new ArrayList[1]);
                    int contador = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                            textViewDescripcion.setText(document.get("descripcion").toString());

                            imgs[0] = (ArrayList<String>) document.get("url_img");
                            txtModelo.setText(document.get("modelo").toString());
                    }

                    Glide.with(getApplicationContext())
                            .load(imgs[0].get(0))
                            .into(imgExterior);

                    Glide.with(getApplicationContext())
                            .load(imgs[0].get(1))
                            .into(imgInterior);


                    textExterior.setVisibility(View.VISIBLE);

                    textInterior.setVisibility(View.VISIBLE);

                    imgExterior.setVisibility(View.VISIBLE);

                    imgInterior.setVisibility(View.VISIBLE);

                    textViewDescripcion.setVisibility(View.VISIBLE);


                } else {
                    Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    public void obtnerInfoMarca(String idM){
        db.collection("catalogo")
                .whereEqualTo("marca", idM)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                textViewNombre.setText(document.get("marca").toString());
                                Glide.with(getApplicationContext())
                                        .load(document.get("img_url").toString())
                                        .into(imageMarca);
                            }
                        } else {
                           // Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
