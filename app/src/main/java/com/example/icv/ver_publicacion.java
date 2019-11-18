package com.example.icv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icv.Chat.MensajeriaActivity;
import com.example.icv.Chat.listadoUsuarioActivity;
import com.example.icv.publicacion.publicar;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import ahmed.easyslider.EasySlider;

public class ver_publicacion extends AppCompatActivity {


    EasySlider easySlider;
    FirebaseFirestore db;
    String cod;
    String codView;

    TextView txtTitulo, txtDescripcion, txtVendedor, txtPrecio, txtTelefono, txtYear, txtMarca, txtModelo, txtLugar, txttypePrcio,txtIdVendedor;

    TextView txtMotorS;
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
        txtIdVendedor=findViewById(R.id.txtIDVendedor);
        txtMarca = findViewById(R.id.txtMarca);
        txtModelo = findViewById(R.id.txtModelo);
        txtYear = findViewById(R.id.txtYear);
        txttypePrcio = findViewById(R.id.txttypePrcio);
        txtMotorS = findViewById(R.id.txtMotor);
        txtLugar = findViewById(R.id.txtLugarP);
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
                Intent intent=new Intent(ver_publicacion.this, listadoUsuarioActivity.class);
                startActivity(intent);
               // Toast.makeText(myContext, "mensajes", Toast.LENGTH_LONG).show();
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
                        txtIdVendedor.setText(document.get("id_usuario").toString());
                        txtPrecio.setText( " $ " + nf.format(precio));
                        txtTelefono.setText(document.get("Telefono").toString());
                        txtTitulo.setText(document.get("titulo").toString());
                        txtMarca.setText(document.get("marca").toString());
                        txtModelo.setText(document.get("modelo").toString());
                        txtYear.setText(document.get("año").toString());
                        txtLugar.setText(document.get("ciudad").toString());
                        txttypePrcio.setText(document.get("PrecioTipo").toString());
                        txtMotorS.setText(document.get("Motor").toString());

                        String sliderItems[] = new String[imgs.size()];
                        for(int i = 0; i <=imgs.size() -1; i++)
                        {
                        // Toast.makeText(ver_publicacion.this, imgs.get(i), Toast.LENGTH_LONG).show();
                            sliderItems[i] = imgs.get(i);
                        }

                        adapter_slider adaptador = new adapter_slider(ver_publicacion.this, sliderItems, cod);
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

        Intent intent=new Intent(ver_publicacion.this, catalogo_filtro.class);
        intent.putExtra("marca",txtMarca.getText());
        intent.putExtra("modelo",txtModelo.getText());
        startActivity(intent);
    }

    public void verPerfil(View view) {
        //Intent intent = new Intent(ver_publicacion.this, Perfil.class);
       // intent.putExtra("idU", codView);
       // startActivity(intent);

        Intent intent=new Intent(ver_publicacion.this, MensajeriaActivity.class);
        intent.putExtra("key_receptor",txtIdVendedor.getText());
        intent.putExtra("nombre_receptor",txtVendedor.getText());
        startActivity(intent);
    }

    public void verImagnes(View view) {

        Intent intent  = new Intent(ver_publicacion.this, ver_imagenes.class);
        intent.putExtra("id_p", cod);
        startActivities(new Intent[]{intent});
    }
}
