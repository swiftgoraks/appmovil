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

    FirebaseFirestore db;
;

    ImageView imageView;

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_imagenes);



        db = FirebaseFirestore.getInstance();
       // easySlider = findViewById(R.id.sliderImagenes);

        imageView = findViewById(R.id.imageViewView);


        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            cod = extras.getString("imgUrl");

            Glide.with(getApplicationContext()).load(cod).into(imageView);
        }

    }

}
