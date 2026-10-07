package com.example.proyectoandroid_chatfirebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectoandroid_chatfirebase.R;
import com.example.proyectoandroid_chatfirebase.data.model.Message;

import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constantes para saber que tipo de fila es, para saber cuales son los mios y cuales son los del otro usuario
    private static final int TIPO_ENVIADO = 1;
    private static final int TIPO_RECIBIDO = 2;

    private final List<Message> listaMensajes;
    private final String uidUsuarioActual;

    public MessageAdapter(List<Message> listaMensajes, String uidUsuarioActual) {
        this.listaMensajes = listaMensajes;
        this.uidUsuarioActual = uidUsuarioActual;
    }

    // Los agrupa por remitente, si es mensaje propio lo coloca a la derecba
    // si es recibido lo coloca a la izquierda
    @Override
    public int getItemViewType(int position) {
        Message mensaje = listaMensajes.get(position);
        if (mensaje.getRemitenteuid().equals(uidUsuarioActual)) {
            return TIPO_ENVIADO;
        } else {
            return TIPO_RECIBIDO;
        }
    }

    // Se ejecuta cada vez que necesite crearse una fila nueva,
    // Se crea el layout con el tipo de fila que corresponda al tipo de mensaje
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TIPO_ENVIADO) {
            View vista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msj_env, parent, false);
            return new EnviadoViewHolder(vista);
        } else {
            View vista = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_msj_rec, parent, false);
            return new RecibidoViewHolder(vista);
        }
    }

    // Se ejecuta cada vez que una fila va a mostrarse
    // Se llena la fila con los datos del mensaje, digase contenido, hora y fecha
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message mensaje = listaMensajes.get(position);

        if (holder instanceof EnviadoViewHolder) {
            EnviadoViewHolder enviadoHolder = (EnviadoViewHolder) holder;
            mostrarContenido(mensaje, enviadoHolder.txtMensajeEnv, enviadoHolder.imgMensajeEnv);
            enviadoHolder.txtHoraEnv.setText(mensaje.getHora());
            configurarFecha(enviadoHolder.txtFecha, position);
        } else if (holder instanceof RecibidoViewHolder) {
            RecibidoViewHolder recibidoHolder = (RecibidoViewHolder) holder;
            mostrarContenido(mensaje, recibidoHolder.txtMensajeRec, recibidoHolder.imgMensajeRec);
            recibidoHolder.txtHoraRec.setText(mensaje.getHora());
            configurarFecha(recibidoHolder.txtFecha, position);
        }
    }

    // Si hay URL muestra la imagen con Glide, si no, muestra el texto
    private void mostrarContenido(Message mensaje, TextView txtMensaje, ImageView imgMensaje) {
        String urlImagen = mensaje.getImagenUrl();

        if (urlImagen != null && !urlImagen.isEmpty()) {
            txtMensaje.setVisibility(View.GONE);
            imgMensaje.setVisibility(View.VISIBLE);
            Glide.with(imgMensaje.getContext()).load(urlImagen).into(imgMensaje);
        } else {
            imgMensaje.setVisibility(View.GONE);
            txtMensaje.setVisibility(View.VISIBLE);
            txtMensaje.setText(mensaje.getTexto());
        }
    }

    // Convierte el timestamp en una clave de dia para poder comparar las fechas
    private String claveDia(long timestamp) {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date(timestamp));
    }

    // Retorna hoy, ayer o la fecha completa
    private String textoFecha(long timestamp) {
        String clave = claveDia(timestamp);
        long ahora = System.currentTimeMillis();

        if (clave.equals(claveDia(ahora))) {
            return "Hoy";
        }
        if (clave.equals(claveDia(ahora - 24L * 60 * 60 * 1000))) {
            return "Ayer";
        }
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(timestamp));
    }

    // Agrupa por día mostrando la fecha solo en el primer mensaje o cuando cambia de dia
    private void configurarFecha(TextView txtFecha, int position) {
        long actual = listaMensajes.get(position).getTimestamp();

        boolean mostrar = position == 0
                || !claveDia(actual).equals(claveDia(listaMensajes.get(position - 1).getTimestamp()));

        if (mostrar) {
            txtFecha.setText(textoFecha(actual));
            txtFecha.setVisibility(View.VISIBLE);
        } else {
            txtFecha.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    // Guarda las referencias a las vistas de la burbuja para no buscarlas en cada bind
    static class EnviadoViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensajeEnv;
        ImageView imgMensajeEnv;
        TextView txtHoraEnv;
        TextView txtFecha;

        public EnviadoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensajeEnv = itemView.findViewById(R.id.txtMensajeEnv);
            imgMensajeEnv = itemView.findViewById(R.id.imgMensajeEnv);
            txtHoraEnv = itemView.findViewById(R.id.txtHoraEnv);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }

    static class RecibidoViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensajeRec;
        ImageView imgMensajeRec;
        TextView txtHoraRec;
        TextView txtFecha;

        public RecibidoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensajeRec = itemView.findViewById(R.id.txtMensajeRec);
            imgMensajeRec = itemView.findViewById(R.id.imgMensajeRec);
            txtHoraRec = itemView.findViewById(R.id.txtHoraRec);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}