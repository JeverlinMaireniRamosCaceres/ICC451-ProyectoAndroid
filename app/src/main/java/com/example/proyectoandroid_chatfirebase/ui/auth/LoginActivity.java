package com.example.proyectoandroid_chatfirebase.ui.auth;

import android.os.Bundle;
import android.content.Intent;
import android.util.Patterns;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoandroid_chatfirebase.databinding.ActivityLoginBinding;
import com.example.proyectoandroid_chatfirebase.ui.users.UsuariosActivity;
import com.example.proyectoandroid_chatfirebase.viewmodel.AuthViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        if (authViewModel.isUserLoggedIn()) {
            goToUsers();
            return;
        }

        observeViewModel();

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

    private void observeViewModel() {
        authViewModel.getLoading().observe(this, isLoading -> {
            binding.btnSignIn.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
            binding.pgbLogin.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        });

        authViewModel.getAuthSuccess().observe(this, success -> {
            if (success) {
                goToUsers();
            }
        });

        authViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                binding.lblError.setText(message);
                binding.lblError.setVisibility(View.VISIBLE);
                authViewModel.clearError();
            }
        });
    }
    private void validationAndLogin() {
        binding.lblError.setVisibility(View.GONE);
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
            authViewModel.login(email, password);
        }
    }

    private void goToUsers() {
        Intent intent = new Intent(LoginActivity.this, UsuariosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}