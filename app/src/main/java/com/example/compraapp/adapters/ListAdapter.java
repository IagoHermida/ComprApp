package com.example.compraapp.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.compraapp.views.ProductsActivity;
import com.example.compraapp.R;
import com.example.compraapp.views.ViewListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // Click normal: ver lista
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewListActivity.class);
            intent.putExtra("listName", listName);
            context.startActivity(intent);
        });

        // Click prolongado: mostrar opciones
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Opciones para \"" + listName + "\"")
                    .setItems(new CharSequence[]{"Eliminar lista", "Añadir productos"}, (dialog, which) -> {
                        if (which == 0) {
                            eliminarLista(listName);
                        } else if (which == 1) {
                            añadirProductos(listName);
                        }
                    })
                    .show();
            return true;
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

    private void eliminarLista(String listName) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("listas")
                .child(uid)
                .child(listName);

        ref.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Lista eliminada", Toast.LENGTH_SHORT).show();
                    listaNombres.remove(listName);
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                );
    }

    private void añadirProductos(String listName) {
        Intent intent = new Intent(context, ProductsActivity.class);
        intent.putExtra("listName", listName);
        intent.putExtra("modoEdicion", true); // puedes usar esto para diferenciar si es una lista nueva o edición
        context.startActivity(intent);
    }

}