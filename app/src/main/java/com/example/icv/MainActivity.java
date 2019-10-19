package com.example.icv;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtId = findViewById(R.id.idP);

        Bundle extras  = getIntent().getExtras();

        if (extras != null){
            txtId.setText(extras.getString("publicacionCod"));
        }
    }
}
