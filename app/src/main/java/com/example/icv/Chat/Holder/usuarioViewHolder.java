package com.example.icv.Chat.Holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.icv.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class usuarioViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView img;
    private TextView txtnombre;
    private LinearLayout linear;

    public usuarioViewHolder(@NonNull View itemView) {
        super(itemView);
        img=itemView.findViewById(R.id.foto);
        txtnombre=itemView.findViewById(R.id.txtnombre);
        linear=itemView.findViewById(R.id.layoutprincipal);
    }

    public CircleImageView getImg() {
        return img;
    }

    public void setImg(CircleImageView img) {
        this.img = img;
    }

    public TextView getTxtnombre() {
        return txtnombre;
    }

    public void setTxtnombre(TextView txtnombre) {
        this.txtnombre = txtnombre;
    }

    public LinearLayout getLinear() {
        return linear;
    }

    public void setLinear(LinearLayout linear) {
        this.linear = linear;
    }
}