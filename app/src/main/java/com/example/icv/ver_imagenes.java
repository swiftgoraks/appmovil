package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;

public class ver_imagenes extends AppCompatActivity {

    String cod;

    EasySlider easySlider;
    FirebaseFirestore db;
    ImageSwitcher imageSwitcher;

    ImageButton btnAtras, btnDeltante, btnRestart;

    ImageView imageView;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagenes);



        db = FirebaseFirestore.getInstance();
       // easySlider = findViewById(R.id.sliderImagenes);

        btnAtras = findViewById(R.id.btnAtras);
        btnDeltante = findViewById(R.id.btnAdelante);
        btnRestart = findViewById(R.id.btnRestart);

        imageView = findViewById(R.id.imageViewView);


        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            cod = extras.getString("id_p");

            datos_publicacion();
        }

        if(i==0){btnAtras.setVisibility(View.INVISIBLE);}

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DocumentReference docRef = db.collection("publicacion").document(cod);

                docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList <String> imgs = new ArrayList <String>();
                                imgs = (ArrayList<String>) document.get("list_img");

                                assert imgs != null;
                                // Toast.makeText(getApplicationContext(), imgs.get(0).toString(), Toast.LENGTH_LONG).show();

                                if(i > 0){

                                    i--;
                                    if(i == 0){btnAtras.setVisibility(View.INVISIBLE);}
                                    else {btnDeltante.setVisibility(View.VISIBLE);
                                    btnRestart.setVisibility(View.GONE);
                                    }
                                    Glide.with(ver_imagenes.this)
                                            .load(imgs.get(i))
                                            .into(imageView);
                                }

                            } else {
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            }
        });

        btnDeltante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("publicacion").document(cod);

                docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList <String> imgs = new ArrayList <String>();
                                imgs = (ArrayList<String>) document.get("list_img");

                                assert imgs != null;
                                // Toast.makeText(getApplicationContext(), imgs.get(0).toString(), Toast.LENGTH_LONG).show();

                                if(i < imgs.size() - 1){

                                    i++;
                                    if(i == imgs.size() - 1 ){btnDeltante.setVisibility(View.GONE); btnRestart.setVisibility(View.VISIBLE);}
                                    else {
                                        btnAtras.setVisibility(View.VISIBLE);
                                }
                                    Glide.with(ver_imagenes.this)
                                            .load(imgs.get(i))
                                            .into(imageView);
                                }

                            } else {
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                        }


                    }
                });
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("publicacion").document(cod);

                docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList <String> imgs = new ArrayList <String>();
                                imgs = (ArrayList<String>) document.get("list_img");

                                assert imgs != null;
                                // Toast.makeText(getApplicationContext(), imgs.get(0).toString(), Toast.LENGTH_LONG).show();

                                        Glide.with(ver_imagenes.this)
                                                .load(imgs.get(0))
                                                .into(imageView);
                                        btnAtras.setVisibility(View.INVISIBLE);
                                        btnDeltante.setVisibility(View.VISIBLE);
                                        btnRestart.setVisibility(View.GONE);
                                        i=0;


                            } else {
                            }
                        } else {
                            //Log.d(TAG, "get failed with ", task.getException());
                        }


                    }
                });
            }
        });
    }

    public void datos_publicacion(){

        DocumentReference docRef = db.collection("publicacion").document(cod);

        docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList <String> imgs = new ArrayList <String>();
                        imgs = (ArrayList<String>) document.get("list_img");

                        assert imgs != null;
                        Toast.makeText(getApplicationContext(), imgs.get(0).toString(), Toast.LENGTH_LONG).show();
                        Glide.with(ver_imagenes.this)
                                .load(imgs.get(0))
                                .into(imageView);
                    } else {
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}
