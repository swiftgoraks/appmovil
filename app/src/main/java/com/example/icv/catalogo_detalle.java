package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

    ImageView imageMarca;
    TextView textViewNombre;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_detalle);

        Bundle extras  = getIntent().getExtras();

        imageMarca = findViewById(R.id.logoMarca);
        textViewNombre = findViewById(R.id.nombreMarca);

        spinModelo = findViewById(R.id.spinModeloC);

        db = FirebaseFirestore.getInstance();

        if (extras != null){
            //idU = extras.getString("idU");

            id_marca = extras.getString("marca");

            obtnerInfoMarca(id_marca);

            obtenermodelo(id_marca);
        }
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
