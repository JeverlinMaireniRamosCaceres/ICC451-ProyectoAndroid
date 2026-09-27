package com.example.proyectoandroid_chatfirebase.ui.auth;

import android.os.Bundle;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoandroid_chatfirebase.databinding.ActivityLoginBinding;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                validationAndLogin();
            }
        });

        binding.lblCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });
    }
    private void validationAndLogin() {
        AtomicBoolean isValid = new AtomicBoolean(true);

        String email = binding.txtEmail.getText().toString().trim();
        String password = binding.txtPassword.getText().toString();

        if (password.isEmpty()) {
            binding.txtPassword.setError("Ingresa tu contraseña");
            binding.txtPassword.requestFocus();
            isValid.set(false);
        }

        if (email.isEmpty()) {
            binding.txtEmail.setError("Ingresa tu correo");
            binding.txtEmail.requestFocus();
            isValid.set(false);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtEmail.setError("El correo no tiene un formato válido");
            binding.txtEmail.requestFocus();
            isValid.set(false);
        }

        if (isValid.get()) {
            // Aquí se conectará con Firebase en el siguiente paso
        }
    }
}