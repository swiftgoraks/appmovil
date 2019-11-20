
package com.example.icv;

        import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.NumberFormat;
import java.util.Locale;

class AdaptadorMisFavoritos extends RecyclerView.Adapter<AdaptadorMisFavoritos.MyViewHolder> {
    // private String[] mDataset;
    Anuncio anunciosLista[];
    private Context mCtx;
    FirebaseFirestore db;
    String usuario_view;

    //Prueba click

    private AdaptadorMisFavoritos.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
   // AdaptadorMisFavoritos

  public AdaptadorMisFavoritos(Context myCtx,Anuncio MYanunciosLista[], String id_usuarioView) {
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
        View view  = hola.inflate(R.layout.list_mis_favoritos, null);
        //MyViewHolder vh = new MyViewHolder(v);


        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //Se envia la informacion que se quiere mostrar a los elementos del layout
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.textView.setText(mDataset[position]);

        Anuncio anun = anunciosLista[position];

        holder.txtTitulo.setText(anun.getTitulo());
        holder.txtFecha.setText(anun.getFecha_publicacion());
        holder.txtDescripcion.setText(anun.getDescripcion());


        NumberFormat nf = NumberFormat.getInstance();
        nf = NumberFormat.getInstance(Locale.ENGLISH);

        holder.txtPrecio.setText("$ " + nf.format(anun.getPrecio()));
        holder.txtId_pub.setText(anun.getId_anuncio());
        holder.txt_id_favorito.setText(anun.getId_favorito());
        holder.txtCiudad.setText(anun.getCiudad());

        /// Extrae informacion de favoritos del vendedor.
        DocumentReference docRef = db.collection("usuarios").document(anun.getId_usuario());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        holder.txtNombreUserV.setText(document.get("Nombre").toString());
                        if (document.get("UrlImagen").toString().equals("")){
                            holder.ProfileImage.setImageResource(R.drawable.usuario);
                        }
                        else {

                            //Glide.with(mCtx).load(document.get("UrlImagen").toString()).into(holder.ProfileImage);
                            //Picasso.get().load(document.get("UrlImagen").toString()).resize(128,128).into(holder.ProfileImage);
                            Glide.with(mCtx)
                                    .load(String.valueOf(document.get("UrlImagen")))
                                    .into(holder.ProfileImage);
                        }

                    } else {

                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });





        // StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("img_publicaciones/cabra.png");

/// Extrae informacion de favoritos del usuario logeado.



        Glide.with(mCtx).load(anun.getListImg().get(0)).into(holder.imgPortada);


        holder.setOnClickListeners();


    }

    @Override
    public int getItemCount() {

        return anunciosLista.length;
    }



    //Se enlanza controladores del layout con los del adapter
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtFecha, txtDescripcion, txtTitulo, txtId_pub, txt_fav, txtNombreUserV, txt_id_favorito;
        ImageView imgPortada, imgFav,  imgDefault;
        Button btnverMF, btnEliminarMF;
        Context contextoMy;
        TextView txtPrecio, txtCiudad;

        CircularImageView ProfileImage;



        public MyViewHolder(View itemView){
            super(itemView);



            contextoMy = itemView.getContext();

            txtFecha = itemView.findViewById(R.id.txtFechaPF);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionMF);

            txt_id_favorito = itemView.findViewById(R.id.txtId_favorito);

            btnverMF = itemView.findViewById(R.id.verMF);
            btnEliminarMF = itemView.findViewById(R.id.eliminarMF);
            imgPortada = itemView.findViewById(R.id.imgPortadaMF);
            txtTitulo = itemView.findViewById(R.id.txtTituloMF);
            txtPrecio = itemView.findViewById(R.id.txtPrecioMF);

            txtId_pub = itemView.findViewById(R.id.txtIdPMF);

            txtNombreUserV = itemView.findViewById(R.id.txtNomVendedor);

            ProfileImage = itemView.findViewById(R.id.PerfilMF);

            txtCiudad = itemView.findViewById(R.id.txtCiudadMF);

            //Prueba click

            //Fin prueba click

        }

        void setOnClickListeners(){
            btnEliminarMF.setOnClickListener(this);
            btnverMF.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.verMF:
                    Intent intent  = new Intent(contextoMy, ver_publicacion.class);
                    intent.putExtra("publicacionCod", txtId_pub.getText());
                    intent.putExtra("vendedor", txtNombreUserV.getText());
                    intent.putExtra("CodU", usuario_view);
                    contextoMy.startActivities(new Intent[]{intent});
                    break;
                case R.id.eliminarMF:
                        db.collection("usuario_fav").document(String.valueOf(txt_id_favorito.getText())).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //startActivity(new Intent(Resgistrarse.this, home.class));
                               Anuncio list_temp[] = new Anuncio[anunciosLista.length - 1];


                                for(int i =0 ; i <=list_temp.length -1; i++)
                                {
                                    if(!anunciosLista[i].equals(String.valueOf(txt_id_favorito.getText()))){
                                        list_temp[i] = anunciosLista[i];
                                    }
                                }

                                anunciosLista = list_temp;

                                notifyItemRemoved(getAdapterPosition());
                                Toast.makeText(mCtx,  "La publicación se ha eliminado de tus favoritos.", Toast.LENGTH_LONG).show();
                            }
                        });




                    break;

            }
        }
    }


}