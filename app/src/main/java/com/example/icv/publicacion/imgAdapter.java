package com.example.icv.publicacion;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.icv.R;
import com.squareup.picasso.Picasso;

public class imgAdapter extends RecyclerView.Adapter<imgAdapter.imgHolder>{

    private Context mContext;
    imgUpload Lista[];
    private OnClick mListener;

    public imgAdapter(Context context, imgUpload ListaImg[]){
        mContext=context;
        Lista =  ListaImg;
    }

    @NonNull
    @Override
    public imgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.img_publicacion_edit,parent,false);
        return new imgHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imgHolder holder, int position) {
        imgUpload current=Lista[position];
        //Picasso.get().load(current.getImgUrl()).fit().centerCrop().into(holder.imgview);
        Glide.with(mContext).load(current.getImgUrl()).into(holder.imgview);
    }

    @Override
    public int getItemCount() {
            return Lista.length;
    }

    public class imgHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public ImageView imgview;
        public TextView txt;
        public imgHolder(@NonNull View itemView) {
            super(itemView);
            imgview=itemView.findViewById(R.id.img_upload);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null)
            {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION)
                {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Seleccione una opcion");
            MenuItem delete=contextMenu.add(Menu.NONE,1,2,"Eliminar");
            MenuItem agregar=contextMenu.add(Menu.NONE,2,1,"Agregar imagenes");
            delete.setOnMenuItemClickListener(this);
            agregar.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(mListener!=null)
            {
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION)
                {
                   switch (item.getItemId())
                   {
                       case 1:
                           mListener.onDeleteClick(position);
                           return true;
                       case 2:
                           mListener.onAgregarClick(position);
                           return true;
                   }
                }
            }
            return false;
        }
    }

    public interface OnClick
    {
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onAgregarClick(int position);
    }

    public void setOnClickListener(OnClick listener)
    {
        mListener=listener;
    }
}
