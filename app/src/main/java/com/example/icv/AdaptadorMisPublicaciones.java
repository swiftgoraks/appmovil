

package com.example.icv;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
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
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;

        import org.w3c.dom.Document;

        import java.sql.Timestamp;
        import java.text.SimpleDateFormat;
        import java.util.HashMap;
        import java.util.Map;

class AdaptadorMisPublicaciones extends RecyclerView.Adapter<AdaptadorMisPublicaciones.MyViewHolder> {
    // private String[] mDataset;
    Anuncio anunciosLista[];
    private Context mCtx;
    FirebaseFirestore db;
    String usuario_view;



    public AdaptadorMisPublicaciones(Context myCtx, Anuncio MYanunciosLista[], String codU) {
        anunciosLista = MYanunciosLista;
        mCtx = myCtx;
        db = FirebaseFirestore.getInstance();
        usuario_view = codU;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view\\

        LayoutInflater hola;
        hola =  LayoutInflater.from(mCtx);
        View view  = hola.inflate(R.layout.list_mis_publicaciones, null);
        //MyViewHolder vh = new MyViewHolder(v);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        Anuncio anun = anunciosLista[position];

        //holder.txtDescripcion.setText(anun.getTitulo());
       // holder.txtFecha.setText(anun.getFecha_publicacion());

       // if (usuario_view.equals(anun.getId_usuario())) {
            holder.txtTituloMP.setText(anun.getTitulo());
            holder.txtFecha.setText(anun.getFecha_publicacion());
            holder.txtPrecio.setText(String.valueOf(anun.getPrecio()));
            holder.txtDescripcion.setText(anun.getDescripcion());

        //}



        /// Extrae informacion de favoritos del vendedor.





        // StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("img_publicaciones/cabra.png");





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



    //Se enlanza controladores del layout con los del adapter
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       TextView txtFecha, txtDescripcion, txtId_pub, txt_fav, txtNombreUserV;

        TextView txtTituloMP;
        ImageView imgPortada;
        TextView txtPrecio;

       // ImageView imgPortada, imgFav, ProfileImage, imgDefault;
      //  Button btnMas;
        Context contextoMy;



        public MyViewHolder(View itemView){
            super(itemView);

            contextoMy = itemView.getContext();

            imgPortada = itemView.findViewById(R.id.imgPortadaMP);
          txtFecha = itemView.findViewById(R.id.txtFechaPM);
           txtDescripcion = itemView.findViewById(R.id.txtDescripcionMP);
           // imgPortada = itemView.findViewById(R.id.imgPortada);
           // txtId_pub = itemView.findViewById(R.id.idPublicacion);
           // imgFav = itemView.findViewById(R.id.imgFav);
          //  btnMas = itemView.findViewById(R.id.btnVerMas);
          //  txt_fav = itemView.findViewById(R.id.id_fav);
          //  txtNombreUserV = itemView.findViewById(R.id.txtNombreUser);
           // ProfileImage = itemView.findViewById(R.id.profile_image);
           // imgDefault = itemView.findViewById(R.id.imgPerfil);

            txtTituloMP = itemView.findViewById(R.id.txtTituloMP);
            txtPrecio = itemView.findViewById(R.id.txtPrecioMP);
        }

        void setOnClickListeners(){
           // btnMas.setOnClickListener(this);
            //imgFav.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.btnVerMas:
                   // Intent intent  = new Intent(contextoMy, ver_publicacion.class);
                    //intent.putExtra("publicacionCod", txtId_pub.getText());
                   // intent.putExtra("vendedor", txtNombreUserV.getText());
                    //contextoMy.startActivities(new Intent[]{intent});
                    break;
                case R.id.imgFav:

                    break;

            }
        }
    }


}