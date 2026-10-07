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

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    // Lista de los usuarios que se ven en la pantalla en el momento
    private final List<User> listaUsuarios;

    // Copia de todos los usuarios para poder restaurar la lista al borrar la busqueda
    private final List<User> listaCompleta = new ArrayList<>();
    private final OnUserClickListener listener;

    // A traves de esta interfaz el adapter avisa al activity que hicieron clic sobre un usuario
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

    // Llena la fila con los datos del usuario y conecta el clic
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

    // Recibe la lista nueva del ViewModel y la muestra sin filtro
    public void actualizarLista(List<User> nuevosUsuarios) {
        listaCompleta.clear();
        listaCompleta.addAll(nuevosUsuarios);
        filtrar("");
    }

    // Busca en la lista que ya esta descargada, no consulta en Firestore
    // Para filtrar los usuarios
    public void filtrar(String texto) {
        String busqueda = texto == null ? "" : texto.trim().toLowerCase();

        listaUsuarios.clear();
        for (User usuario : listaCompleta) {
            String nombre = usuario.getNombre() == null ? "" : usuario.getNombre().toLowerCase();
            if (nombre.contains(busqueda)) {
                listaUsuarios.add(usuario);
            }
        }
        notifyDataSetChanged();
    }

    // Guarda las referencias a las vistas de la fila para no buscarlas en cada bind
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