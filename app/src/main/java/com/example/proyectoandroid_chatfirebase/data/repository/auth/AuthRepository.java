package com.example.proyectoandroid_chatfirebase.data.repository.auth;

import com.example.proyectoandroid_chatfirebase.data.model.User;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepository {
    public static final String COLLECTION_USUARIOS = "usuarios";

    public interface AuthCallback {
        void onSuccess();

        void onError(String message);
    }

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public void login(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(getErrorMessage(e)));
    }

    public void register(String nombre, String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser firebaseUser = result.getUser();
                    User user = new User(firebaseUser.getUid(), nombre, email);
                    firestore.collection(COLLECTION_USUARIOS).document(user.getUid()).set(user)
                            .addOnSuccessListener(unused -> callback.onSuccess())
                            .addOnFailureListener(e -> {
                                firebaseUser.delete();
                                callback.onError("No se pudo guardar tu perfil. Intenta de nuevo.");
                            });
                }).addOnFailureListener(e -> callback.onError(getErrorMessage(e)));
    }

    public void logout() {
        firebaseAuth.signOut();
    }

    private String getErrorMessage(Exception e) {
        if (e instanceof FirebaseAuthWeakPasswordException) {
            return "La contraseña es muy débil. Usa al menos 6 caracteres.";
        }
        if (e instanceof FirebaseAuthUserCollisionException) {
            return "Ya existe una cuenta con ese correo.";
        }
        if (e instanceof FirebaseAuthInvalidUserException
                || e instanceof FirebaseAuthInvalidCredentialsException) {
            return "Correo o contraseña incorrectos. Si no tiene cuenta, regístrese.";
        }
        if (e instanceof FirebaseNetworkException) {
            return "No hay conexión a internet. Revisa tu red.";
        }
        return "Ocurrió un error. Intenta de nuevo.";
    }
}
