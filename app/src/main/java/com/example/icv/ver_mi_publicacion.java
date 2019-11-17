package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icv.publicacion.EditarPublicacion;
import com.example.icv.publicacion.publicar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ver_mi_publicacion extends AppCompatActivity {


    FirebaseFirestore db;
    String cod;
    String codView;

    TextView txtTitulo, txtDescripcion, txtVendedor, txtPrecio, txtTelefono, txtYear, txtMarca, txtModelo, txtLugar, txttypeP;

    Button btnVer_info;

    RecyclerView myRecyclerViewSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mi_publicacion);

        txtTitulo = findViewById(R.id.txtTituloAnuncio);
        txtDescripcion = findViewById(R.id.txtComentario);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtTelefono = findViewById(R.id.txtTelefono);

        txtMarca = findViewById(R.id.txtMarca);
        txtModelo = findViewById(R.id.txtModelo);
        txtYear = findViewById(R.id.txtYear);

        txttypeP = findViewById(R.id.txttypePrcio2);

        txtLugar = findViewById(R.id.txtLugarP);

        btnVer_info = findViewById(R.id.btnInformacion);

        // easySlider = findViewById(R.id.slider);
        db = FirebaseFirestore.getInstance();

        myRecyclerViewSlider = findViewById(R.id.myRecycleSlider);

        myRecyclerViewSlider.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(ver_mi_publicacion.this, LinearLayoutManager.HORIZONTAL, false);
        myRecyclerViewSlider.setLayoutManager(horizontalLayoutManager);



        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            cod = extras.getString("publicacionCod");

//            codView = extras.getString("CodU");

           // Toast.makeText(getApplicationContext(), cod, Toast.LENGTH_LONG).show();

           datos_publicacion();
        }
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

    public void datos_publicacion(){

        DocumentReference docRef = db.collection("publicacion").document(cod);

        docRef.get().addOnCompleteListener(new OnCompleteListener <DocumentSnapshot>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList <String> imgs = new ArrayList <String>();
                        imgs = (ArrayList<String>) document.get("list_img");

                        txtDescripcion.setText(document.get("descripcion").toString());

                        NumberFormat nf = NumberFormat.getInstance();
                        nf = NumberFormat.getInstance(Locale.ENGLISH);

                        Double precio = Double.parseDouble(document.get("precio").toString());

                        txtPrecio.setText( " $ " + nf.format(precio));
                        txtTelefono.setText(document.get("Telefono").toString());
                        txtTitulo.setText(document.get("titulo").toString());

                        txtMarca.setText(document.get("marca").toString());
                        txtModelo.setText(document.get("modelo").toString());
                        txtYear.setText(document.get("año").toString());
                        txtLugar.setText(document.get("ciudad").toString());
                        txttypeP.setText(document.get("PrecioTipo").toString());


                        String sliderItems[] = new String[imgs.size()];
                        for(int i = 0; i <=imgs.size() -1; i++)
                        {
                            // Toast.makeText(ver_publicacion.this, imgs.get(i), Toast.LENGTH_LONG).show();
                            sliderItems[i] = imgs.get(i);
                        }

                        adapter_slider adaptador = new adapter_slider(ver_mi_publicacion.this, sliderItems, cod);
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
    }

    public void editarPublicacion(View view) {
     Intent intent = new Intent(this, EditarPublicacion.class);
     intent.putExtra("publicacionCod", cod);
     startActivity(intent);
    }
}
