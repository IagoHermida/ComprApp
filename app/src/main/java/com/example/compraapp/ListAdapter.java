package com.example.compraapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final List<String> listaNombres;
    private final Context context;

    public ListAdapter(Context context, List<String> listaNombres) {
        this.context = context;
        this.listaNombres = listaNombres;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String listName = listaNombres.get(position);
        holder.tvListName.setText(listName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewListActivity.class);
            intent.putExtra("listName", listName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaNombres.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvListName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.tvListName);
        }
    }
}