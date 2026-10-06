package com.example.proyectoandroid_chatfirebase.data.model;

public class Message {
    private String texto;
    private String remitenteuid;

    // Hora formateada para cada mensaje
    private String hora;

    // Timestamp para ordenar los mensajes por hora de llegada
    private long timestamp;

    // Almacena la url de la imagen en Storage, si es un mensaje de texto queda nula
    private String imagenUrl;

    // Constructor vacio necesario para que Firestore convierta a documentos
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

