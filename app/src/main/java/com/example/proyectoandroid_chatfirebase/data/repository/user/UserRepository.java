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

    public void guardarToken(String uid, String token) {
        db.collection("usuarios")
                .document(uid)
                .update("fcmToken", token);
    }

    public void borrarToken(String uid, Runnable alTerminar) {
        db.collection("usuarios")
                .document(uid)
                .update("fcmToken", FieldValue.delete())
                .addOnCompleteListener(tarea -> alTerminar.run());
    }

    public String obtenerMiUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void guardarTokenActual(String uid) {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> guardarToken(uid, token))
                .addOnFailureListener(e -> Log.e("FCM", "No se pudo obtener el token: " + e.getMessage()));
    }

    public interface OnUsuariosObtenidosListener {
        void onUsuariosObtenidos(List<User> usuarios);
        void onError(String error);
    }




}
