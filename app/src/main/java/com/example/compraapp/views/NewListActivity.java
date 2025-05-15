package com.example.compraapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.compraapp.R;

public class NewListActivity extends AppCompatActivity {

    private EditText etListName;
    private Button btnConfirmListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);

        etListName = findViewById(R.id.etListName);
        btnConfirmListName = findViewById(R.id.btnConfirmListName);

        btnConfirmListName.setOnClickListener(v -> {
            String listName = etListName.getText().toString().trim();

            if (TextUtils.isEmpty(listName)) {
                Toast.makeText(this, "Por favor, ingresa un nombre para la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ProductsActivity.class);
            intent.putExtra("listName", listName);
            startActivity(intent);
        });
    }
}