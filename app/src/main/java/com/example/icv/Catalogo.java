package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {

    GridView myGridView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        myGridView = findViewById(R.id.gridView);

        db = FirebaseFirestore.getInstance();

        catalogo_data();

    }

    public void catalogo_data(){
 //

        db.collection("catalogo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            String nom_marca[] = new String[task.getResult().size()];
                            String url_img[] = new String[task.getResult().size()];
                            int contador = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                nom_marca[contador] = document.get("marca").toString();
                                url_img[contador] = document.get("img_url").toString();

                                contador = contador + 1;

                            }

            final GridAdapter gridAdapter = new GridAdapter(Catalogo.this, nom_marca, url_img);
                            myGridView.setAdapter(gridAdapter);

                            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView <?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(Catalogo.this, catalogo_detalle.class);
                                    intent.putExtra("marca", gridAdapter.getItem(i).toString());
                                    startActivity(intent);
                                }
                            });


                            // Toast.makeText(home.this, titulo[0].toString(), Toast.LENGTH_LONG ).show();



                        } else {
                            Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }


                    }
                });
        ////
    }
}
