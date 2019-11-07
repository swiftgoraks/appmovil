package com.example.icv;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.icv.publicacion.publicar;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity {

    FirebaseFirestore db;

    RecyclerView myRecyclerViewMP, myRecyclerViewMF;

    String codViewU;


    ///

    String id_anuncioF[];
    String tituloF[];
    Integer yearF[];
    String modeloF[];
    String marcaF[];
    String id_usuarioF[];
    Double precioF[];
    String estadoF[] ;
    String descripcionF[];
    ArrayList<String> imgsF[] ;
    String fechaF[];
    String ciudad[];
   // String if_favoritoS[] ;

    String idPI = null;
    int contarPF;

    TextView lblNuser , lblCorreoUser;

    ImageView imagenPerfil;

    Button btnCambiarImg;

    private StorageReference mStorage;

    private FirebaseStorage storage;

    CircularImageView circularImageView;

    ///
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        circularImageView = findViewById(R.id.imagenCircular);

        storage=FirebaseStorage.getInstance();

        mStorage = FirebaseStorage.getInstance().getReference();
        //imagenPerfil = findViewById(R.id.PhotoPerfil);
        btnCambiarImg = findViewById(R.id.btnChangeFoto);

        db = FirebaseFirestore.getInstance();
        myRecyclerViewMP = findViewById(R.id.myRecycleViewViewMP);

        myRecyclerViewMP.setHasFixedSize(true);

        myRecyclerViewMP.setLayoutManager(new LinearLayoutManager(this));


        ///
        myRecyclerViewMF = findViewById(R.id.myRecycleViewViewMF);

        myRecyclerViewMF.setHasFixedSize(true);
        myRecyclerViewMF.setItemAnimator(new DefaultItemAnimator());
        myRecyclerViewMF.setLayoutManager(new LinearLayoutManager(this));
        //

        lblNuser = findViewById(R.id.lblNomUser);
        lblCorreoUser = findViewById(R.id.lblEmailUser);

        Bundle extras  = getIntent().getExtras();

        //if (extras != null){

            codViewU = FirebaseAuth.getInstance().getUid();

        //}

       // Toast.makeText(this, codViewU, Toast.LENGTH_LONG).show();

        TabHost tabs = findViewById(R.id.miTabHost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Mis Publicaciones", null);
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Mis Favoritos", null);
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
               // Toast.makeText(Perfil.this, "Tab " + s, Toast.LENGTH_LONG).show();

                if(s.equals("mitab2")){
                    cargarMF();
                }
            }
        });

        cargar_MP();
        cargarUsuario();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
               // startActivity(new Intent(myContext, Login.class));
               // finish();

                startActivity(new Intent(getBaseContext(), Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                //Toast.makeText(home.this, "Salir", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void cargar_MP(){

        db.collection("publicacion").whereEqualTo("id_usuario", codViewU)
                .get()
                .addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            String id_anuncio[] = new String[task.getResult().size()];
                            String titulo[] = new String[task.getResult().size()];
                            Integer year[] = new Integer[task.getResult().size()];
                            String modelo[] = new String[task.getResult().size()];
                            String marca [] = new String[task.getResult().size()];
                            String id_usuario [] = new String[task.getResult().size()];
                            Double precio[] = new Double[task.getResult().size()];
                            String estado [] = new String[task.getResult().size()];
                            String descripcion [] = new String[task.getResult().size()];
                            ArrayList <String> imgs[] = new ArrayList <>().toArray(new ArrayList[task.getResult().size()]);
                            String fecha[] = new String[task.getResult().size()];
                            int contador = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {



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





                            Anuncio anunciosLista[] = new Anuncio[titulo.length];
                            for(int i = 0; i <=titulo.length - 1; i++)
                            {
                                Anuncio anuncios =  new Anuncio(id_anuncio[i],titulo[i],year[i], modelo[i], marca[i], id_usuario[i], precio[i], estado[i], descripcion[i], imgs[i], fecha[i], null, null);

                                anunciosLista[i] = anuncios;
                            }


                            AdaptadorMisPublicaciones adaptador = new AdaptadorMisPublicaciones(Perfil.this, anunciosLista, codViewU);
                            myRecyclerViewMP.setAdapter(adaptador);


                        } else {
                            Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }


                    }
                });

    }

    public void cargarMF(){

////

        db.collection("usuario_fav").whereEqualTo("id_usuario", codViewU)
                .get()
                .addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            final String id_anuncioFF[] = new String[task.getResult().size()];
                            final String id_Favorito[] = new String[task.getResult().size()];

                            int contador = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {



                                id_anuncioFF[contador] = document.get("id_anuncio").toString();
                                id_Favorito[contador] = document.getId();

                                contador = contador + 1;

                            }

                           id_anuncioF = new String[id_anuncioFF.length];
                             tituloF = new String[id_anuncioFF.length];
                             yearF = new Integer[id_anuncioFF.length];
                             modeloF = new String[id_anuncioFF.length];
                            marcaF = new String[id_anuncioFF.length];
                             id_usuarioF = new String[id_anuncioFF.length];
                             precioF = new Double[id_anuncioFF.length];
                             estadoF = new String[id_anuncioFF.length];
                             descripcionF = new String[id_anuncioFF.length];
                             imgsF= new ArrayList <>().toArray(new ArrayList[id_anuncioFF.length]);
                             fechaF = new String[id_anuncioFF.length];
                             ciudad = new String[id_anuncioFF.length];

                             contarPF = 0 ;

                                db.collection("publicacion")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {



                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        for(int i = 0; i <=id_anuncioFF.length - 1; i++)
                                                        {
                                                            if(id_anuncioFF[i].equals(document.getId())){

                                                                id_anuncioF[contarPF] = document.getId();
                                                                tituloF[contarPF] = document.get("titulo").toString();
                                                                yearF[contarPF] =Integer.parseInt(document.get("año").toString());
                                                                modeloF[contarPF] = document.get("modelo").toString();
                                                                marcaF[contarPF] = document.get("marca").toString();
                                                                id_usuarioF[contarPF] = document.get("id_usuario").toString();
                                                                precioF[contarPF] =  Double.parseDouble(document.get("precio").toString());
                                                                estadoF[contarPF] = document.get("estado").toString();
                                                                descripcionF[contarPF] = document.get("descripcion").toString();
                                                                imgsF[contarPF] = (ArrayList<String>) document.get("list_img");
                                                                fechaF[contarPF] = document.get("fecha_publicacion").toString();
                                                                ciudad[contarPF] = document.get("ciudad").toString();
                                                                contarPF = contarPF + 1;
                                                            }
                                                        }

                                                    }

                                                    Anuncio anunciosListaF[] = new Anuncio[tituloF.length];
                                                    for(int i = 0; i <=tituloF.length - 1; i++)
                                                    {
                                                      Anuncio anunciosF =  new Anuncio(id_anuncioF[i],tituloF[i],yearF[i], modeloF[i], marcaF[i], id_usuarioF[i], precioF[i], estadoF[i], descripcionF[i], imgsF[i], fechaF[i], id_Favorito[i], ciudad[i]);

                                                      anunciosListaF[i] = anunciosF;
                                                        //Toast.makeText(Perfil.this, tituloF[0] + " " + tituloF[1], Toast.LENGTH_LONG).show();
                                                   }


                                                    AdaptadorMisFavoritos adaptadorF = new AdaptadorMisFavoritos(Perfil.this, anunciosListaF, codViewU);
                                                    myRecyclerViewMF.setAdapter(adaptadorF);

                                                } else {
                                                    Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                                                }


                                            }
                                        });





                        } else {
                            Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }


                    }
                });


////



    }

    public void cargarUsuario(){
        db.collection("usuarios").document(codViewU).get()
                .addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                        lblNuser.setText(String.valueOf(task.getResult().get("Nombre")));
                        lblCorreoUser.setText(String.valueOf(task.getResult().get("Correo")));

                        if(!String.valueOf(task.getResult().get("UrlImagen")).isEmpty()){
                            //Glide.with(Perfil.this)
                                    //.load(String.valueOf(task.getResult().get("UrlImagen")))
                                  //  .into(imagenPerfil);

                          //  circularImageView.setImageURI((Uri) task.getResult().get("UrlImagen"));
                            Glide.with(getApplicationContext())
                                    .load(String.valueOf(task.getResult().get("UrlImagen")))
                                    .into(circularImageView);
                           // circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);
                            circularImageView.setTag(task.getResult().get("UrlImagen"));
                            //imagenPerfil.setTag(String.valueOf(task.getResult().get("UrlImagen")));
                        }else {
                            circularImageView.setTag("");
                        }

                    }
                });


    }

    public void cambiarFoto(View view) {

        cargarImagen();
    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccionar la aplicacion"), 10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data.getData()!=null)
            {
               // listaimg=new ArrayList<String>();

if(!circularImageView.getTag().toString().isEmpty()){
    // Create a reference to the file to delete

}

                Uri u=data.getData();
                //Toast.makeText(publicar.this,"Una foto ", Toast.LENGTH_LONG).show();
                //prueba(u);
                guardarImg(u);
            }

        }
    }

    public void guardarImg(Uri u){

        Random aleatorio = new Random(System.currentTimeMillis());
// Producir nuevo int aleatorio entre 0 y 500
        int intAletorio = aleatorio.nextInt(100);
        int intAletorio2 = aleatorio.nextInt(150);

        Toast.makeText(getApplicationContext(), "Subiendo Imagen . . .", Toast.LENGTH_LONG).show();

        StorageReference storageRef = storage.getReference();
        final  StorageReference fotoReferencia = storageRef.child("img_perfil/perfil_"+u.getLastPathSegment() + intAletorio + intAletorio2);

        fotoReferencia.putFile(u).continueWithTask(new Continuation <UploadTask.TaskSnapshot, Task<Uri>>() {
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


                    guadarImgDB(downloadUrl);
                //Toast.makeText(getApplicationContext(), downloadUrl.toString(), Toast.LENGTH_LONG).show();
                    //mostrarImg(listaimgs);
                }
            }
        });

    }

    public void guadarImgDB(final Uri urlImg){

        DocumentReference usuarioPerfil = db.collection("usuarios").document(codViewU);

// Set the "isCapital" field of the city 'DC'
        usuarioPerfil
                .update("UrlImagen", urlImg.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");

                       // Glide.with(getApplicationContext()).load(urlImg.toString()).into(imagenPerfil);

                        Glide.with(getApplicationContext())
                                .load(urlImg)
                                .into(circularImageView);
                        //circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);
                        circularImageView.setTag(urlImg);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}
