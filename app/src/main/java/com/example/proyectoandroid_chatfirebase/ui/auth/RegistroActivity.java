package com.example.proyectoandroid_chatfirebase.ui.auth;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectoandroid_chatfirebase.ui.users.UsuariosActivity;
import com.example.proyectoandroid_chatfirebase.viewmodel.AuthViewModel;
import com.example.proyectoandroid_chatfirebase.databinding.ActivityRegistroBinding;

import java.util.concurrent.atomic.AtomicBoolean;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        observeViewModel();


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationAndRegister();
            }
        });

        binding.lblSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void observeViewModel() {
        authViewModel.getLoading().observe(this, isLoading -> {
            binding.btnSignUp.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
            binding.pgbRegister.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        });

        authViewModel.getAuthSuccess().observe(this, success -> {
            if (success) {
                authViewModel.logout();
                finish();
            }
        });

        authViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(RegistroActivity.this, message, Toast.LENGTH_LONG).show();
                authViewModel.clearError();
            }
        });
    }

    private void validationAndRegister() {
        AtomicBoolean isValid = new AtomicBoolean(true);

        String name = binding.txtName.getText().toString().trim();
        String email = binding.txtEmail.getText().toString().trim();
        String password = binding.txtPassword.getText().toString();
        String confirmPassword = binding.txtConfirmPassword.getText().toString();

        if (confirmPassword.isEmpty()) {
            binding.txtConfirmPassword.setError("Confirma tu contraseña");
            binding.txtConfirmPassword.requestFocus();
            isValid.set(false);
        } else if (!confirmPassword.equals(password)) {
            binding.txtConfirmPassword.setError("Las contraseñas no coinciden");
            binding.txtConfirmPassword.requestFocus();
            isValid.set(false);
        }

        if (password.isEmpty()) {
            binding.txtPassword.setError("Ingresa una contraseña");
            binding.txtPassword.requestFocus();
            isValid.set(false);
        } else if (password.length() < 6) {
            binding.txtPassword.setError("La contraseña debe tener al menos 6 caracteres");
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

        if (name.isEmpty()) {
            binding.txtName.setError("Ingresa tu nombre");
            binding.txtName.requestFocus();
            isValid.set(false);
        }

        if (isValid.get()) {
            authViewModel.register(name, email, password);
        }
    }
}