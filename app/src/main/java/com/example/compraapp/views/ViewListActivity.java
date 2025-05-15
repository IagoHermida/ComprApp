package com.example.compraapp.views;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.compraapp.R;
import com.example.compraapp.adapters.ProductCheckAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ViewListActivity extends AppCompatActivity {

    private TextView tvListTitle;
    private ListView listViewItems;
    //private ArrayAdapter<String> adapter;
    private ArrayList<String> products;
    private ProductCheckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.compraapp.R.layout.activity_view_list);

        tvListTitle = findViewById(com.example.compraapp.R.id.tvListTitle);
        listViewItems = findViewById(R.id.listViewItems);
        products = new ArrayList<>();
        adapter = new ProductCheckAdapter(this, products);
        listViewItems.setAdapter(adapter);

        String listName = getIntent().getStringExtra("listName");
        tvListTitle.setText("Lista: " + listName);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference listRef = FirebaseDatabase.getInstance()
                .getReference("listas")
                .child(uid)
                .child(listName);

        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();
                for (DataSnapshot productSnap : snapshot.getChildren()) {
                    products.add(productSnap.getValue(String.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewListActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}