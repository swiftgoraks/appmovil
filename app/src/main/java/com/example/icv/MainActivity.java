package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView txtId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                startActivity(new Intent(MainActivity.this, MainActivity.class));
               // finish();
                return true;
            case R.id.menu_explorar:
                Toast.makeText(MainActivity.this, "Explorar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_publicar:
                Toast.makeText(MainActivity.this, "publicar", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_perfil:
                Toast.makeText(MainActivity.this, "perfil", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_mensajes:
                Toast.makeText(MainActivity.this, "mensajes", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_salir:
               // FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Salir", Toast.LENGTH_LONG).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
