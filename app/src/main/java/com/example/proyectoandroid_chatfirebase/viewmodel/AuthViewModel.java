package com.example.proyectoandroid_chatfirebase.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectoandroid_chatfirebase.data.repository.auth.AuthRepository;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository = new AuthRepository();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> authSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

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

    public void login(String email, String password) {
        loading.setValue(true);
        authRepository.login(email, password, authCallback);
    }

    public void register(String nombre, String email, String password) {
        loading.setValue(true);
        authRepository.register(nombre, email, password, authCallback);
    }
}
