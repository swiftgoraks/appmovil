package com.example.icv;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.CaseMap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.icv.publicacion.marca;
import com.example.icv.publicacion.modelo;
import com.example.icv.publicacion.publicar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;

public class filtroClass {

    FirebaseFirestore db;
    public ArrayList<marca> marcalista;
    public ArrayList<String> listamarca;

    public ArrayList<modelo> modelolista;
    public ArrayList<String> listamodelo;




    public static long idselectMarca,idSelectModel;

    public interface FinalizoCuadroDialogo{
        void resultado(String marca, String modelo);
    }

    private FinalizoCuadroDialogo interfaz;
    Context context;

    final Spinner spinMarca;
    final Spinner spinModelo;

    EditText yearMaximo, yearMinimo, precioMinimo, precioMaximo;

    public filtroClass(final Context myContext, FinalizoCuadroDialogo actividad) {

        context = myContext;
        interfaz = actividad;



        final Dialog dialog = new Dialog(myContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      //  dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogo_filtro);



//precioMaximo = dialog.findViewById(R.id.precioMaximo);
//precioMinimo = dialog.findViewById(R.id.precioMinimo);

        spinMarca = dialog.findViewById(R.id.spinner_marcaB);
        spinModelo = dialog.findViewById(R.id.spinner_modeloB);

        Button btnBuscar = dialog.findViewById(R.id.btnBuscarB);

        db = FirebaseFirestore.getInstance();


        obtenermarca();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

         /*       int yearMa = 0;
                int yearMin = 0;
                double precioMx = 0;
                double precionMin = 0;
                Boolean precioB = false;
                Boolean yearB = false;

                Boolean hacer1 = false;
                Boolean hacer2 = false;

                Boolean continuar1 = false;

                Boolean continuar2 = false;

               if(precioMaximo.length() > 0 ){
                   precioMx = Double.parseDouble(precioMaximo.getText().toString());
               }
                if(precioMinimo.length() > 0 ){
                    precionMin = Double.parseDouble(precioMinimo.getText().toString());
                }

                if(yearMaximo.length() > 0){
                    yearMa = Integer.parseInt(yearMaximo.getText().toString());
                }
                if(yearMinimo.length() > 0){
                    yearMin = Integer.parseInt(yearMinimo.getText().toString());
                }


                if(yearMin == 0 || yearMa == 0){
                    hacer1 = true;
                }
                else{
                    if(yearMa > yearMin){
                        yearB = true;
                    }
                    else{
                        yearB = false;
                        Toast.makeText(context, "Años incorrectas", Toast.LENGTH_LONG).show();
                    }
                }

                if(precionMin == 0 || precioMx == 0){
                    hacer2 = true;
                }
                else{
                    if(precioMx > precionMin){
                        precioB = true;
                    }
                    else{
                        precioB = false;
                        Toast.makeText(context, "Precios incorrectos", Toast.LENGTH_LONG).show();
                    }
                }


if(hacer1){
    continuar1 = true;
}
else if(!yearB){
    continuar1 = false;
}else {
    continuar1 = true;
}

                if(hacer2){
                    continuar2 = true;
                }
                else if(!precioB){
                    continuar2 = false;
                }else {
                    continuar2 = true;
                } */


               // if(continuar1 && continuar2){
                  interfaz.resultado(spinMarca.getSelectedItem().toString(), spinModelo.getSelectedItem().toString());


                    dialog.dismiss();
               // }
    //interfaz.resultado(spinMarca.getSelectedItem().toString(), spinModelo.getSelectedItem().toString());


    //interfaz.resultado(spinMarca.getSelectedItem().toString(), spinModelo.getSelectedItem().toString());


               // Toast.makeText(myContext,spinMarca.getSelectedItem().toString(), Toast.LENGTH_LONG ).show();

            }
        });

        spinMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                idSelectModel=0;


                //Toast.makeText(context,"Me has presionado",Toast.LENGTH_SHORT).show();
                obtenermodelo();
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinModelo.getSelectedItemPosition()>0)
                {
                    idSelectModel=spinModelo.getSelectedItemId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
    }


    public void obtenermarca()
    {
        db.collection("catalogo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String marca [] = new String[task.getResult().size()];
                    String id [] = new String[task.getResult().size()];
                    int contador = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        marca[contador] = document.get("marca").toString();
                        id[contador] = document.getId();
                        contador = contador + 1;
                    }

                    com.example.icv.publicacion.marca cat=null;
                    marcalista=new ArrayList<marca>();

                    for(int i = 0; i <=marca.length -1; i++)
                    {
                        cat=new marca();
                        cat.setMarca(marca[i]);
                        cat.setId(id[i]);
                        marcalista.add(cat);
                    }
                    obtenerlistadomarca(context);
                } else {
                    Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                }

            }
        });

    }

    public void obtenerlistadomarca(Context otroContext)
    {
        listamarca=new ArrayList<String>();
        listamarca.add("Ninguna");

        if(marcalista==null)
        {
            //txtt.setText("nulo");
        }
        else
        {

            for(int i = 0; i <=marcalista.size()-1; i++)
            {
                listamarca.add(marcalista.get(i).marca);
            }
            ArrayAdapter <String> miAdaptador=new ArrayAdapter<>(otroContext,android.R.layout.simple_spinner_item,listamarca);
            spinMarca.setAdapter(miAdaptador);

            if(idselectMarca>0)
            {
                int id=(int) idselectMarca;
                spinMarca.setSelection(id);
            }

        }
    }

    public void obtenermodelo()
    {
        if(spinMarca.getSelectedItemPosition()>0)
        {

            idselectMarca=spinMarca.getSelectedItemId();
            int id=(int) idselectMarca;
            db.collection("modelo").whereEqualTo("id_catalogo", marcalista.get(id-1).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        String marca [] = new String[task.getResult().size()];
                        String id [] = new String[task.getResult().size()];
                        int contador = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            marca[contador] = document.get("modelo").toString();
                            id[contador] = document.get("id_catalogo").toString();
                            contador = contador + 1;
                        }

                        modelo cat=null;
                        modelolista=new ArrayList<modelo>();

                        for(int i = 0; i <=marca.length -1; i++)
                        {
                            cat=new modelo();
                            cat.setModelo(marca[i]);
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
        else
        {
            listamodelo=new ArrayList<String>();
            listamodelo.add("Seleccione una marca");
            ArrayAdapter<String> miAdaptador=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,listamodelo);
            spinModelo.setAdapter(miAdaptador);
        }
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
            ArrayAdapter<String> miAdaptador=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,listamodelo);
            spinModelo.setAdapter(miAdaptador);

            if(idSelectModel>0)
            {
                int id=(int) idSelectModel;
                spinModelo.setSelection(id);
            }

        }

    }
}
