package com.example.proyectoandroid_chatfirebase.data.model;

public class User {

    private String uid;
    private String nombre;
    private String email;
    private String ultimoMensaje;
    private String horaUltimoMensaje;

    public User(){

    }

    public User(String uid, String nombre, String email){
        this.uid = uid;
        this.nombre = nombre;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUltimoMensaje() {
        return ultimoMensaje;
    }
    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }
    public String getHoraUltimoMensaje() {
        return horaUltimoMensaje;
    }
    public void setHoraUltimoMensaje(String horaUltimoMensaje) {
        this.horaUltimoMensaje = horaUltimoMensaje;
    }
}
