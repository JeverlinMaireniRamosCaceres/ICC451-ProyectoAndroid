package com.example.proyectoandroid_chatfirebase.data.repository.message;

import android.net.Uri;

import com.example.proyectoandroid_chatfirebase.data.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRepository {

    // Para los mensajes
    private final FirebaseFirestore db;

    // Para las imagenes
    private final FirebaseStorage storage;
    public MessageRepository() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    // Para generar el id del chat ordenado para que los dos telefonos tengan el mismo chatId
    // Recibe los dos uid para unirlos con un underscore
    // Coloca primero al que va antes alfabeticamente
    public String generarChatId(String uid1, String uid2) {
        if (uid1.compareTo(uid2) < 0) {
            return uid1 + "_" + uid2;
        } else {
            return uid2 + "_" + uid1;
        }
    }

    // Retorna el uid del usuario que tiene la sesion iniciada
    public String obtenerMiUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // Guarda el mensaje en la subcoleccion de mensaje dentro del documento del chat
    public void enviarMensaje(String chatId, Message mensaje, OnMensajeEnviadoListener listener) {
        db.collection("chats")
                .document(chatId)
                .collection("mensajes")
                .add(mensaje)
                .addOnSuccessListener(documentReference -> listener.onExito())
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // Primero se sube el archivo de la imagen a Storage y devuelve su url
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

    // Es la comunicacion en tiempo real
    // Primero se ordenan los mensajes del mas viejo al mas reciente
    // Se tiene un snapshot listener que se ejecuta cada vez que hay un cambio en la subcoleccion de mensajes
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

    // Callbacks para comunicarse con el viewmodel de mensaje de forma asincrona
    
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