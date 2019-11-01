package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;

public class ver_publicacion extends AppCompatActivity {


    EasySlider easySlider;
    FirebaseFirestore db;
    String cod;
    String codView;

    TextView txtTitulo, txtDescripcion, txtVendedor, txtPrecio, txtTelefono, txtYear, txtMarca, txtModelo;

    Button btnVer_info;

    RecyclerView myRecyclerViewSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_publicacion);

        txtTitulo = findViewById(R.id.txtTituloAnuncio);
        txtDescripcion = findViewById(R.id.txtComentario);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtVendedor = findViewById(R.id.txtVendedor);
        txtTelefono = findViewById(R.id.txtTelefono);

        txtMarca = findViewById(R.id.txtMarca);
        txtModelo = findViewById(R.id.txtModelo);
        txtYear = findViewById(R.id.txtYear);

        btnVer_info = findViewById(R.id.btnInformacion);

       // easySlider = findViewById(R.id.slider);
        db = FirebaseFirestore.getInstance();

        myRecyclerViewSlider = findViewById(R.id.myRecycleSlider);

        myRecyclerViewSlider.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(ver_publicacion.this, LinearLayoutManager.HORIZONTAL, false);
        myRecyclerViewSlider.setLayoutManager(horizontalLayoutManager);



        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            cod = extras.getString("publicacionCod");
            txtVendedor.setText(extras.getString("vendedor"));

            codView = extras.getString("CodU");

            datos_publicacion();
        }
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

                        txtDescripcion.setText(document.get("descripcion").toString());
                        txtPrecio.setText(document.get("precio").toString());
                        txtTelefono.setText(document.get("Telefono").toString());
                        txtTitulo.setText(document.get("titulo").toString());

                        txtMarca.setText(document.get("marca").toString());
                        txtModelo.setText(document.get("modelo").toString());
                        txtYear.setText(document.get("año").toString());


                        String sliderItems[] = new String[imgs.size()];
                        for(int i = 0; i <=imgs.size() -1; i++)
                        {
                        // Toast.makeText(ver_publicacion.this, imgs.get(i), Toast.LENGTH_LONG).show();
                            sliderItems[i] = imgs.get(i);
                        }

                        adapter_slider adaptador = new adapter_slider(ver_publicacion.this, sliderItems);
                        myRecyclerViewSlider.setAdapter(adaptador);



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

    public void verInformacion(View view) {
        Intent intent = new Intent(ver_publicacion.this, Catalogo.class);
        startActivity(intent);
    }

    public void verPerfil(View view) {
        Intent intent = new Intent(ver_publicacion.this, Perfil.class);
        intent.putExtra("idU", codView);
        startActivity(intent);
    }

    public void verImagnes(View view) {

        Intent intent  = new Intent(ver_publicacion.this, ver_imagenes.class);
        intent.putExtra("id_p", cod);
        startActivities(new Intent[]{intent});
    }
}
