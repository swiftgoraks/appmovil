package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Resgistrarse extends AppCompatActivity {

    private EditText nombreEdit, CorreoEdit, PassEdit;
    private String nombre, correo, pass;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistrarse);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        nombreEdit = findViewById(R.id.nombre);
        CorreoEdit = findViewById(R.id.correo);
        PassEdit = findViewById(R.id.password);


    }

    public  void  registrarse(View view){

        nombre = nombreEdit.getText().toString();
        correo = CorreoEdit.getText().toString();
        pass = PassEdit.getText().toString();

        if (!nombre.isEmpty() && !correo.isEmpty() && !pass.isEmpty()) {

            if (pass.length() >= 6) {

                mAuth.createUserWithEmailAndPassword(correo, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            crearUSerDB(mAuth.getCurrentUser().getUid(),nombre, pass, correo);
                        }else
                        {
                            Toast.makeText(Resgistrarse.this, "Un error", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
            else  {

                Toast.makeText(Resgistrarse.this, "La contrasena debe de tener almenos 6 caracteres", Toast.LENGTH_LONG).show();

            }
        }
        else {
            Toast.makeText(Resgistrarse.this, "Debe de completar todos los datos", Toast.LENGTH_LONG).show();

        }

    }

    public void goLogin(View view){

        startActivity(new Intent(Resgistrarse.this, Login.class));
    }

    public void crearUSerDB(final String idU, String nombreU, String passU, String  emailU){

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Nombre", nombreU);
        user.put("Correo", emailU);
        user.put("Contrasena", passU);
        user.put("UrlImagen", "");


        db.collection("usuarios").document(idU).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //startActivity(new Intent(Resgistrarse.this, home.class));
                //intent.putExtra("idU", idU);
                //finish();

                Intent intent  = new Intent(Resgistrarse.this, home.class);
                intent.putExtra("idU", mAuth.getCurrentUser().getUid());
                startActivities(new Intent[]{intent});
                finish();
            }
        });
    }
}
