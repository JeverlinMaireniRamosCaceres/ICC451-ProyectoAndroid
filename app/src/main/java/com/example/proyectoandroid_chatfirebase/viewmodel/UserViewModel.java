package com.example.proyectoandroid_chatfirebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoandroid_chatfirebase.data.model.User;
import com.example.proyectoandroid_chatfirebase.data.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {

    private final UserRepository userRepository = new UserRepository();

    // Es la lista de usuarios para el recycler view
    private final MutableLiveData<List<User>> usuarios = new MutableLiveData<>();

    // Si algo falla
    private final MutableLiveData<String> error = new MutableLiveData<>();

    // Es el nombre del usuario que tiene la sesion iniciada para que lo muestre en el titulo
    private final MutableLiveData<String> miNombre = new MutableLiveData<>();

    public LiveData<List<User>> getUsuarios() {
        return usuarios;
    }

    public LiveData<String> getError() {
        return error;
    }

    // Se cargan todos los usuarios, separando el usuario actual para no mostrarlo en la lista
    public void cargarUsuarios(String miUid) {
        userRepository.obtenerUsuarios(new UserRepository.OnUsuariosObtenidosListener() {
            @Override
            public void onUsuariosObtenidos(List<User> lista) {
                List<User> filtrada = new ArrayList<>();
                for (User u : lista) {
                    if (miUid.equals(u.getUid())) {
                        miNombre.setValue(u.getNombre());
                    } else {
                        filtrada.add(u);
                    }
                }
                usuarios.setValue(filtrada);
            }

            @Override
            public void onError(String mensaje) {
                error.setValue(mensaje);
            }
        });
    }

    public String obtenerMiUid() {
        return userRepository.obtenerMiUid();
    }

    // Guarda el token del Fire Cloud Messaging de este dispositivo en el perfil del usuario actual
    public void guardarTokenActual() {
        userRepository.guardarTokenActual(obtenerMiUid());
    }

    // Borra el token y, al terminar, ejecuta la acción recibida, que es cerrar sesion
    public void cerrarSesionLimpiando(Runnable alTerminar) {
        userRepository.borrarToken(obtenerMiUid(), alTerminar);
    }

    public LiveData<String> getMiNombre() {
        return miNombre;
    }

}
