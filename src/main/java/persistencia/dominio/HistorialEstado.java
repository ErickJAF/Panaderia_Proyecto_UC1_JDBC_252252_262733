/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.time.LocalDateTime;

/**
 *
 * @author ERICK
 */
public class HistorialEstado {
    private int idHistorial;
    private int idPedido;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;

    public HistorialEstado() {
    }

    public HistorialEstado(int idHistorial, int idPedido, String estadoAnterior, String estadoNuevo, LocalDateTime fechaCambio) {
        this.idHistorial = idHistorial;
        this.idPedido = idPedido;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }

    public int getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(int idHistorial) {
        this.idHistorial = idHistorial;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(String estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(String estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    @Override
    public String toString() {
        return "HistorialEstado{" + "idHistorial=" + idHistorial + ", idPedido=" + idPedido + ", estadoAnterior=" + estadoAnterior + ", estadoNuevo=" + estadoNuevo + ", fechaCambio=" + fechaCambio + '}';
    }
}
