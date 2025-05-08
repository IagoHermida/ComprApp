package com.example.compraapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.compraapp.R;

import java.util.ArrayList;

public class ProductCheckAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> products;
    private final SharedPreferences sharedPreferences;

    // Constructor del adaptador
    public ProductCheckAdapter(Context context, ArrayList<String> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
        this.sharedPreferences = context.getSharedPreferences("ProductPreferences", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_product_check, parent, false);
        }

        CheckBox checkBox = view.findViewById(R.id.checkBoxItem);
        String product = products.get(position);
        checkBox.setText(product);

        // Cargar el estado guardado para este producto
        boolean isChecked = sharedPreferences.getBoolean("product_" + position, false);
        checkBox.setChecked(isChecked);

        // Eliminar el listener antes de actualizar el estado
        checkBox.setOnCheckedChangeListener(null);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            // Guardar el estado cuando el usuario cambia el estado
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("product_" + position, isChecked1);  // Guardar el nuevo estado
            editor.apply();
        });

        return view;
    }
}
