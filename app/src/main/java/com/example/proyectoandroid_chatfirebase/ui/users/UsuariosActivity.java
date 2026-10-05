package com.example.proyectoandroid_chatfirebase.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;
import com.example.proyectoandroid_chatfirebase.viewmodel.UserViewModel;

import com.example.proyectoandroid_chatfirebase.R;
import com.example.proyectoandroid_chatfirebase.adapter.UserAdapter;
import com.example.proyectoandroid_chatfirebase.data.model.User;
import com.example.proyectoandroid_chatfirebase.ui.auth.LoginActivity;
import com.example.proyectoandroid_chatfirebase.ui.chat.ChatActivity;
import com.example.proyectoandroid_chatfirebase.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import androidx.appcompat.widget.SearchView;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView rvChats;
    private UserAdapter userAdapter;
    private List<User> listaUsuarios;
    private AuthViewModel authViewModel;
    private UserViewModel usersViewModel;
    private ActivityResultLauncher<String> permisoNots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        rvChats = findViewById(R.id.rvChats);

        listaUsuarios = new ArrayList<>();

        userAdapter = new UserAdapter(listaUsuarios, usuario -> {
            Intent intent = new Intent(UsuariosActivity.this, ChatActivity.class);
            intent.putExtra("uid", usuario.getUid());
            intent.putExtra("nombre", usuario.getNombre());
            startActivity(intent);
        });

        rvChats.setAdapter(userAdapter);
        rvChats.setLayoutManager(new LinearLayoutManager(this));

        usersViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        usersViewModel.getUsuarios().observe(this, usuarios -> userAdapter.actualizarLista(usuarios));
        usersViewModel.getError().observe(this, mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show());

        usersViewModel.getMiNombre().observe(this, nombre -> {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Chats de " + nombre);
            }
        });

        String miUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersViewModel.cargarUsuarios(miUid);

        usersViewModel.guardarTokenActual();

        permisoNots = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                concedido -> Log.d("FCM", "Permiso de notificaciones: " + concedido));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            permisoNots.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuarios, menu);

        MenuItem itemBuscar = menu.findItem(R.id.action_buscar);
        SearchView searchView = (SearchView) itemBuscar.getActionView();
        searchView.setQueryHint(getString(R.string.hint_buscar_usuario));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.filtrar(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.filtrar(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            usersViewModel.cerrarSesionLimpiando(() -> {
                authViewModel.logout();
                Intent intent = new Intent(UsuariosActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}