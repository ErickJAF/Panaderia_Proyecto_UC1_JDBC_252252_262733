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
public class PedidoEntregaDTO {
    private int idPedido;
    private String nombreCliente;
    private String telefono;
    private Integer folio; // puede ser null si no es express
    private String estado;
    private double total;
    private LocalDateTime fechaCreacion;

    public PedidoEntregaDTO() {
    }

    public PedidoEntregaDTO(int idPedido, String nombreCliente, String telefono, Integer folio, String estado, double total, LocalDateTime fechaCreacion) {
        this.idPedido = idPedido;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.folio = folio;
        this.estado = estado;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "PedidoEntregaDTO{" + "idPedido=" + idPedido + ", nombreCliente=" + nombreCliente + ", telefono=" + telefono + ", folio=" + folio + ", estado=" + estado + ", total=" + total + ", fechaCreacion=" + fechaCreacion + '}';
    }
}
