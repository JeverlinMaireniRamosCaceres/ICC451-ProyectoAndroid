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
    private final MutableLiveData<List<User>> usuarios = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<User>> getUsuarios() {
        return usuarios;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void cargarUsuarios(String miUid) {
        userRepository.obtenerUsuarios(new UserRepository.OnUsuariosObtenidosListener() {
            @Override
            public void onUsuariosObtenidos(List<User> lista) {
                List<User> filtrada = new ArrayList<>();
                for (User u : lista) {
                    if (!miUid.equals(u.getUid())) {
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


}
