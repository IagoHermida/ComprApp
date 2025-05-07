package com.example.compraapp;


import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ViewListActivity extends AppCompatActivity {

    private TextView tvListTitle;
    private ListView listViewItems;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        tvListTitle = findViewById(R.id.tvListTitle);
        listViewItems = findViewById(R.id.listViewItems);
        products = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
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