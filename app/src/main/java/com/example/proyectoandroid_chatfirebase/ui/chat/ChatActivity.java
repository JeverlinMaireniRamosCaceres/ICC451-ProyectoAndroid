package com.example.proyectoandroid_chatfirebase.ui.chat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid_chatfirebase.R;
import com.example.proyectoandroid_chatfirebase.adapter.MessageAdapter;
import com.example.proyectoandroid_chatfirebase.data.model.Message;
import com.example.proyectoandroid_chatfirebase.viewmodel.ChatViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMensajes;
    private MessageAdapter messageAdapter;
    private List<Message> listaMensajes;
    private String uidOtroUs;
    private String nombreOtroUs;
    private ChatViewModel chatViewModel;

    private static final String UID_USUARIO_ACTUAL = "uhshYjr8izUZhktDJTKACXXlmPj2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);

        uidOtroUs = getIntent().getStringExtra("uid");
        nombreOtroUs = getIntent().getStringExtra("nombre");

        if (nombreOtroUs != null) {
            toolbar.setTitle(nombreOtroUs);
        }

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        chatViewModel.iniciarChat(UID_USUARIO_ACTUAL, uidOtroUs);

        rvMensajes = findViewById(R.id.rvMensajes);
        EditText etMensaje = findViewById(R.id.etMensaje);
        ImageButton btnEnviar = findViewById(R.id.btnEnviar);

        /*listaMensajes = crearDatosDePrueba();

        messageAdapter = new MessageAdapter(listaMensajes, UID_USUARIO_ACTUAL);
        rvMensajes.setAdapter(messageAdapter);
        rvMensajes.setLayoutManager(new LinearLayoutManager(this));*/

        listaMensajes = new ArrayList<>();

        messageAdapter = new MessageAdapter(listaMensajes, UID_USUARIO_ACTUAL);
        rvMensajes.setAdapter(messageAdapter);
        rvMensajes.setLayoutManager(new LinearLayoutManager(this));

        chatViewModel.getMensajes().observe(this, mensajes -> {
            listaMensajes.clear();
            listaMensajes.addAll(mensajes);
            messageAdapter.notifyDataSetChanged();
        });

        btnEnviar.setOnClickListener(v -> {
            String texto = etMensaje.getText().toString();
            String horaActual = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());

            chatViewModel.enviarMensaje(texto, UID_USUARIO_ACTUAL, horaActual);
            etMensaje.setText("");
        });

    }

    private List<Message> crearDatosDePrueba() {
        List<Message> lista = new ArrayList<>();

        lista.add(new Message("Hola, ¿cómo estás?", "uidMaria", "10:30", System.currentTimeMillis()));
        lista.add(new Message("Bien, ¿y tú?", "uidYo", "10:31", System.currentTimeMillis()));
        lista.add(new Message("También bien, gracias", "uidMaria", "10:32", System.currentTimeMillis()));
        lista.add(new Message("¿Nos vemos mañana?", "uidYo", "10:33", System.currentTimeMillis()));

        return lista;
    }

}