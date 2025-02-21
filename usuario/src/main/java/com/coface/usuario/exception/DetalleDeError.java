package com.coface.usuario.exception;

import java.util.Date;

public class DetalleDeError {

    private Date fecha;
    private String mensaje;
    private String detalle;

    public DetalleDeError(Date fecha, String mensaje, String detalle) {
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.detalle = detalle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
