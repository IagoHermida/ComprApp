package com.example.compraapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    private Button btnNuevaLista, btnHistorial, btnPerfil, btnCerrarSesion;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnNuevaLista = findViewById(R.id.btnNuevaLista);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        mAuth = FirebaseAuth.getInstance();

        btnNuevaLista.setOnClickListener(v -> {
            // Intent para NewListActivity
            startActivity(new Intent(this, NewListActivity.class));
        });

        btnHistorial.setOnClickListener(v -> {
            // Intent para HistoryListsActivity
            startActivity(new Intent(this, HistoryListsActivity.class));
        });

        btnPerfil.setOnClickListener(v -> {
            // Intent para ProfileActivity
            startActivity(new Intent(this, ProfileActivity.class));
        });

        btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MenuActivity.this, MainActivity.class));
            finish();
        });
    }
}