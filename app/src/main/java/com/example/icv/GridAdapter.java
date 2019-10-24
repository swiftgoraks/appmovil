package com.example.icv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class GridAdapter extends BaseAdapter {
    Context mContext;
    private final String[] nom_imgs;
    private final String[] img_url;
    View view;
    LayoutInflater layoutInflater;

    public GridAdapter(Context mContext, String[] nom_imgs, String[] img_url) {
        this.mContext = mContext;
        this.nom_imgs = nom_imgs;
        this.img_url = img_url;
    }

    @Override
    public int getCount() {
        return nom_imgs.length;
    }

    @Override
    public Object getItem(int i) {
        return nom_imgs[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            view = new View(mContext);
            view = layoutInflater.inflate(R.layout.single_layout, null);

            ImageView imageCatalogo = view.findViewById(R.id.imgCat);
            TextView txtCatName = view.findViewById(R.id.txtCagName);

            // imageCatalogo.setImageResource(img_url[i]);

            Glide.with(mContext).load(img_url[i]).into(imageCatalogo);
            txtCatName.setText(nom_imgs[i]);


        }

return  view;
}
}
