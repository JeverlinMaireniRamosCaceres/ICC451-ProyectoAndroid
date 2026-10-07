package com.example.proyectoandroid_chatfirebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoandroid_chatfirebase.data.repository.auth.AuthRepository;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository = new AuthRepository();

    // Los MutableLiveData se utilizan para saber el estado de la pantalla,
    // es decir, lo que la activity necesita para dibujarse
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> authSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // Este callback recibe la respuesta del repositorio, que es el AuthRepository, y las retorna para los live data
    private final AuthRepository.AuthCallback authCallback = new AuthRepository.AuthCallback() {
        @Override
        public void onSuccess() {
            loading.setValue(false);
            authSuccess.setValue(true);
        }

        @Override
        public void onError(String message) {
            loading.setValue(false);
            errorMessage.setValue(message);
        }
    };

    // Los LiveData solo se pueden leer, no se pueden modificar. La vista no puede modificarlos
    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getAuthSuccess() {
        return authSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void clearError() {
        errorMessage.setValue(null);
    }


    public boolean isUserLoggedIn() {
        return authRepository.isUserLoggedIn();
    }

    // Muestra el indicador de que esta cargando y pasa el login al repositorio
    public void login(String email, String password) {
        loading.setValue(true);
        authRepository.login(email, password, authCallback);
    }

    // Muestra el indicador de que esta cargando y pasa el registro al repositorio
    public void register(String nombre, String email, String password) {
        loading.setValue(true);
        authRepository.register(nombre, email, password, authCallback);
    }

    public void logout() {
        authRepository.logout();
    }
}
