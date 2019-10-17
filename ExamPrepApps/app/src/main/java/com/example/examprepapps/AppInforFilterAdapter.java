package com.example.examprepapps;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppInforFilterAdapter extends RecyclerView.Adapter<AppInforFilterAdapter.viewHolder> {
    ArrayList<Appinfo> mdata;

    public AppInforFilterAdapter(ArrayList<Appinfo> mdata) {
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public AppInforFilterAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_item, parent, false);
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull AppInforFilterAdapter.viewHolder holder, final int position) {


        Appinfo appinfodetails = mdata.get(position);
        holder.tv_name.setText(appinfodetails.name);
        holder.tv_price.setText(appinfodetails.price);
        Picasso.get().load(appinfodetails.images).into(holder.iv_main);
        String dolar = appinfodetails.price;
        Log.i("demo", "price " + dolar);
        dolar = dolar.replaceAll("[$]", "");
        Log.i("demo", "price " + dolar);
        Float price = Float.parseFloat(dolar);
        Log.i("demo", "price " + price);
        holder.appinfo = appinfodetails;

        if (price >= 0 && price <= 1.99) {
            holder.iv_price.setImageResource(R.drawable.price_low);
        } else if (price >= 2.0 && price <= 5.99) {
            holder.iv_price.setImageResource(R.drawable.price_medium);
        } else {
            holder.iv_price.setImageResource(R.drawable.price_high);
        }


    }


    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_price;
        ImageView iv_price, iv_main;
        ImageButton ib_delete;
        Appinfo appinfo;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            iv_main = itemView.findViewById(R.id.iv_main);
            iv_price = itemView.findViewById(R.id.iv_price);
            ib_delete = itemView.findViewById(R.id.ib_delete);


            ib_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.appList.add(appinfo);
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.selectedList.remove(appinfo);
                    MainActivity.mAdapter.notifyDataSetChanged();

                }
            });


        }
    }
}