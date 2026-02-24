/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Isaaa
 */
public class PedidoExpressDTO {
    private int idPedido;
    private int folio;
    private double subtotal;
    private double total;
    private String estado;
    private String pin;
    private LocalDateTime fechaCreacion;
    private List<DetallePedidoDTO> detalles;
    private LocalDateTime fechaListo;

    public PedidoExpressDTO() {}

    public PedidoExpressDTO(int idPedido,
                            int folio,
                            double subtotal,
                            double total,
                            String estado,
                            String pin,
                            LocalDateTime fechaCreacion,
                            List<DetallePedidoDTO> detalles,
                            LocalDateTime fechaListo) {

        this.idPedido = idPedido;
        this.folio = folio;
        this.subtotal = subtotal;
        this.total = total;
        this.estado = estado;
        this.pin=pin;
        this.fechaCreacion = fechaCreacion;
        this.detalles = detalles;
        this.fechaListo=fechaListo;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }
    public void setDetalles(List<DetallePedidoDTO> detalles) {
    this.detalles = detalles;
}

    public LocalDateTime getFechaListo() {
        return fechaListo;
    }

    public void setFechaListo(LocalDateTime fechaListo) {
        this.fechaListo = fechaListo;
    }
    
}
