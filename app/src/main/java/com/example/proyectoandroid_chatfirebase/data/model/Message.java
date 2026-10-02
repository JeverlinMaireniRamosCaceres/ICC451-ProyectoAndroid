package com.example.proyectoandroid_chatfirebase.data.model;

public class Message {
    private String texto;
    private String remitenteuid;
    private String hora;
    private long timestamp;
    private String imagenUrl;

    public Message() {

    }
    public Message(String texto, String remitenteuid, String hora, long timestamp, String imagenUrl) {
        this.texto = texto;
        this.remitenteuid = remitenteuid;
        this.hora = hora;
        this.timestamp = timestamp;
        this.imagenUrl = imagenUrl;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getRemitenteuid() {
        return remitenteuid;
    }

    public void setRemitenteuid(String remitenteuid) {
        this.remitenteuid = remitenteuid;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImagenUrl() {return imagenUrl;}

    public void setImagenUrl(String imagenUrl) {this.imagenUrl = imagenUrl;}
}

