package com.example.proyectoandroid_chatfirebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid_chatfirebase.R;
import com.example.proyectoandroid_chatfirebase.data.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_ENVIADO = 1;
    private static final int TIPO_RECIBIDO = 2;

    private final List<Message> listaMensajes;
    private final String uidUsuarioActual;

    public MessageAdapter(List<Message> listaMensajes, String uidUsuarioActual) {
        this.listaMensajes = listaMensajes;
        this.uidUsuarioActual = uidUsuarioActual;
    }

    @Override
    public int getItemViewType(int position) {
        Message mensaje = listaMensajes.get(position);
        if (mensaje.getRemitenteuid().equals(uidUsuarioActual)) {
            return TIPO_ENVIADO;
        } else {
            return TIPO_RECIBIDO;
        }
    }

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message mensaje = listaMensajes.get(position);

        if (holder instanceof EnviadoViewHolder) {
            EnviadoViewHolder enviadoHolder = (EnviadoViewHolder) holder;
            enviadoHolder.txtMensajeEnv.setText(mensaje.getTexto());
            enviadoHolder.txtHoraEnv.setText(mensaje.getHora());
        } else if (holder instanceof RecibidoViewHolder) {
            RecibidoViewHolder recibidoHolder = (RecibidoViewHolder) holder;
            recibidoHolder.txtMensajeRec.setText(mensaje.getTexto());
            recibidoHolder.txtHoraRec.setText(mensaje.getHora());
        }
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    static class EnviadoViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensajeEnv;
        TextView txtHoraEnv;

        public EnviadoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensajeEnv = itemView.findViewById(R.id.txtMensajeEnv);
            txtHoraEnv = itemView.findViewById(R.id.txtHoraEnv);
        }
    }

    static class RecibidoViewHolder extends RecyclerView.ViewHolder {
        TextView txtMensajeRec;
        TextView txtHoraRec;

        public RecibidoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMensajeRec = itemView.findViewById(R.id.txtMensajeRec);
            txtHoraRec = itemView.findViewById(R.id.txtHoraRec);
        }
    }
}