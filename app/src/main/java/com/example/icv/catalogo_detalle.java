package com.example.icv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class catalogo_detalle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_detalle);

        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            //idU = extras.getString("idU");

            Toast.makeText(this, extras.getString("marca"), Toast.LENGTH_LONG).show();
        }
    }
}
