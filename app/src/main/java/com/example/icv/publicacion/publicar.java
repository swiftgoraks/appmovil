package com.example.icv.publicacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.icv.R;
import com.example.icv.Resgistrarse;
import com.example.icv.home;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class publicar extends AppCompatActivity {

    private Spinner spinMarcar,spinModelo;
    public FirebaseAuth mAuth;
    public FirebaseFirestore db;
    public ArrayList<marca> marcalista;
    public ArrayList<modelo> modelolista;
    public ArrayList<String> listamarca;
    public ArrayList<String> listamodelo;
    private Button btPublicar,btUbicacion;
    private FirebaseStorage storage;
    private EditText txtTitulo,txtAnio,txtKm,txtDescrip,txtPrecio;
    public static ArrayList<String> listaimg;
    private static final int fotoenviada=1;
    private ImageButton btImg;
    public static String latitud,longitud,ciudad;
    public static long idselectMarca,idSelectModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        spinMarcar=findViewById(R.id.spinMarca);
        btPublicar=findViewById(R.id.btPublicarAnuncio);
        spinModelo=findViewById(R.id.spinModelo);
        txtAnio=findViewById(R.id.txtAnio);
        txtDescrip=findViewById(R.id.txtDescripcion);
        txtKm=findViewById(R.id.txtKm);
        txtTitulo=findViewById(R.id.txtTitulo);
        txtPrecio=findViewById(R.id.txtPrecio);
        storage=FirebaseStorage.getInstance();
        btImg=findViewById(R.id.btImg);
        btUbicacion=findViewById(R.id.btUbicacion);
        obtenermarca();

        btPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(publicar.this,""+listaimg, Toast.LENGTH_LONG).show();
                publicarAnuncio();
                finish();
            }
        });

        btUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ventana= new Intent(publicar.this, MapsActivity.class);
                startActivityForResult(ventana,1234);
            }
        });

        btImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(Intent.createChooser(i,"Seleccionar imagen"),fotoenviada);
            }
        });


        spinMarcar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                obtenermodelo();
               // Toast.makeText(publicar.this,""+i,Toast.LENGTH_SHORT).show();
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

                    marca cat=null;
                    marcalista=new ArrayList<marca>();

                    for(int i = 0; i <=marca.length -1; i++)
                    {
                        cat=new marca();
                        cat.setMarca(marca[i]);
                        cat.setId(id[i]);
                        marcalista.add(cat);
                    }
                    obtenerlistadomarca();
                } else {
                    Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }

            }
        });

    }

    public void obtenerlistadomarca()
    {
        listamarca=new ArrayList<String>();
        listamarca.add("Seleccione marca");

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
            ArrayAdapter<String> miAdaptador=new ArrayAdapter<>(publicar.this,android.R.layout.simple_spinner_item,listamarca);
            spinMarcar.setAdapter(miAdaptador);

            if(idselectMarca>0)
            {
                int id=(int) idselectMarca;
                spinMarcar.setSelection(id);
            }

        }
    }

    public void obtenermodelo()
    {
        if(spinMarcar.getSelectedItemPosition()>0)
        {

            idselectMarca=spinMarcar.getSelectedItemId();
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
            ArrayAdapter<String> miAdaptador=new ArrayAdapter<>(publicar.this,android.R.layout.simple_spinner_item,listamodelo);
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
            ArrayAdapter<String> miAdaptador=new ArrayAdapter<>(publicar.this,android.R.layout.simple_spinner_item,listamodelo);
            spinModelo.setAdapter(miAdaptador);

            if(idSelectModel>0)
            {
                int id=(int) idSelectModel;
                spinModelo.setSelection(id);
            }

        }

    }

    public void publicarAnuncio()
    {
        if(txtTitulo.getText().length()>0 && txtAnio.getText().length()>0 && txtKm.getText().length()>0 && txtDescrip.getText().length()>0 && txtPrecio.getText().length()>0 &&spinMarcar.getSelectedItemPosition()>0 &&spinModelo.getSelectedItemPosition()>0)
        {
            String titulo=txtTitulo.getText().toString();
            String marca=spinMarcar.getSelectedItem().toString();
            String modelo=spinModelo.getSelectedItem().toString();
            int anio=Integer.parseInt(txtAnio.getText().toString());
            double precio=Double.parseDouble(txtPrecio.getText().toString());
            String descrip=txtDescrip.getText().toString();

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String date = df.format(Calendar.getInstance().getTime());

            Map<String, Object> user = new HashMap<>();
            user.put("titulo", titulo);
            user.put("marca", marca);
            user.put("modelo", modelo);
            user.put("año", anio);
          //  user.put("kilometraje", passU);
            user.put("precio", precio);
            user.put("descripcion", descrip);
            user.put("Telefono", "+503 - 74827879");
            user.put("estado", "activo");
            user.put("fecha_publicacion", date);
            user.put("id_usuario",  mAuth.getCurrentUser().getUid());
            user.put("list_img", listaimg);
            user.put("latitud",latitud);
            user.put("longitud",longitud);
            user.put("ciudad",ciudad);


            db.collection("publicacion").document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    startActivity(new Intent(publicar.this, home.class));
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==fotoenviada && resultCode==RESULT_OK)
        {
            if(data.getClipData()!=null)
            {
                Toast.makeText(publicar.this,"varias fotos", Toast.LENGTH_LONG).show();
                int cantidadselect=data.getClipData().getItemCount();
                listaimg=new ArrayList<String>();
                for(int i=0; i<cantidadselect;i++)
                {
                    Uri u=data.getClipData().getItemAt(i).getUri();
                    guardarImg(u,listaimg);
                }

            }
            else if(data.getData()!=null)
            {
                listaimg=new ArrayList<String>();
                Toast.makeText(publicar.this,"Una foto", Toast.LENGTH_LONG).show();
                Uri u=data.getData();
                guardarImg(u,listaimg);
            }
        }else if (requestCode == 1234 && resultCode == RESULT_OK)
        {
            if(data.getExtras().getString("longitud")!=null)
            {
                Toast.makeText(publicar.this,data.getExtras().getString("latitud")+data.getExtras().getString("longitud")+data.getExtras().getString("ciudad"),Toast.LENGTH_SHORT).show();
                latitud=data.getExtras().getString("latitud");
                longitud=data.getExtras().getString("longitud");
                ciudad=data.getExtras().getString("ciudad");
            }
            else
            {
                Toast.makeText(publicar.this,"Debe ingresar una ubicación",Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void guardarImg(Uri u,final ArrayList<String> listaimgs)
    {
        StorageReference storageRef = storage.getReference();
        final  StorageReference fotoReferencia = storageRef.child("img_publicacion/"+u.getLastPathSegment());

        fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        throw Objects.requireNonNull(task.getException());
                    }
                }
                return fotoReferencia.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUrl = task.getResult();
                    listaimgs.add(downloadUrl.toString());
                }
            }
        });
    }


}
