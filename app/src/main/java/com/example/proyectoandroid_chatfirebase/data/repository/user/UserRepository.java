package com.example.proyectoandroid_chatfirebase.data.repository.user;

import com.example.proyectoandroid_chatfirebase.data.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

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

    public interface OnUsuariosObtenidosListener {
        void onUsuariosObtenidos(List<User> usuarios);
        void onError(String error);
    }




}
