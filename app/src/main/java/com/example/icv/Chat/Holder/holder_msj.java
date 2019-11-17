package com.example.icv.Chat.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icv.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class holder_msj extends RecyclerView.ViewHolder {

    private TextView txtMsj,txtHora,txtUser;
    private CircleImageView imgUser;
    private ImageView fotomsj;

    public holder_msj(@NonNull View itemView) {
        super(itemView);
        this.txtUser=itemView.findViewById(R.id.txtUser);
        this.txtHora=itemView.findViewById(R.id.txtHora);
        this.txtMsj=itemView.findViewById(R.id.txtMsj);
        this.imgUser=itemView.findViewById(R.id.imgUser);
        this.fotomsj= itemView.findViewById(R.id.fotomsj);
    }

    public TextView getTxtMsj() {
        return txtMsj;
    }

    public void setTxtMsj(TextView txtMsj) {
        this.txtMsj = txtMsj;
    }

    public TextView getTxtHora() {
        return txtHora;
    }

    public void setTxtHora(TextView txtHora) {
        this.txtHora = txtHora;
    }

    public TextView getTxtUser() {
        return txtUser;
    }

    public void setTxtUser(TextView txtUser) {
        this.txtUser = txtUser;
    }

    public CircleImageView getImgUser() {
        return imgUser;
    }

    public void setImgUser(CircleImageView imgUser) {
        this.imgUser = imgUser;
    }

    public ImageView getFotomsj() {
        return fotomsj;
    }

    public void setFotomsj(ImageView fotomsj) {
        this.fotomsj = fotomsj;
    }
}
