package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.icv.publicacion.publicar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {

    GridView myGridView;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        myGridView = findViewById(R.id.gridView);

        db = FirebaseFirestore.getInstance();

        catalogo_data();

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
                startActivity(new Intent(getBaseContext(), Login.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                //Toast.makeText(home.this, "Salir", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void catalogo_data(){
 //

        db.collection("catalogo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            String nom_marca[] = new String[task.getResult().size()];
                            String url_img[] = new String[task.getResult().size()];
                            String id_marca[] = new String[task.getResult().size()];
                            int contador = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                nom_marca[contador] = document.get("marca").toString();
                                url_img[contador] = document.get("img_url").toString();
                                id_marca[contador] = document.getId();
                                contador = contador + 1;

                            }

            final GridAdapter gridAdapter = new GridAdapter(Catalogo.this, nom_marca, url_img,id_marca );
                            myGridView.setAdapter(gridAdapter);

                            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView <?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(Catalogo.this, catalogo_detalle.class);
                                    intent.putExtra("marca", gridAdapter.getItem(i).toString());
                                    startActivity(intent);
                                }
                            });


                            // Toast.makeText(home.this, titulo[0].toString(), Toast.LENGTH_LONG ).show();



                        } else {
                            Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                        }


                    }
                });
        ////
    }

}
