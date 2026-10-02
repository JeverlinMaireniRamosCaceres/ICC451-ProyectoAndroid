package com.example.proyectoandroid_chatfirebase.viewmodel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoandroid_chatfirebase.data.model.Message;
import com.example.proyectoandroid_chatfirebase.data.repository.message.MessageRepository;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private final MessageRepository messageRepository;
    private final MutableLiveData<List<Message>> mensajes = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private String chatId;

    public ChatViewModel() {
        messageRepository = new MessageRepository();
    }

    public LiveData<List<Message>> getMensajes() {
        return mensajes;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void iniciarChat(String uidUsuarioActual, String uidOtroUsuario) {
        chatId = messageRepository.generarChatId(uidUsuarioActual, uidOtroUsuario);

        messageRepository.escucharMensajes(chatId, new MessageRepository.OnMensajesActualizadosListener() {
            @Override
            public void onMensajesActualizados(List<Message> listaMensajes) {
                mensajes.setValue(listaMensajes);
            }

            @Override
            public void onError(String mensajeError) {
                error.setValue(mensajeError);
            }
        });
    }

    public void enviarMensaje(String texto, String uidUsuarioActual, String hora) {
        if (texto == null || texto.trim().isEmpty()) {
            return;
        }

        Message nuevoMensaje = new Message(texto, uidUsuarioActual, hora, System.currentTimeMillis(), null);

        messageRepository.enviarMensaje(chatId, nuevoMensaje, new MessageRepository.OnMensajeEnviadoListener() {
            @Override
            public void onExito() {
 
            }

            @Override
            public void onError(String mensajeError) {
                error.setValue(mensajeError);
            }
        });
    }
    public void enviarImagen(Uri imagenUri, String uidUsuarioActual, String hora) {
        if (imagenUri == null) {
            return;
        }

        messageRepository.subirImagen(chatId, imagenUri, new MessageRepository.OnImagenSubidaListener() {
            @Override
            public void onExito(String urlImagen) {
                Message nuevoMensaje = new Message("", uidUsuarioActual, hora, System.currentTimeMillis(), urlImagen);

                messageRepository.enviarMensaje(chatId, nuevoMensaje, new MessageRepository.OnMensajeEnviadoListener() {
                    @Override
                    public void onExito() {

                    }

                    @Override
                    public void onError(String mensajeError) {
                        error.setValue(mensajeError);
                    }
                });
            }

            @Override
            public void onError(String mensajeError) {
                error.setValue(mensajeError);
            }
        });
    }
}