package com.example.proyectoandroid_chatfirebase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectoandroid_chatfirebase.R;
import com.example.proyectoandroid_chatfirebase.data.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<User> listaUsuarios;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(User usuario);
    }

    public UserAdapter(List<User> listaUsuarios, OnUserClickListener listener) {
        this.listaUsuarios = listaUsuarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UserViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User usuario = listaUsuarios.get(position);

        holder.txtNombre.setText(usuario.getNombre());
        holder.txtUltMesj.setText(usuario.getUltimoMensaje());
        holder.txtHora.setText(usuario.getHoraUltimoMensaje());
        holder.itemView.setOnClickListener(v -> listener.onUserClick(usuario));
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView txtNombre;
        TextView txtUltMesj;
        TextView txtHora;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtUltMesj = itemView.findViewById(R.id.txtUltMesj);
            txtHora = itemView.findViewById(R.id.txtHora);
        }
    }
}