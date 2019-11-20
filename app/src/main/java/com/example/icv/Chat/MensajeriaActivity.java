package com.example.icv.Chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icv.Chat.Adapter.adapter_msj;
import com.example.icv.Chat.Persistencia.MensajeriaDAO;
import com.example.icv.Chat.Persistencia.UsuarioDAO;
import com.example.icv.Chat.entidades.Base.mensaje;
import com.example.icv.Chat.entidades.Logica.LMensaje;
import com.example.icv.Chat.entidades.Logica.LUsuario;
import com.example.icv.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MensajeriaActivity extends AppCompatActivity {

    private TextView txtuser;
    private RecyclerView rvMsj;
    private EditText txtMsj;
    //private Button btEnviar;
    private ImageButton btImg,btEnviar;

    private adapter_msj adapter;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;

    private String usuariolog;
    private static final int fotoenviada=1;
    private String keyreceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);
        this.txtuser=findViewById(R.id.txtuser);
        this.rvMsj=findViewById(R.id.rvMsj);
        this.txtMsj=findViewById(R.id.txtMsj);
        this.btEnviar=findViewById(R.id.btEnviar);
        this.btImg=findViewById(R.id.btImg);

        Bundle bundle= getIntent().getExtras();
        if(bundle!=null)
        {
            keyreceptor=bundle.getString("key_receptor");
            txtuser.setText(bundle.getString("nombre_receptor"));
        }
        else
        {
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        //this.database= FirebaseDatabase.getInstance();
        //this.databaseReference=database.getReference("mensajes/"+UsuarioDAO.getInstance().getKeyUsuario()+"/"+keyreceptor);
        storage=FirebaseStorage.getInstance();

        this.adapter= new adapter_msj(this);
        LinearLayoutManager linear = new LinearLayoutManager(this);
        rvMsj.setLayoutManager(linear);
        rvMsj.setAdapter(adapter);

        this.btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensajeEnviar=txtMsj.getText().toString();
                if(!mensajeEnviar.isEmpty())
                {
                    mensaje Mensaje=new mensaje();
                    Mensaje.setMensaje(mensajeEnviar);
                    Mensaje.setContieneFoto(false);
                    Mensaje.setKeyEmisor(UsuarioDAO.getInstance().getKeyUsuario());
                    MensajeriaDAO.getInstance().nuevoMensaje(UsuarioDAO.getInstance().getKeyUsuario(),keyreceptor,Mensaje);
                    txtMsj.setText("");
                }
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollbar();
            }
        });

        FirebaseDatabase.getInstance().getReference("mensajes").child(mAuth.getCurrentUser().getUid()).child(keyreceptor).addChildEventListener(new ChildEventListener() {

            Map<String, LUsuario> mapUsuTemporal= new HashMap<>();


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final mensaje Mensaje  =dataSnapshot.getValue(mensaje.class);
                final LMensaje lMensaje=new LMensaje(Mensaje,dataSnapshot.getKey());
                final int posicion= adapter.agregarMsj(lMensaje);

                if(mapUsuTemporal.get(Mensaje.getKeyEmisor())!=null)
                {
                    lMensaje.setLusuario(mapUsuTemporal.get(Mensaje.getKeyEmisor()));
                    adapter.actualizarMsj(posicion,lMensaje);
                }
                else
                {
                    UsuarioDAO.getInstance().InfoUsuPorLlave(Mensaje.getKeyEmisor(), new UsuarioDAO.IDevolverUsu() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            mapUsuTemporal.put(Mensaje.getKeyEmisor(),lUsuario);
                            lMensaje.setLusuario(lUsuario);
                            adapter.actualizarMsj(posicion,lMensaje);
                        }

                        @Override
                        public void devolverError(String error) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        btImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(i,"Seleccionar imagen"),fotoenviada);
            }
        });
    }

    private void setScrollbar()
    {
        rvMsj.scrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==fotoenviada && resultCode==RESULT_OK)
        {
            Uri u=data.getData();
            StorageReference storageRef = storage.getReference();

            final  StorageReference fotoReferencia = storageRef.child(u.getLastPathSegment());

            fotoReferencia.putFile(u).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        mensaje Mensaje=new mensaje();
                        Mensaje.setMensaje("Ha enviado una foto");
                        Mensaje.setUrlfoto(downloadUrl.toString());
                        Mensaje.setContieneFoto(true);
                        Mensaje.setKeyEmisor(UsuarioDAO.getInstance().getKeyUsuario());
                        MensajeriaDAO.getInstance().nuevoMensaje(UsuarioDAO.getInstance().getKeyUsuario(),keyreceptor,Mensaje);
                        Toast.makeText(MensajeriaActivity.this, "Subida exitosamente", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
