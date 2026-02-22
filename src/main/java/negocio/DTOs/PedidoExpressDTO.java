/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

import java.time.LocalDateTime;

/**
 *
 * @author ERICK
 */
public class PedidoExpressDTO {
    private int idPedido;
    private int folio;
    private String pinEncriptado;
    private String nombreCliente;
    private String telefonoCliente;
    private double subtotal;
    private double total;
    private String estado;
    private LocalDateTime fechaCreacion;

    public PedidoExpressDTO() {}

    public PedidoExpressDTO(int idPedido, int folio, String pinEncriptado, String nombreCliente,
                             String telefonoCliente, double subtotal, double total,
                             String estado, LocalDateTime fechaCreacion) {
        this.idPedido = idPedido;
        this.folio = folio;
        this.pinEncriptado = pinEncriptado;
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.subtotal = subtotal;
        this.total = total;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getPinEncriptado() {
        return pinEncriptado;
    }

    public void setPinEncriptado(String pinEncriptado) {
        this.pinEncriptado = pinEncriptado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
