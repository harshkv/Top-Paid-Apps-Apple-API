package com.example.examprepapps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AppInfoAdapter extends ArrayAdapter<Appinfo> {

    public AppInfoAdapter(@NonNull Context context, int resource, @NonNull List<Appinfo> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Appinfo appInfObject = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.each_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.iv_main = (ImageView) convertView.findViewById(R.id.iv_main);
            viewHolder.iv_price = (ImageView) convertView.findViewById(R.id.iv_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(appInfObject.name);
        viewHolder.tv_price.setText(appInfObject.price);
        Picasso.get().load(appInfObject.images).into(viewHolder.iv_main);
        String dolar = appInfObject.price;
        dolar = dolar.replaceAll("[$]", "");
        Float price = Float.parseFloat(dolar);

        if (price >= 0 && price <= 1.99) {
            viewHolder.iv_price.setImageResource(R.drawable.price_low);
        } else if (price >= 2.0 && price <= 5.99) {
            viewHolder.iv_price.setImageResource(R.drawable.price_medium);
        } else {
            viewHolder.iv_price.setImageResource(R.drawable.price_high);
        }

        return convertView;
    }


    private static class ViewHolder {
        TextView tv_name;
        TextView tv_price;
        ImageView iv_main, iv_price;

    }
}


