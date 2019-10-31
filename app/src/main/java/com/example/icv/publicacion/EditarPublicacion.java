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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icv.Perfil;
import com.example.icv.R;
import com.example.icv.home;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ahmed.easyslider.EasySlider;
import ahmed.easyslider.SliderItem;

public class EditarPublicacion extends AppCompatActivity {
    public ArrayList<marca> marcalista;
    public ArrayList<modelo> modelolista;
    public ArrayList<String> listamarca;
    public ArrayList<String> listamodelo;
    public String marcabase, modelobase;
    public static long idselectMarca, idSelectModel;
    private Button btGuardar;
    private ImageButton btImg;
    TextView txtTitulo, txtDescripcion, txtPrecio, txtTelefono, txtYear;
    Spinner spinMarca, spinModelo;
    EasySlider easySlider;
    FirebaseFirestore db;
    String cod;
    private static final int fotoenviada = 1;
    private FirebaseStorage storage;
    public static ArrayList<String> listaimg = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_publicacion);
        txtTitulo = findViewById(R.id.txtTitulo);
        spinMarca = findViewById(R.id.spinMarca);
        spinModelo = findViewById(R.id.spinModelo);
        txtYear = findViewById(R.id.txtAnio);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtTelefono = findViewById(R.id.txtKm);
        easySlider = findViewById(R.id.slider);
       // btGuardar = findViewById(R.id.btPublicar);
        btImg = findViewById(R.id.btImg);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            cod = extras.getString("publicacionCod");
            datos_publicacion();
        }

        easySlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        spinMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

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
                if (spinModelo.getSelectedItemPosition() > 0) {
                    idSelectModel = spinModelo.getSelectedItemId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(i, "Seleccionar imagen"), fotoenviada);
            }
        });
    }

    public void datos_publicacion() {

        DocumentReference docRef = db.collection("publicacion").document(cod);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       // ArrayList<String> imgs = new ArrayList<>();
                        listaimg = (ArrayList<String>) document.get("list_img");

                        txtDescripcion.setText(document.get("descripcion").toString());
                        txtPrecio.setText(document.get("precio").toString());
                        txtTelefono.setText(document.get("Telefono").toString());
                        txtTitulo.setText(document.get("titulo").toString());
                        txtYear.setText(document.get("año").toString());
                        marcabase = document.get("marca").toString();
                        modelobase = document.get("marca").toString();

                        SliderItem sliderItems[] = new SliderItem[listaimg.size()];
                        for (int i = 0; i <= listaimg.size() - 1; i++) {
                            sliderItems[i] = (new SliderItem("", listaimg.get(i)));
                        }

                        easySlider.setPages(Arrays.asList(sliderItems));
                        easySlider.setTimer(0);
                        obtenermarca();
                    } else {
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void obtenermarca() {
        db.collection("catalogo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String marca[] = new String[task.getResult().size()];
                    String id[] = new String[task.getResult().size()];
                    int contador = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        marca[contador] = document.get("marca").toString();
                        id[contador] = document.getId();
                        contador = contador + 1;
                    }

                    marca cat = null;
                    marcalista = new ArrayList<marca>();

                    for (int i = 0; i <= marca.length - 1; i++) {
                        cat = new marca();
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

    public void obtenerlistadomarca() {
        listamarca = new ArrayList<String>();
        listamarca.add("Seleccione marca");
        int pos = 0;

        if (marcalista == null) {
            //txtt.setText("nulo");
        } else {

            for (int i = 0; i <= marcalista.size() - 1; i++) {
                listamarca.add(marcalista.get(i).marca);
                if (marcabase.equals(marcalista.get(i).marca)) {
                    pos = i;
                }
            }
            ArrayAdapter<String> miAdaptador = new ArrayAdapter<>(EditarPublicacion.this, android.R.layout.simple_spinner_item, listamarca);
            spinMarca.setAdapter(miAdaptador);

            spinMarca.setSelection(pos + 1);
            if (idselectMarca > 0) {
                int id = (int) idselectMarca;
                spinMarca.setSelection(id);
            }

        }
    }

    public void obtenermodelo() {
        if (spinMarca.getSelectedItemPosition() > 0) {

            idselectMarca = spinMarca.getSelectedItemId();
            int id = (int) idselectMarca;
            db.collection("modelo").whereEqualTo("id_catalogo", marcalista.get(id - 1).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        String marca[] = new String[task.getResult().size()];
                        String id[] = new String[task.getResult().size()];
                        int contador = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            marca[contador] = document.get("modelo").toString();
                            id[contador] = document.get("id_catalogo").toString();
                            contador = contador + 1;
                        }

                        modelo cat = null;
                        modelolista = new ArrayList<modelo>();

                        for (int i = 0; i <= marca.length - 1; i++) {
                            cat = new modelo();
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

        } else {
            listamodelo = new ArrayList<String>();
            listamodelo.add("Seleccione una marca");
            ArrayAdapter<String> miAdaptador = new ArrayAdapter<>(EditarPublicacion.this, android.R.layout.simple_spinner_item, listamodelo);
            spinModelo.setAdapter(miAdaptador);
        }
    }

    public void obtenerlistadomodelo() {
        listamodelo = new ArrayList<String>();
        listamodelo.add("Seleccione modelo");
        int pos = 0;

        if (modelolista == null) {
            //txtt.setText("nulo");
        } else {

            for (int i = 0; i <= modelolista.size() - 1; i++) {
                listamodelo.add(modelolista.get(i).modelo);
                if (modelobase.equals(modelolista.get(i).modelo)) {
                    pos = i;
                }
            }
            ArrayAdapter<String> miAdaptador = new ArrayAdapter<>(EditarPublicacion.this, android.R.layout.simple_spinner_item, listamodelo);
            spinModelo.setAdapter(miAdaptador);
            spinModelo.setSelection(pos + 1);

            if (idSelectModel > 0) {
                int id = (int) idSelectModel;
                spinModelo.setSelection(id);
            }

        }

    }

    public void Guardar(View view) {

        String titulo = txtTitulo.getText().toString();
        String marca = spinMarca.getSelectedItem().toString();
        String modelo = spinModelo.getSelectedItem().toString();
        int anio = Integer.parseInt(txtYear.getText().toString());
        double precio = Double.parseDouble(txtPrecio.getText().toString());
        String descrip = txtDescripcion.getText().toString();
        String telefono = txtTelefono.getText().toString();

        // DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        // String date = df.format(Calendar.getInstance().getTime());


        Map<String, Object> editarPublicacion = new HashMap<>();
        editarPublicacion.put("titulo", titulo);
        editarPublicacion.put("marca", marca);
        editarPublicacion.put("modelo", modelo);
        editarPublicacion.put("año", anio);
        //  user.put("kilometraje", passU);
        editarPublicacion.put("precio", precio);
        editarPublicacion.put("descripcion", descrip);
        editarPublicacion.put("Telefono", telefono);
        editarPublicacion.put("estado", "activo");
        //editarPublicacion.put("fecha_publicacion", date);
        // editarPublicacion.put("id_usuario",  mAuth.getCurrentUser().getUid());
        editarPublicacion.put("list_img", listaimg);
        // editarPublicacion.put("latitud",latitud);
        // editarPublicacion.put("longitud",longitud);
        // editarPublicacion.put("ciudad",ciudad);


        db.collection("publicacion").document(cod)
                .update(editarPublicacion)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //startActivity(new Intent(EditarPublicacion.this, Perfil.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == fotoenviada && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int cantidadselect = data.getClipData().getItemCount();
                //listaimg = new ArrayList<String>();
                for (int i = 0; i < cantidadselect; i++) {
                    Uri u = data.getClipData().getItemAt(i).getUri();
                    guardarImg(u, listaimg);
                }

            } else if (data.getData() != null) {
               // listaimg = new ArrayList<String>();
                Uri u = data.getData();
                guardarImg(u, listaimg);
            }
        }
    }

    public void guardarImg(Uri u, final ArrayList<String> listaimgs) {
        StorageReference storageRef = storage.getReference();
        final StorageReference fotoReferencia = storageRef.child("img_publicacion/" + u.getLastPathSegment());

        fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        throw Objects.requireNonNull(task.getException());
                    }
                }
                return fotoReferencia.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    listaimgs.add(downloadUrl.toString());
Toast.makeText(EditarPublicacion.this,"ac  "+listaimgs.size(),Toast.LENGTH_SHORT).show();
                    SliderItem sliderItems[] = new SliderItem[listaimgs.size()];
                    for (int i = 0; i <= listaimgs.size() - 1; i++) {
                        sliderItems[i] = (new SliderItem("", listaimgs.get(i)));
                    }

                    easySlider.setPages(Arrays.asList(sliderItems));
                    easySlider.setTimer(0);
                }
            }
        });
    }
}
