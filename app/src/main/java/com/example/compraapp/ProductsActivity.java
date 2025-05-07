package com.example.compraapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "Lista guardada correctamente", Toast.LENGTH_SHORT).show()
                    )
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
}