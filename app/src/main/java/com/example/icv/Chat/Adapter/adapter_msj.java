package com.example.icv.Chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.icv.Chat.Holder.holder_msj;
import com.example.icv.Chat.Persistencia.UsuarioDAO;
import com.example.icv.Chat.entidades.Logica.LMensaje;
import com.example.icv.Chat.entidades.Logica.LUsuario;
import com.example.icv.R;

import java.util.ArrayList;
import java.util.List;

public class adapter_msj extends RecyclerView.Adapter<holder_msj> {

    private List<LMensaje> listaMensaje= new ArrayList<>();
    private Context cont;

    public adapter_msj( Context cont) {
        this.cont = cont;
    }

    public int agregarMsj(LMensaje lmensaje)
    {
        listaMensaje.add(lmensaje);
        int posicion=listaMensaje.size()-1;
        notifyItemInserted(listaMensaje.size());
        return  posicion;
    }

    @NonNull
    @Override
    public holder_msj onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==1)
        {
            v= LayoutInflater.from(cont).inflate(R.layout.card_view_msj_emisor,parent,false);
        }
        else
        {
            v= LayoutInflater.from(cont).inflate(R.layout.card_view_msj,parent,false);
        }

        return new holder_msj(v);
    }

    public void actualizarMsj(int posicion,LMensaje lMensaje)
    {
        listaMensaje.set(posicion,lMensaje);
        notifyItemChanged(posicion);
    }

    @Override
    public void onBindViewHolder(@NonNull holder_msj holder, int position) {

        LMensaje lMensaje=listaMensaje.get(position);
        LUsuario lUsuario=lMensaje.getLusuario();

        if(lUsuario!=null)
        {
            holder.getTxtUser().setText(lUsuario.getUsuario().getNombre());
            if(!lUsuario.getUsuario().getFotoPerfil().isEmpty())
            {
                Glide.with(cont).load(lUsuario.getUsuario().getFotoPerfil()).into(holder.getImgUser());
            }

        }

        holder.getTxtMsj().setText(lMensaje.getMensaje().getMensaje());

        if(lMensaje.getMensaje().getContieneFoto())
        {
            holder.getFotomsj().setVisibility(View.VISIBLE);
            holder.getTxtMsj().setVisibility(View.VISIBLE);
            Glide.with(cont).load(lMensaje.getMensaje().getUrlfoto()).into(holder.getFotomsj());
        }
        else
        {
            holder.getTxtMsj().setVisibility(View.VISIBLE);
            holder.getFotomsj().setVisibility(View.GONE);
        }
        holder.getTxtHora().setText(lMensaje.FechaMensaje());
    }

    @Override
    public int getItemCount() {
        return listaMensaje.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(listaMensaje.get(position).getLusuario()!=null)
        {
            if(listaMensaje.get(position).getLusuario().getKey().equals(UsuarioDAO.getInstance().getKeyUsuario()))
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }


        // return super.getItemViewType(position);
    }
}

