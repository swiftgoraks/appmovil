package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.icv.publicacion.modelo;
import com.example.icv.publicacion.publicar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class catalogo_detalle extends AppCompatActivity {

    private Spinner spinMarcar,spinModelo;

    public ArrayList<modelo> modelolista;

    public ArrayList<String> listamodelo;

    public static long idselectMarca,idSelectModel;

    String id_marca;

    ImageView imageMarca, imgExterior, imgInterior;
    TextView textViewNombre, textViewDescripcion, textInterior, textExterior;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_detalle);

        Bundle extras  = getIntent().getExtras();

        imageMarca = findViewById(R.id.logoMarca);
        textViewNombre = findViewById(R.id.nombreMarca);
        textViewDescripcion = findViewById(R.id.txtDescripcionModelo);

        spinModelo = findViewById(R.id.spinModeloC);

        imgExterior = findViewById(R.id.imgExterior);
        imgInterior = findViewById(R.id.imgInterior);

        textExterior = findViewById(R.id.txtExterior);

        textInterior = findViewById(R.id.txtInterior);

        textExterior.setVisibility(View.INVISIBLE);

        textInterior.setVisibility(View.INVISIBLE);

        textViewDescripcion.setVisibility(View.INVISIBLE);


        db = FirebaseFirestore.getInstance();

        if (extras != null){
            //idU = extras.getString("idU");

            id_marca = extras.getString("marca");

            obtnerInfoMarca(id_marca);

            obtenermodelo(id_marca);
        }

        spinModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinModelo.getSelectedItemPosition()>0)
                {
        obtenerInfoModelo(String.valueOf(spinModelo.getSelectedItemId()));

                        //Toast.makeText(getApplicationContext(),String.valueOf(spinModelo.getSelectedItemId()), Toast.LENGTH_LONG ).show();

                 }else {
                   // Toast.makeText(getApplicationContext(),"Esta vacio", Toast.LENGTH_LONG ).show();

                    textExterior.setVisibility(View.INVISIBLE);

                    textInterior.setVisibility(View.INVISIBLE);

                    imgExterior.setVisibility(View.INVISIBLE);

                    imgInterior.setVisibility(View.INVISIBLE);

                    textViewDescripcion.setVisibility(View.INVISIBLE);
                        }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void obtenerInfoModelo(final String id_modeloSelect){
        db.collection("modelo").whereEqualTo("id_catalogo", id_marca).get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task <QuerySnapshot> task) {
                if (task.isSuccessful()) {


                    ArrayList<String> imgs[] = new ArrayList <>().toArray(new ArrayList[1]);
                    int contador = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if(id_modeloSelect.equals(String.valueOf(contador))){

                            textViewDescripcion.setText(document.get("descripcion").toString());

                            imgs[0] = (ArrayList<String>) document.get("url_img");
                        }
                        contador = contador + 1;
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

        DocumentReference docRef = db.collection("catalogo").document(idM);

        docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Glide.with(getApplicationContext())
                                .load(document.get("img_url").toString())
                                .into(imageMarca);
                        textViewNombre.setText(document.get("marca").toString());

                    } else {
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void obtenermodelo(String idM)
    {

            db.collection("modelo").whereEqualTo("id_catalogo", idM).get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task <QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        String modelo [] = new String[task.getResult().size()];
                        String id [] = new String[task.getResult().size()];
                        int contador = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            modelo[contador] = document.get("modelo").toString();
                            id[contador] = document.get("id_catalogo").toString();
                            contador = contador + 1;
                        }

                        modelo cat=null;
                        modelolista=new ArrayList <modelo>();

                        for(int i = 0; i <= modelo.length -1; i++)
                        {
                            cat=new modelo();
                            cat.setModelo(modelo[i]);
                            cat.setId(id[i]);
                            modelolista.add(cat);
                        }
                        obtenerlistadomodelo();
                    } else {
                        Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                    }
                }
            });


    }


    public void obtenerlistadomodelo()
    {
        listamodelo=new ArrayList<String>();
        listamodelo.add("Seleccione modelo");

        if(modelolista==null)
        {
            //txtt.setText("nulo");
        }
        else
        {

            for(int i = 0; i <=modelolista.size()-1; i++)
            {
                listamodelo.add(modelolista.get(i).modelo);
            }
            ArrayAdapter<String> miAdaptador=new ArrayAdapter<>(catalogo_detalle.this,android.R.layout.simple_spinner_item,listamodelo);
            spinModelo.setAdapter(miAdaptador);

            if(idSelectModel>0)
            {
                int id=(int) idSelectModel;
                spinModelo.setSelection(id);
            }

        }

    }
}
