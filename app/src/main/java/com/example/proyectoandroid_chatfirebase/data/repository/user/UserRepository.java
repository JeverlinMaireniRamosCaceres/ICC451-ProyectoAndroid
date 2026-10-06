package com.example.proyectoandroid_chatfirebase.data.repository.user;

import com.example.proyectoandroid_chatfirebase.data.model.User;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class UserRepository {

    private final FirebaseFirestore db;

    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // Para obtener la lista de usuarios
    // Usa un get, por lo que la lectura no es en tiempo real
    public void obtenerUsuarios(OnUsuariosObtenidosListener listener) {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<User> lista = new ArrayList<>();
                    for (var documento : querySnapshot.getDocuments()) {
                        User usuario = documento.toObject(User.class);
                        if (usuario != null) {
                            lista.add(usuario);
                        }
                    }
                    listener.onUsuariosObtenidos(lista);
                })
                .addOnFailureListener(e -> listener.onError(e.getMessage()));
    }

    // Guarda el token del Fire Cloud Messaging para que la funcion de Cloud Function sepa a que dispositivo enviar la notificacion
    public void guardarToken(String uid, String token) {
        db.collection("usuarios")
                .document(uid)
                .update("fcmToken", token);
    }

    // Borra el token al cerrar la sesion para que el telefono no reciba las notificaciones
    public void borrarToken(String uid, Runnable alTerminar) {
        db.collection("usuarios")
                .document(uid)
                .update("fcmToken", FieldValue.delete())
                .addOnCompleteListener(tarea -> alTerminar.run());
    }

    public String obtenerMiUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // Para guardar el token del dispositivo
    public void guardarTokenActual(String uid) {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> guardarToken(uid, token))
                .addOnFailureListener(e -> Log.e("FCM", "No se pudo obtener el token: " + e.getMessage()));
    }

    // Es el callback para retornar la lista de usuarios al viewmodel
    public interface OnUsuariosObtenidosListener {
        void onUsuariosObtenidos(List<User> usuarios);
        void onError(String error);
    }




}
