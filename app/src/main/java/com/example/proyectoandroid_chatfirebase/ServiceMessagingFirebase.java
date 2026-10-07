package com.example.proyectoandroid_chatfirebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.PendingIntent;
import android.content.Intent;
import java.util.Map;
import com.example.proyectoandroid_chatfirebase.ui.chat.ChatActivity;import android.app.PendingIntent;
import android.content.Intent;
import java.util.Map;
import com.example.proyectoandroid_chatfirebase.ui.chat.ChatActivity;

public class ServiceMessagingFirebase extends FirebaseMessagingService {

    private static final String TAG = "FCM";

    // Id del canal al que pertenecen las notificaciones de los mensajes
    private static final String CANAL_ID = "canal_mensajes";

    // Se ejecuta cuando el token es renovado por Firebase
    // solo se registra en el log
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo token: " + token);
    }

    // Se ejecuta cuando llega el mensaje que fue enviado por el cloud function
    @Override
    public void onMessageReceived(@NonNull RemoteMessage mensaje) {
        super.onMessageReceived(mensaje);
        Log.d(TAG, "Mensaje recibido de: " + mensaje.getFrom());

        // Lee los datos que envio la cloud function
        Map<String, String> datos = mensaje.getData();
        String titulo = datos.get("titulo");
        String cuerpo = datos.get("cuerpo");
        String remitenteUid = datos.get("remitenteUid");
        String remitenteNombre = datos.get("remitenteNombre");

        // Si no tiene titulo ni cuerpo no se muestra nada
        if (titulo == null || cuerpo == null) {
            return;
        }

        crearCanal();

        // Si no hay permiso de notificaciones no se muestra
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Al tocar la notificacion se abre el chat del remitente con los mismos uid y nombre que la lista
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("uid", remitenteUid);
        intent.putExtra("nombre", remitenteNombre);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Id unico para que cada notificacion aparezca por separado
        int idNotificacion = (int) System.currentTimeMillis();

        // PendingIntent guarda el intent para que el sistema lo ejecute al tocar la notificacion
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                idNotificacion,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        // NotificationCompat hace que el mismo codigo funcione en distintas versiones de Android
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CANAL_ID)
                .setSmallIcon(R.drawable.baseline_account_circle_24)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(idNotificacion, builder.build());

    }

    private void crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CANAL_ID, "Mensajes", NotificationManager.IMPORTANCE_HIGH);
            getSystemService(NotificationManager.class).createNotificationChannel(canal);
        }
    }

}
