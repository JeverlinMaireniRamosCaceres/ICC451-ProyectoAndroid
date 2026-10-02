package com.example.proyectoandroid_chatfirebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServiceMessagingFirebase extends FirebaseMessagingService {

    private static final String TAG = "FCM";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage mensaje) {
        super.onMessageReceived(mensaje);
        Log.d(TAG, "Mensaje recibido de: " + mensaje.getFrom());
    }

}
