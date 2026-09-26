package com.example.proyectoandroid_chatfirebase.ui.auth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoandroid_chatfirebase.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}