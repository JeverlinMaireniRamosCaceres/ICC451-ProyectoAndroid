package com.example.proyectoandroid_chatfirebase.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoandroid_chatfirebase.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}