/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.time.LocalDate;

/**
 *
 * @author ERICK
 */
public class Pedido {
    private int idPedido;
    private LocalDate fechaCreacion;
    private String estado;
    private double subtotal;
    private float descuento;
    private double total;
    private int idEmpleado;

    public Pedido() {
    }

    public Pedido(int idPedido, LocalDate fechaCreacion, String estado, double subtotal, float descuento, double total, int idEmpleado) {
        this.idPedido = idPedido;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.idEmpleado = idEmpleado;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public float getDescuento() {
        return descuento;
    }

    public double getTotal() {
        return total;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public String toString() {
        return "Pedido{" + "idPedido=" + idPedido + ", fechaCreacion=" + fechaCreacion + ", estado=" + estado + ", subtotal=" + subtotal + ", descuento=" + descuento + ", total=" + total + ", idEmpleado=" + idEmpleado + '}';
    }
}
