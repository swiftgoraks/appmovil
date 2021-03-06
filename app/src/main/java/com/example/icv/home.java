package com.example.icv;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icv.Chat.listadoUsuarioActivity;
import com.example.icv.pasarela.pago;
import com.example.icv.publicacion.publicar;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.icv.R.drawable.busqueda;
import static com.example.icv.R.drawable.exito;
import static com.example.icv.R.mipmap.ic_launcher;

public class home extends AppCompatActivity  implements filtroClass.FinalizoCuadroDialogo {


    public FirebaseAuth mAuth;
    public TextView textBienvienido;
    private Button btPublicar;

    int cantidad;
    DocumentReference docRef;
    TextView txtCantidad;
    Boolean filtro = false;



    RecyclerView myRecyclerView;

    FirebaseFirestore db;

    static String idU;

    ImageView imgCancelar, imgBusqueda;

    Button btnFiltro;
    private AdView mAdView;
    String ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
      //  btPublicar=findViewById(R.id.btPublicar);

        Bundle extras  = getIntent().getExtras();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        idU= mAuth.getCurrentUser().getUid();

        //MobileAds.initialize(this, "ca-app-pub-6481675684933795~9363671043");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        docRef = db.collection("usuarios").document(  mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ads=String.valueOf(task.getResult().get("anuncios"));
                    mAdView = findViewById(R.id.adView);
                    if(ads.equals("si"))
                    {

                        AdRequest adRequest = new AdRequest.Builder().build();
                        mAdView.loadAd(adRequest);
                    }
                    else
                    {
                        mAdView.setVisibility(View.GONE);
                    }

                }
            }
        });
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        txtCantidad=findViewById(R.id.txtCantidadHome);
        imgCancelar = findViewById(R.id.imgCancelar);
        imgBusqueda = findViewById(R.id.imgBuscar);

       //if (extras != null){
           //idU = extras.getString("idU");
       //}

        btnFiltro = findViewById(R.id.btnFiltro);

        filtro = false;


       // textBienvienido = findViewById(R.id.txtBienvenido);

        myRecyclerView = findViewById(R.id.myRecycleViewView);

        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       // textBienvienido.setText(mAuth.getCurrentUser().getEmail());

        cargarAnuncios();

       // btPublicar.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
            //    startActivity(new Intent(home.this, publicar.class));
          //  }
        //});

        getSupportActionBar().setIcon(ic_launcher);
        //getSupportActionBar().ico
       // getActionBar().setIcon(R.mipmap.ic_launcher_round);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menu_catalogo:
                // Toast.makeText(MainActivity.this, "catalogo", Toast.LENGTH_LONG).show();
                startActivity(new Intent(home.this, Catalogo.class));
                // finish();
                return true;
            case R.id.menu_explorar:
                startActivity(new Intent(home.this, home.class));
               // Toast.makeText(home.this, "Explorar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_publicar:

                docRef = db.collection("usuarios").document(  mAuth.getCurrentUser().getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            txtCantidad.setText(String.valueOf(task.getResult().get("cant_publicacion")));
                            cantidad= Integer.parseInt(txtCantidad.getText().toString());
                            if (cantidad>0)
                            {
                                startActivity(new Intent(home.this, publicar.class));
                            }
                            else
                            {
                                Toast.makeText(home.this, "Ya no dispone de publicaciones, debe comprar algún paquete.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                //Toast.makeText(home.this, "publicar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_perfil:
                filtro = false;
                startActivity(new Intent(home.this, Perfil.class));
                //Toast.makeText(home.this, "perfil", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_mensajes:
                startActivity(new Intent(home.this, listadoUsuarioActivity.class));
                //Toast.makeText(home.this, "mensajes", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_compra:
                startActivity(new Intent(home.this, pago.class));
                //Toast.makeText(home.this, "mensajes", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_salir:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getBaseContext(), Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                //Toast.makeText(home.this, "Salir", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(filtro != true){
            cargarAnuncios();
        }

    }

    public void cargarAnuncios(){

       // Toast.makeText(home.this,"usuario: "+idU, Toast.LENGTH_SHORT).show();
        db.collection("publicacion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                         //   Toast.makeText(home.this,String.valueOf(task.getResult().size()), Toast.LENGTH_LONG ).show();

                            String id_anuncio[] = new String[task.getResult().size()];
                            String titulo[] = new String[task.getResult().size()];
                            Integer year[] = new Integer[task.getResult().size()];
                            String modelo[] = new String[task.getResult().size()];
                            String marca [] = new String[task.getResult().size()];
                            String id_usuario [] = new String[task.getResult().size()];
                            Double precio[] = new Double[task.getResult().size()];
                            String estado [] = new String[task.getResult().size()];
                            String descripcion [] = new String[task.getResult().size()];
                            ArrayList<String> imgs[] = new ArrayList <>().toArray(new ArrayList[task.getResult().size()]);
                            String fecha[] = new String[task.getResult().size()];
                            int contador = 0;
                            int contadorN = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {

    if (!document.get("id_usuario").toString().equals(idU)){
        id_anuncio[contador] = document.getId();
        titulo[contador] = document.get("titulo").toString();
        year[contador] =Integer.parseInt(document.get("año").toString());
        modelo[contador] = document.get("modelo").toString();
        marca[contador] = document.get("marca").toString();
        id_usuario[contador] = document.get("id_usuario").toString();
        precio[contador] =  Double.parseDouble(document.get("precio").toString());
        estado[contador] = document.get("estado").toString();
        descripcion[contador] = document.get("descripcion").toString();
        imgs[contador] = (ArrayList<String>) document.get("list_img");
        fecha[contador] = document.get("fecha_publicacion").toString();
        contador = contador + 1;
    }
    else {
        contadorN = contadorN +1;
    }

                            }

                            Anuncio anunciosLista[] = new Anuncio[titulo.length - contadorN];
                            for(int i = 0; i <=anunciosLista.length - 1; i++)
                            {
            Anuncio anuncios =  new Anuncio(id_anuncio[i],titulo[i],year[i], modelo[i], marca[i], id_usuario[i], precio[i], estado[i], descripcion[i], imgs[i], fecha[i], null, null);

                                anunciosLista[i] = anuncios;
                            }


                           MyAdapter adaptador = new MyAdapter(home.this, anunciosLista, idU);
                            myRecyclerView.setAdapter(adaptador);

                        } else {
                            Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }


                    }
                });

    }

    public void cerrarSesion(View view){

        //mAuth.signOut();
        startActivity(new Intent(home.this, MainActivity.class));
        finish();
    }


    public void abrirDialogo(View view) {
        new filtroClass(this,home.this);
    }


    public void cargarAnunciosFiltro(String marca, String modeloE){

        Query query = null;
        Query queryTemp = null;


                if(!modeloE.equals("")){
                    query =  db.collection("publicacion").whereEqualTo("marca", marca)
                            .whereEqualTo("modelo", modeloE);
                }else {
                    query = db.collection("publicacion").whereEqualTo("marca", marca);
                }



        //db.collection("publicacion").whereEqualTo("marca", marca)
                query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            //   Toast.makeText(home.this,String.valueOf(task.getResult().size()), Toast.LENGTH_LONG ).show();

                            String id_anuncio[] = new String[task.getResult().size()];
                            String titulo[] = new String[task.getResult().size()];
                            Integer year[] = new Integer[task.getResult().size()];
                            String modelo[] = new String[task.getResult().size()];
                            String marca [] = new String[task.getResult().size()];
                            String id_usuario [] = new String[task.getResult().size()];
                            Double precio[] = new Double[task.getResult().size()];
                            String estado [] = new String[task.getResult().size()];
                            String descripcion [] = new String[task.getResult().size()];
                            ArrayList<String> imgs[] = new ArrayList <>().toArray(new ArrayList[task.getResult().size()]);
                            String fecha[] = new String[task.getResult().size()];
                            int contador = 0;
                            int contadorN = 0;
                            int marcaN = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (!document.get("id_usuario").toString().equals(idU)){


                                        id_anuncio[contador] = document.getId();
                                        titulo[contador] = document.get("titulo").toString();
                                        year[contador] =Integer.parseInt(document.get("año").toString());
                                        modelo[contador] = document.get("modelo").toString();
                                        marca[contador] = document.get("marca").toString();
                                        id_usuario[contador] = document.get("id_usuario").toString();
                                        precio[contador] =  Double.parseDouble(document.get("precio").toString());
                                        estado[contador] = document.get("estado").toString();
                                        descripcion[contador] = document.get("descripcion").toString();
                                        imgs[contador] = (ArrayList<String>) document.get("list_img");
                                        fecha[contador] = document.get("fecha_publicacion").toString();
                                        contador = contador + 1;



                                }
                                else {
                                    contadorN = contadorN +1;
                                }

                            }

                            Anuncio anunciosLista[] = new Anuncio[titulo.length - contadorN];
                            for(int i = 0; i <=anunciosLista.length - 1; i++)
                            {
                                Anuncio anuncios =  new Anuncio(id_anuncio[i],titulo[i],year[i], modelo[i], marca[i], id_usuario[i], precio[i], estado[i], descripcion[i], imgs[i], fecha[i], null, null);

                                anunciosLista[i] = anuncios;
                            }


                            MyAdapter adaptador = new MyAdapter(home.this, anunciosLista, idU);
                            myRecyclerView.setAdapter(adaptador);

                        } else {
                            Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }


                    }
                });

    }


    @Override
    public void resultado(String marca, String modelo) {

        if(!marca.equals("Ninguna")){
            filtro = true;
            if(!modelo.equals("Seleccione modelo")){
                cargarAnunciosFiltro(marca, modelo);
                imgCancelar.setVisibility(View.VISIBLE);
                imgBusqueda.setImageResource(exito);

            }
            else{
                cargarAnunciosFiltro(marca, "");
                imgCancelar.setVisibility(View.VISIBLE);
                imgBusqueda.setImageResource(exito);
            }

        }else{
            filtro = false;
            imgCancelar.setVisibility(View.INVISIBLE);
            imgBusqueda.setImageResource(busqueda);
            cargarAnuncios();
        }
    }

    public void cancelarFiltro(View view) {
        filtro = false;
        imgCancelar.setVisibility(View.INVISIBLE);
        imgBusqueda.setImageResource(busqueda);
        cargarAnuncios();
    }
}
