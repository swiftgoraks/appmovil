package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    FirebaseFirestore db;
;

    ImageView imageView;

    RecyclerView myRecyclerViewSliderF;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagenes);



        db = FirebaseFirestore.getInstance();
       // easySlider = findViewById(R.id.sliderImagenes);

        //imageView = findViewById(R.id.imageViewView);

        myRecyclerViewSliderF = findViewById(R.id.myRecycleSliderFull);

        myRecyclerViewSliderF.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(ver_imagenes.this, LinearLayoutManager.HORIZONTAL, false);
        myRecyclerViewSliderF.setLayoutManager(horizontalLayoutManager);


        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            cod = extras.getString("idP");

            //Glide.with(getApplicationContext()).load(cod).into(imageView);
            imagenes_array(cod);
        }

    }

    public void imagenes_array(String CodPub){

        DocumentReference docRef = db.collection("publicacion").document(CodPub);

        docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList <String> imgs = new ArrayList <String>();
                        imgs = (ArrayList<String>) document.get("list_img");

                        String sliderItems[] = new String[imgs.size()];
                        for(int i = 0; i <=imgs.size() -1; i++)
                        {
                            // Toast.makeText(ver_publicacion.this, imgs.get(i), Toast.LENGTH_LONG).show();
                            sliderItems[i] = imgs.get(i);
                        }

                        adapterImageFull adaptador = new adapterImageFull(ver_imagenes.this, sliderItems);
                        myRecyclerViewSliderF.setAdapter(adaptador);



                        //easySlider.setPages(Arrays.asList(sliderItems));
                        //easySlider.setTimer(0);


                        // holder.txtNombreUserV.setText(document.get("Nombre").toString());
                    } else {
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
