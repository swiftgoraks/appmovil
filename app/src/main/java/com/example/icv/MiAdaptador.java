package com.example.icv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
   // private String[] mDataset;
   Anuncio anunciosLista[];
    private Context mCtx;
     FirebaseFirestore db;
     String usuario_view;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    // Provide a suitable constructor (depends on the kind of dataset)
     //public MyAdapter(String[] myDataset) {
       //  mDataset = myDataset;
     //}

     public MyAdapter(Context myCtx,Anuncio MYanunciosLista[], String id_usuarioView) {
         anunciosLista = MYanunciosLista;
         mCtx = myCtx;
         db = FirebaseFirestore.getInstance();
         usuario_view = id_usuarioView;

     }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view\\

        LayoutInflater hola;
        hola =  LayoutInflater.from(mCtx);
        View view  = hola.inflate(R.layout.activity_list_layout, null);
        //MyViewHolder vh = new MyViewHolder(v);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.textView.setText(mDataset[position]);

        Anuncio anun = anunciosLista[position];

        holder.txtDescripcion.setText(anun.getTitulo());
        holder.txtFecha.setText(anun.getFecha_publicacion());
        holder.txtId_pub.setText(anun.getId_anuncio());



       // StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("img_publicaciones/cabra.png");


        db.collection("usuario_fav")
                .whereEqualTo("id_anuncio", anun.getId_anuncio())
                .whereEqualTo("id_usuario", usuario_view)
                .get()
                .addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                //Toast.makeText(mCtx, document.getId(), Toast.LENGTH_LONG).show();
                                holder.txt_fav.setText(document.getId());
                                holder.imgFav.setTag("conFav");
                                holder.imgFav.setImageResource(R.drawable.corazon2);

                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        Glide.with(mCtx).load(anun.getListImg().get(0)).into(holder.imgPortada);

        holder.setOnClickListeners();



// ImageView in your Activity


// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide GlideApp = null;






    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

         return anunciosLista.length;
    }




     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txtFecha, txtDescripcion, txtId_pub, txt_fav;
            ImageView imgPortada, imgFav;
            Button btnMas;
            Context contextoMy;


            public MyViewHolder(View itemView){
                super(itemView);

                contextoMy = itemView.getContext();

                txtFecha = itemView.findViewById(R.id.txtFecha);
                txtDescripcion = itemView.findViewById(R.id.txtTitulo);
                imgPortada = itemView.findViewById(R.id.imgPortada);
                txtId_pub = itemView.findViewById(R.id.idPublicacion);
                imgFav = itemView.findViewById(R.id.imgFav);
                btnMas = itemView.findViewById(R.id.btnVerMas);
                txt_fav = itemView.findViewById(R.id.id_fav);
            }

            void setOnClickListeners(){
                btnMas.setOnClickListener(this);
                imgFav.setOnClickListener(this);
            }

         @Override
         public void onClick(View view) {
             switch (view.getId()){

                 case R.id.btnVerMas:
                    Intent intent  = new Intent(contextoMy, MainActivity.class);
                    intent.putExtra("publicacionCod", txtId_pub.getText());
                    contextoMy.startActivities(new Intent[]{intent});
                     break;
                 case R.id.imgFav:


                     String backgroundImageName = String.valueOf(imgFav.getTag());

                     // Create a new user with a first and last name
                     Map <String, Object> user = new HashMap <>();
                     user.put("id_anuncio", txtId_pub.getText());
                     user.put("id_usuario", usuario_view);

                     if (backgroundImageName.equals("sinFav")){
                         //Toast.makeText(mCtx,  "conFav", Toast.LENGTH_LONG).show();



                         db.collection("usuario_fav")
                                 .add(user)
                                 .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                     @Override
                                     public void onSuccess(DocumentReference documentReference) {
                                         //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                                         txt_fav.setText(documentReference.getId());

                                         imgFav.setImageResource(R.drawable.corazon2);

                                         imgFav.setTag("conFav");
                                     }
                                 })
                                 .addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         //Log.w(TAG, "Error adding document", e);
                                     }
                                 });
                     }else if(backgroundImageName.equals("conFav")){

                         //Toast.makeText(mCtx,  "sinFav", Toast.LENGTH_LONG).show();

                         db.collection("usuario_fav").document(txt_fav.getText().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 //startActivity(new Intent(Resgistrarse.this, home.class));
                                 imgFav.setImageResource(R.drawable.corazon1);

                                 imgFav.setTag("sinFav");

                                 txt_fav.setText("");
                             }
                         });


                     }


                     break;

             }
         }
     }


}