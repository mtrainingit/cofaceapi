package com.coface.usuario.exception;

public class CampoOrdenDesconocido extends RuntimeException {
    public CampoOrdenDesconocido(String mensaje) {
        super(mensaje);
    }
}
