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
public class Pago {
    private int idPago;
    private LocalDate fechaPago;
    private double monto;
    private String metodoPago;
    private int idPedido;

    public Pago() {
    }

    public Pago(int idPago, LocalDate fechaPago, double monto, String metodoPago, int idPedido) {
        this.idPago = idPago;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.idPedido = idPedido;
    }

    public int getIdPago() {
        return idPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    @Override
    public String toString() {
        return "Pago{" + "idPago=" + idPago + ", fechaPago=" + fechaPago + ", monto=" + monto + ", metodoPago=" + metodoPago + ", idPedido=" + idPedido + '}';
    }
}
