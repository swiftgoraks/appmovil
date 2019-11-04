package com.example.icv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class adapterImageFull extends RecyclerView.Adapter<adapterImageFull.MyViewHolder> {
    // private String[] mDataset;
    String listImgs[];
    private Context mCtx;
    //FirebaseFirestore db;
    //String id_publicacion;

    //Prueba click

    private adapterImageFull.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(adapterImageFull.OnItemClickListener listener){
        mListener = listener;
    }
    // AdaptadorMisFavoritos

    public adapterImageFull(Context myCtx,String ImagenesLista[]) {
        listImgs = ImagenesLista;
        mCtx = myCtx;
        //db = FirebaseFirestore.getInstance();
       // id_publicacion = id_publicacion1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public adapterImageFull.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view\\

        LayoutInflater hola;
        hola =  LayoutInflater.from(mCtx);
        View view  = hola.inflate(R.layout.ver_imagen_full, null);
        //MyViewHolder vh = new MyViewHolder(v);


        return new adapterImageFull.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //Se envia la informacion que se quiere mostrar a los elementos del layout
    @Override
    public void onBindViewHolder(final adapterImageFull.MyViewHolder holder, int position) {

        String imgUrl = listImgs[position];

        Glide.with(mCtx).load(imgUrl).into(holder.imgSlider);
        holder.imgSlider.setTag(imgUrl);
        // Toast.makeText(mCtx, imgUrl, Toast.LENGTH_LONG).show();

        //Picasso.get().load(imgUrl).resizeDimen(400, 200).into(holder.imgSlider);

        holder.setOnClickListeners();


    }

    @Override
    public int getItemCount() {

        return listImgs.length;
    }



    //Se enlanza controladores del layout con los del adapter
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgSlider;
        Context contextoMy;




        public MyViewHolder(View itemView){
            super(itemView);



            contextoMy = itemView.getContext();


            imgSlider = itemView.findViewById(R.id.imageSliderFull);


        }

        void setOnClickListeners(){
            //imgSlider.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.imageSlider:

                    break;

            }
        }
    }


}
