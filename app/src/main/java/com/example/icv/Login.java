package com.example.icv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login FaceBook" ;
    public EditText email;
    public EditText pass;
    public CardView btnLogin;

    String emailtext;
    String passtext;


    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.correo);
        pass = findViewById(R.id.password);


        mAuth = FirebaseAuth.getInstance();

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               // Log.d(TAG, "facebook:onSuccess:" + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());
                //Toast.makeText(Login.this, "Facebokk", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.


        if (mAuth.getCurrentUser() != null){


            Intent intent  = new Intent(Login.this, home.class);
            intent.putExtra("idU", mAuth.getCurrentUser().getUid());
            startActivities(new Intent[]{intent});
            finish();
        }
       // updateUI(currentUser);
    }

    public void entrar(View view) {

        emailtext = email.getText().toString();
        passtext = pass.getText().toString();

        if (!emailtext.isEmpty()&& !passtext.isEmpty()){

            loginEmail(emailtext, passtext);

        }
        else {
            Toast.makeText(Login.this, "Complete todos los datos", Toast.LENGTH_LONG).show();
        }
    }

    public void goSingIN(View view) {

        startActivity(new Intent(Login.this, Resgistrarse.class));
    }

    public void loginEmail(String e, String p){

    mAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
           if (task.isSuccessful()){
              // startActivity(new Intent(Login.this, home.class));
              // finish();

               Intent intent  = new Intent(Login.this, home.class);
               intent.putExtra("idU", mAuth.getCurrentUser().getUid());
               startActivities(new Intent[]{intent});
               finish();
           }else {
               Toast.makeText(Login.this, "No se pudo iniciar sesion", Toast.LENGTH_LONG).show();
           }
        }
    });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            crearUSerDB(task.getResult().getUser().getUid(), task.getResult().getUser().getDisplayName(), task.getResult().getUser().getEmail(), task.getResult().getUser().getPhotoUrl().toString());

                        } else {

                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void crearUSerDB(String idU, String nombreU, String  emailU,String imageUrl){

        DocumentReference usuarioPerfil = db.collection("usuarios").document(mAuth.getCurrentUser().getUid());
        if(usuarioPerfil == null){

            Map <String, Object> user = new HashMap <>();

            user.put("Nombre", nombreU);
            user.put("Correo", emailU);
            if (!(imageUrl == null)){
                user.put("UrlImagen", imageUrl);
            }
            else {
                user.put("UrlImagen", "");
            }

            db.collection("usuarios").document(idU).set(user).addOnSuccessListener(new OnSuccessListener <Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    Intent intent  = new Intent(Login.this, home.class);
                    intent.putExtra("idU", mAuth.getCurrentUser().getUid());
                    startActivities(new Intent[]{intent});
                    finish();
                }
            });
        }
        else {
            Intent intent  = new Intent(Login.this, home.class);
            intent.putExtra("idU", mAuth.getCurrentUser().getUid());
            startActivities(new Intent[]{intent});
            finish();

        }



    }

    public void registrarse(View view) {
    }

    public void goLogin(View view) {
    }
}
