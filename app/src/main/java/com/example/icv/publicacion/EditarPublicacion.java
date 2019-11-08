package com.example.icv.publicacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icv.Catalogo;
import com.example.icv.Login;
import com.example.icv.Perfil;
import com.example.icv.R;
import com.example.icv.home;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class EditarPublicacion extends AppCompatActivity implements imgAdapter.OnClick, OnMapReadyCallback {
    public ArrayList<marca> marcalista;
    public ArrayList<modelo> modelolista;
    public ArrayList<String> listamarca;
    public ArrayList<String> listamodelo;
    public String marcabase, modelobase;
    public static double longitude,latitude;
    public static String ciudad,radiobt;
    public static Uri u;
    public static long idselectMarca, idSelectModel;
    private ImageButton btImg;
    TextView txtTitulo, txtDescripcion, txtPrecio, txtTelefono, txtYear,txtKm;
    Spinner spinMarca, spinModelo;
    FirebaseFirestore db;
    String cod;
    private static final int fotoenviada = 1;
    private FirebaseStorage storage;
    public static ArrayList<String> listaimg = new ArrayList<String>();
    private Uri imgUri;
    private StorageTask task;
    private RecyclerView mRecyclerView;
    private imgAdapter mAdapter;
    private List<imgUpload> mUploads;
    Location nueva;
    private GoogleMap mMap;
    private MapView mMapView;
    private RadioButton rdFijo,rdNego;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Deja esteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee xD
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_publicacion);
        txtTitulo = findViewById(R.id.txtTitulo);
        spinMarca = findViewById(R.id.spinMarca);
        spinModelo = findViewById(R.id.spinModelo);
        txtYear = findViewById(R.id.txtAnio);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtKm = findViewById(R.id.txtKm);
        txtTelefono=findViewById(R.id.txtTel);
        rdFijo=findViewById(R.id.rdFijo);
        rdNego=findViewById(R.id.rdNego);
        btImg = findViewById(R.id.btImg);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mRecyclerView=findViewById(R.id.recycler_imgpublic);
        mMapView = findViewById(R.id.mapa);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(EditarPublicacion.this));
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(EditarPublicacion.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(horizontalLayoutManager);
        Toast.makeText(EditarPublicacion.this,"long "+longitude,Toast.LENGTH_SHORT).show();

        Bundle extras = getIntent().getExtras();

        if (extras != null ) {
            cod = extras.getString("publicacionCod");
            datos_publicacion();
        }

        spinMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                idSelectModel=0;
                obtenermodelo();
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
                startActivityForResult(Intent.createChooser(i, "Seleccionar imagenes"), fotoenviada);
            }
        });

        initGoogleMap(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Context myContext = this;
        switch (item.getItemId())
        {
            case R.id.menu_catalogo:
                // Toast.makeText(MainActivity.this, "catalogo", Toast.LENGTH_LONG).show();
                startActivity(new Intent(myContext, Catalogo.class));
                // finish();
                return true;
            case R.id.menu_explorar:
                startActivity(new Intent(myContext, home.class));
                // Toast.makeText(home.this, "Explorar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_publicar:
                startActivity(new Intent(myContext, publicar.class));
                //Toast.makeText(home.this, "publicar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_perfil:
                startActivity(new Intent(myContext, Perfil.class));
                //Toast.makeText(home.this, "perfil", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_mensajes:
                //startActivity(new Intent(home.this, Chat.class));
                Toast.makeText(myContext, "mensajes", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_salir:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getBaseContext(), Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                //Toast.makeText(home.this, "Salir", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

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
                        if(listaimg.size()==0)
                        {
                            listaimg = (ArrayList<String>) document.get("list_img");
                        }

                        if(longitude==0.0)
                        {
                            longitude=Double.parseDouble(document.get("longitud").toString());
                            latitude=Double.parseDouble(document.get("latitud").toString());
                            Toast.makeText(EditarPublicacion.this, +latitude+"l: "+longitude, Toast.LENGTH_LONG).show();
                        }

                        if(radiobt==null)
                        {
                            radiobt=document.get("PrecioTipo").toString();
                            if(radiobt.equals("Fijo"))
                            {
                                rdFijo.setChecked(true);
                            }
                            else
                            {
                                rdNego.setChecked(true);
                            }
                        }


                        txtDescripcion.setText(document.get("descripcion").toString());
                        txtPrecio.setText(document.get("precio").toString());
                        txtKm.setText(document.get("kilometraje").toString());
                        txtTelefono.setText(document.get("Telefono").toString());
                        txtTitulo.setText(document.get("titulo").toString());
                        txtYear.setText(document.get("año").toString());
                        marcabase = document.get("marca").toString();
                        modelobase = document.get("modelo").toString();
                        mostrarImg(listaimg);
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
        int pos = -1;

        if (modelolista == null) {
            //txtt.setText("nulo");
        } else {

            for (int i = 0; i <= modelolista.size()-1 ; i++) {
                listamodelo.add(modelolista.get(i).modelo);
                if (modelobase.equals(modelolista.get(i).modelo)) {
                    pos = i;
                }
            }

            ArrayAdapter<String> miAdaptador = new ArrayAdapter<>(EditarPublicacion.this, android.R.layout.simple_spinner_item, listamodelo);
            spinModelo.setAdapter(miAdaptador);
            if(pos>=0)
            {
                spinModelo.setSelection(pos+1 );
            }


            if (idSelectModel > 0) {
                int id = (int) idSelectModel;

                spinModelo.setSelection(id);
            }

        }

    }

    public void Guardar(View view) {
        // if(task!=null &&task.isInProgress())
        // {
        //     Toast.makeText(EditarPublicacion.this,"Porfavor espere que las img se suban.",Toast.LENGTH_SHORT).show();
        // }
        // else
        // {
        String titulo = txtTitulo.getText().toString();
        String marca = spinMarca.getSelectedItem().toString();
        String modelo = spinModelo.getSelectedItem().toString();
        int anio = Integer.parseInt(txtYear.getText().toString());
        double precio = Double.parseDouble(txtPrecio.getText().toString());
        String descrip = txtDescripcion.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String km=txtKm.getText().toString();
        String preciotipo;
        if(rdFijo.isChecked())
        {
            preciotipo="Fijo";
        }
        else
        {
            preciotipo="Negociable";
        }

        // DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        // String date = df.format(Calendar.getInstance().getTime());


        Map<String, Object> editarPublicacion = new HashMap<>();
        editarPublicacion.put("titulo", titulo);
        editarPublicacion.put("marca", marca);
        editarPublicacion.put("modelo", modelo);
        editarPublicacion.put("año", anio);
        editarPublicacion.put("kilometraje", km);
        editarPublicacion.put("precio", precio);
        editarPublicacion.put("descripcion", descrip);
        editarPublicacion.put("Telefono", telefono);
        editarPublicacion.put("estado", "activo");
        //editarPublicacion.put("fecha_publicacion", date);
        // editarPublicacion.put("id_usuario",  mAuth.getCurrentUser().getUid());
        editarPublicacion.put("list_img", listaimg);
        editarPublicacion.put("latitud",latitude);
        editarPublicacion.put("longitud",longitude);
        editarPublicacion.put("ciudad",ciudad);
        editarPublicacion.put("PrecioTipo",preciotipo);


        db.collection("publicacion").document(cod)
                .update(editarPublicacion)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //startActivity(new Intent(EditarPublicacion.this, Perfil.class));
                        listaimg.removeAll(listaimg);
                        radiobt=null;
                        idselectMarca=0;
                        idSelectModel=0;
                        longitude=0.0;
                        latitude=0.0;
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        // }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == fotoenviada && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int cantidadselect = data.getClipData().getItemCount();
                //listaimg = new ArrayList<String>();
                for (int i = 0; i < cantidadselect; i++) {
                    u = data.getClipData().getItemAt(i).getUri();
                     guardarImg(u);

                }
            } else if (data.getData() != null) {
                // listaimg = new ArrayList<String>();
                u = data.getData();
                guardarImg(u);
            }
        }
        else if (requestCode == 1234 && resultCode == RESULT_OK)
        {
            if(data.getExtras().getString("longitud")!=null )
            {
                Toast.makeText(EditarPublicacion.this,data.getExtras().getString("latitud")+data.getExtras().getString("longitud")+data.getExtras().getString("ciudad"),Toast.LENGTH_SHORT).show();
                latitude=Double.parseDouble(data.getExtras().getString("latitud"));
                longitude=Double.parseDouble(data.getExtras().getString("longitud"));
                ciudad=data.getExtras().getString("ciudad");
            }
            else
            {
                Toast.makeText(EditarPublicacion.this,"Debe ingresar una ubicación",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void guardarImg(Uri u) {
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
                    listaimg.add(downloadUrl.toString());
                    Toast.makeText(EditarPublicacion.this,"Se han subido: "+listaimg.size()+" img.",Toast.LENGTH_LONG).show();
                    mostrarImg(listaimg);
                }
            }
        });
    }

    public void mostrarImg(ArrayList<String> arrayImg )
    {
        imgUpload Listaimg[] = new imgUpload[arrayImg.size()];
        for (int i = 0; i <= Listaimg.length - 1; i++) {
            imgUpload imgs=new imgUpload(arrayImg.get(i));
            Listaimg[i]=imgs;
        }
        mAdapter=new imgAdapter(EditarPublicacion.this, Listaimg);

        mAdapter.setOnClickListener(EditarPublicacion.this);
        mRecyclerView.setAdapter(mAdapter);
        //  pgBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(EditarPublicacion.this,"Mantenga presionado para activar las opciones.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        listaimg.remove(position);
        mostrarImg(listaimg);
        // StorageReference storageRef = storage.getReference();
        // final StorageReference fotoReferencia = storageRef.child("img_publicacion/" + u.getLastPathSegment());

    }

    @Override
    public void onAgregarClick(int position) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        nueva=new Location("nueva");
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //googleMap.setMyLocationEnabled(true);
        if(longitude==0.0 )
        {
            ObtenerLocale(googleMap);

        }
        else
        {
            nueva.setLatitude(latitude);
            nueva.setLongitude(longitude);
            LatLng sydney = new LatLng(nueva.getLatitude(), nueva.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("Mi publicacion"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

    }



    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("mapViewBundleKey");
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    public void NuevaUbicacion(View view) {
        Intent ventana= new Intent(EditarPublicacion.this, MapsActivity.class);
        startActivityForResult(ventana,1234);
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        listaimg.removeAll(listaimg);
        idselectMarca=0;
        idSelectModel=0;
        longitude=0.0;
        latitude=0.0;
        radiobt=null;
    }

    public void ObtenerLocale(final GoogleMap googleMap) {

        DocumentReference docRef = db.collection("publicacion").document(cod);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mMap = googleMap;
                        longitude=Double.parseDouble(document.get("longitud").toString());
                        latitude=Double.parseDouble(document.get("latitud").toString());
                        LatLng sydney = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("Mi publicacion"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,12));
                        mMap.getUiSettings().setZoomControlsEnabled(true);
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
