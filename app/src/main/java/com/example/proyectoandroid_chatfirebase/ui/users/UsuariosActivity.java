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

import com.example.proyectoandroid_chatfirebase.R;
import com.example.proyectoandroid_chatfirebase.adapter.UserAdapter;
import com.example.proyectoandroid_chatfirebase.data.model.User;
import com.example.proyectoandroid_chatfirebase.ui.auth.LoginActivity;
import com.example.proyectoandroid_chatfirebase.ui.chat.ChatActivity;
import com.example.proyectoandroid_chatfirebase.viewmodel.AuthViewModel;

import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView rvChats;
    private UserAdapter userAdapter;
    private List<User> listaUsuarios;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        rvChats = findViewById(R.id.rvChats);

        listaUsuarios = crearDatosDePrueba();

        userAdapter = new UserAdapter(listaUsuarios, usuario -> {
            Intent intent = new Intent(UsuariosActivity.this, ChatActivity.class);
            intent.putExtra("uid", usuario.getUid());
            intent.putExtra("nombre", usuario.getNombre());
            startActivity(intent);
        });

        rvChats.setAdapter(userAdapter);
        rvChats.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuarios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            authViewModel.logout();
            Intent intent = new Intent(UsuariosActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<User> crearDatosDePrueba() {
        List<User> lista = new ArrayList<>();

        User u1 = new User("onxHXz15kjXitQ9cjpBMuad58hU2", "María (prueba real)", "a@email.com");
        u1.setUltimoMensaje("Hola, ¿cómo estás?");
        u1.setHoraUltimoMensaje("10:32");
        lista.add(u1);

        User u2 = new User("uid2", "Carlos", "carlos@correo.com");
        u2.setUltimoMensaje("Nos vemos mañana");
        u2.setHoraUltimoMensaje("09:15");
        lista.add(u2);

        User u3 = new User("uid3", "Ana", "ana@correo.com");
        u3.setUltimoMensaje("Perfecto");
        u3.setHoraUltimoMensaje("Ayer");
        lista.add(u3);

        return lista;
    }
}