package com.example.compraapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    private TextView tvListTitle;
    private EditText etProduct;
    private Button btnAddProduct, btnSaveList, btnClearList;
    private ListView listViewProducts;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> productList;

    private String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean modoEdicion = getIntent().getBooleanExtra("modoEdicion", false);
        listName = getIntent().getStringExtra("listName");


        if (modoEdicion) {
            cargarProductosExistentes(listName);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        tvListTitle = findViewById(R.id.tvListTitle);
        etProduct = findViewById(R.id.etProduct);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnSaveList = findViewById(R.id.btnSaveList);
        btnClearList = findViewById(R.id.btnClearList);
        listViewProducts = findViewById(R.id.listViewProducts);

        listName = getIntent().getStringExtra("listName");
        tvListTitle.setText("Lista: " + listName);

        productList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        listViewProducts.setAdapter(adapter);

        btnAddProduct.setOnClickListener(v -> {
            String product = etProduct.getText().toString().trim();
            if (!TextUtils.isEmpty(product)) {
                productList.add(product);
                adapter.notifyDataSetChanged();
                etProduct.setText("");
            }
        });

        btnSaveList.setOnClickListener(v -> {
            if (productList.isEmpty()) {
                Toast.makeText(this, "No hay productos para guardar", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("listas")
                    .child(uid)
                    .child(listName)
                    .setValue(productList)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Lista guardada correctamente", Toast.LENGTH_SHORT).show();
                        // Redirige a MenuActivity
                        Intent intent = new Intent(ProductsActivity.this, MenuActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // Finaliza la actividad actual para evitar volver atrás
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
        });


        btnClearList.setOnClickListener(v -> {
            productList.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Lista vaciada", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarProductosExistentes(String listName) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("listas")
                .child(uid)
                .child(listName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productoSnap : snapshot.getChildren()) {
                    String producto = productoSnap.getValue(String.class);
                    productList.add(producto);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductsActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}