package com.example.proyectoandroid_chatfirebase.ui.users;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid_chatfirebase.R;
import com.example.proyectoandroid_chatfirebase.adapter.UserAdapter;
import com.example.proyectoandroid_chatfirebase.data.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    private RecyclerView rvChats;
    private UserAdapter userAdapter;
    private List<User> listaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvChats = findViewById(R.id.rvChats);

        listaUsuarios = crearDatosDePrueba();

        userAdapter = new UserAdapter(listaUsuarios);
        rvChats.setAdapter(userAdapter);
        rvChats.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<User> crearDatosDePrueba() {
        List<User> lista = new ArrayList<>();

        User u1 = new User("uid1", "María", "maria@correo.com");
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