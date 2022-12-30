package com.example.aplikasitokolaptop.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aplikasitokolaptop.DetailActivity;
import com.example.aplikasitokolaptop.R;
import com.example.aplikasitokolaptop.model.Laptop;

import java.util.List;

public class LaptopAdapter extends RecyclerView.Adapter<LaptopAdapter.MyViewHolder>{
    private Context context;
    private List<Laptop> list;
    private Dialog dialog;

    public interface Dialog{
        void onLongClick(int pos);

        void onItemClick(int pos);
    }

//    public Dialog getDialog() {
//        return dialog;
//    }

    public void setDialog(Dialog dialog){
        this.dialog = dialog;
    }

    public LaptopAdapter(Context context, List<Laptop> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_laptop, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama.setText(list.get(position).getNama());
        holder.harga.setText(list.get(position).getHarga());
        Glide.with(context).load(list.get(position).getAvatar()).into(holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("avatar", list.get(holder.getAdapterPosition()).getAvatar());
                intent.putExtra("nama", list.get(holder.getAdapterPosition()).getNama());
                intent.putExtra("harga", list.get(holder.getAdapterPosition()).getHarga());
                intent.putExtra("spesifikasi", list.get(holder.getAdapterPosition()).getSpesifikasi());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nama, harga;
        ImageView avatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txtNama);
            harga = itemView.findViewById(R.id.txtHarga);
            avatar = itemView.findViewById(R.id.avatar);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (dialog != null) {
                        dialog.onLongClick(getLayoutPosition());
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.onItemClick(getLayoutPosition());
                }
            });
        }
    }
}
