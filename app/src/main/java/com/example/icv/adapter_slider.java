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
import com.squareup.picasso.Picasso;

public class adapter_slider extends RecyclerView.Adapter<adapter_slider.MyViewHolder> {
    // private String[] mDataset;
    String listImgs[];
    private Context mCtx;
    FirebaseFirestore db;
    String usuario_view;

    //Prueba click

    private adapter_slider.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(adapter_slider.OnItemClickListener listener){
        mListener = listener;
    }
    // AdaptadorMisFavoritos

    public adapter_slider(Context myCtx,String ImagenesLista[]) {
        listImgs = ImagenesLista;
        mCtx = myCtx;
        db = FirebaseFirestore.getInstance();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public adapter_slider.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view\\

        LayoutInflater hola;
        hola =  LayoutInflater.from(mCtx);
        View view  = hola.inflate(R.layout.slider_imagenes, null);
        //MyViewHolder vh = new MyViewHolder(v);


        return new adapter_slider.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    //Se envia la informacion que se quiere mostrar a los elementos del layout
    @Override
    public void onBindViewHolder(final adapter_slider.MyViewHolder holder, int position) {

        String imgUrl = listImgs[position];

       Glide.with(mCtx).load(imgUrl).into(holder.imgSlider);
        holder.imgSlider.setTag(imgUrl);
       Toast.makeText(mCtx, imgUrl, Toast.LENGTH_LONG).show();

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

            imgSlider = itemView.findViewById(R.id.imageSlider);


        }

        void setOnClickListeners(){
            imgSlider.setOnClickListener(this);
        }



        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.imageSlider:
                    Intent intent  = new Intent(contextoMy, ver_imagenes.class);
                    intent.putExtra("imgUrl", imgSlider.getTag().toString());
                    contextoMy.startActivities(new Intent[]{intent});
                    break;

            }
        }
    }


}
