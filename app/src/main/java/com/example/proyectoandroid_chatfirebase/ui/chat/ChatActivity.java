package com.example.proyectoandroid_chatfirebase.ui.chat;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import android.net.Uri;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

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
    private String uidUsuarioActual;
    private ChatViewModel chatViewModel;

    // Abre la galeria y al elegir una imagen la entrega al ViewModel
    private final ActivityResultLauncher<String> selectorImagen = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        String horaActual = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
                        chatViewModel.enviarImagen(uri, uidUsuarioActual, horaActual);
                    }
                }
            });

    // Prepara la pantalla del chat conectando vistas, ViewModel, lista de mensajes y botones
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        uidOtroUs = getIntent().getStringExtra("uid");
        nombreOtroUs = getIntent().getStringExtra("nombre");

        if (nombreOtroUs != null) {
            toolbar.setTitle(nombreOtroUs);
        }

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        uidUsuarioActual = chatViewModel.obtenerMiUid();
        chatViewModel.iniciarChat(uidUsuarioActual, uidOtroUs);

        rvMensajes = findViewById(R.id.rvMensajes);
        EditText etMensaje = findViewById(R.id.etMensaje);
        ImageButton btnEnviar = findViewById(R.id.btnEnviar);
        ImageButton btnAdjuntar = findViewById(R.id.btnAdjuntar);

        listaMensajes = new ArrayList<>();

        messageAdapter = new MessageAdapter(listaMensajes, uidUsuarioActual);
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

            chatViewModel.enviarMensaje(texto, uidUsuarioActual, horaActual);
            etMensaje.setText("");
        });

        btnAdjuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectorImagen.launch("image/*");
            }
        });

    }

    // La flecha de la barra cierra el chat y vuelve a la pantalla anterior
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}