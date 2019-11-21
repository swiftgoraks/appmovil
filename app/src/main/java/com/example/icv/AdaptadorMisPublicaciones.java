
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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.icv.publicacion.EditarPublicacion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.Locale;

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

        NumberFormat nf = NumberFormat.getInstance();
        nf = NumberFormat.getInstance(Locale.ENGLISH);

        Double precio = anun.getPrecio();

            holder.txtPrecio.setText(" $ " + nf.format(precio));
            holder.txtDescripcion.setText(anun.getDescripcion());
            holder.txtId_pub.setText(anun.getId_anuncio());

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
        Button btEdit, btnVer, btnEliminar;

       // ImageView imgPortada, imgFav, ProfileImage, imgDefault;
      //  Button btnMas;
        Context contextoMy;



        public MyViewHolder(View itemView){
            super(itemView);
            contextoMy = itemView.getContext();
            imgPortada = itemView.findViewById(R.id.imgPortadaMP);
            txtFecha = itemView.findViewById(R.id.txtFechaPM);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcionMP);
            txtTituloMP = itemView.findViewById(R.id.txtTituloMP);
            txtPrecio = itemView.findViewById(R.id.txtPrecioMP);
            txtId_pub=itemView.findViewById(R.id.idPublicacion);
            btEdit=itemView.findViewById(R.id.editarMP);
            btnVer = itemView.findViewById(R.id.verMP);
            btnEliminar = itemView.findViewById(R.id.eliminarMP);
        }

        void setOnClickListeners(){
            btEdit.setOnClickListener(this);
            btnVer.setOnClickListener(this);
            btnEliminar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.editarMP:

                    Intent intent  = new Intent(contextoMy, EditarPublicacion.class);
                    intent.putExtra("publicacionCod", txtId_pub.getText());
                    contextoMy.startActivities(new Intent[]{intent});
                    break;
                case R.id.verMP:
                    Intent intent2  = new Intent(contextoMy, ver_mi_publicacion.class);
                    intent2.putExtra("publicacionCod", txtId_pub.getText());
                    contextoMy.startActivities(new Intent[]{intent2});
                    break;
                case R.id.eliminarMP:
                    //Intent intent3  = new Intent(contextoMy, ver_mi_publicacion.class);
                    //intent3.putExtra("publicacionCod", txtId_pub.getText());
                    //contextoMy.startActivities(new Intent[]{intent3});

                    ///Eliminar Favoritos///

                    /*db.collection("usuario_fav").whereEqualTo("id_anuncio",txtId_pub.getText()).get().addOnCompleteListener(new OnCompleteListener <QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task <QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                int contador = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    db.collection("usuario_fav").document(document.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(contextoMy, "Eliminado Favoritos", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }


                            } else {
                                Log.d("Mensaje: ", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                     */

                    //***///


                    ///Eliminar Publicacion///

                    db.collection("publicacion").document(txtId_pub.getText().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //startActivity(new Intent(Resgistrarse.this, home.class));
                            Anuncio list_temp[] = new Anuncio[anunciosLista.length - 1];

                            for(int i =0 ; i <=list_temp.length -1; i++)
                            {
                                if(!anunciosLista[i].equals(String.valueOf(txtId_pub.getText()))){
                                    list_temp[i] = anunciosLista[i];
                                }
                            }

                            anunciosLista = list_temp;

                            notifyItemRemoved(getAdapterPosition());
                            Toast.makeText(mCtx,  "La publicación se ha eliminado.", Toast.LENGTH_LONG).show();
                        }
                    });
                    ///***////
                    break;

            }
        }
    }


}