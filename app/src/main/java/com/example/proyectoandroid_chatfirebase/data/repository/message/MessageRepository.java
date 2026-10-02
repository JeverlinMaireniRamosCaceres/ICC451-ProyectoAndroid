package com.example.proyectoandroid_chatfirebase.data.repository.message;

import android.net.Uri;

import com.example.proyectoandroid_chatfirebase.data.model.Message;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRepository {

    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    public MessageRepository() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public String generarChatId(String uid1, String uid2) {
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + "_" + uid2;
        } else {
            return uid2 + "_" + uid1;
        }
    }

    public void enviarMensaje(String chatId, Message mensaje, OnMensajeEnviadoListener listener) {
        db.collection("chats")
                .document(chatId)
                .collection("mensajes")
                .add(mensaje)
                .addOnSuccessListener(documentReference -> listener.onExito())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void subirImagen(String chatId, Uri imagenUri, OnImagenSubidaListener listener) {
        String nombreArchivo = UUID.randomUUID().toString() + ".jpg";
        StorageReference referencia = storage.getReference()
                .child("chats")
                .child(chatId)
                .child(nombreArchivo);

        referencia.putFile(imagenUri)
                .addOnSuccessListener(taskSnapshot -> {
                    referencia.getDownloadUrl()
                            .addOnSuccessListener(uri -> listener.onExito(uri.toString()))
                            .addOnFailureListener(e -> listener.onError(e.getMessage()));
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    public void escucharMensajes(String chatId, OnMensajesActualizadosListener listener) {
        db.collection("chats")
                .document(chatId)
                .collection("mensajes")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((QuerySnapshot querySnapshot, com.google.firebase.firestore.FirebaseFirestoreException error) -> {
                    if (error != null) {
                        listener.onError(error.getMessage());
                        return;
                    }

                    if (querySnapshot != null) {
                        List<Message> listaMensajes = new ArrayList<>();
                        for (var documento : querySnapshot.getDocuments()) {
                            Message mensaje = documento.toObject(Message.class);
                            listaMensajes.add(mensaje);
                        }
                        listener.onMensajesActualizados(listaMensajes);
                    }
                });
    }

    public interface OnMensajeEnviadoListener {
        void onExito();
        void onError(String error);
    }

    public interface OnImagenSubidaListener {
        void onExito(String urlImagen);
        void onError(String error);
    }

    public interface OnMensajesActualizadosListener {
        void onMensajesActualizados(List<Message> mensajes);
        void onError(String error);
    }
}