/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ERICK
 */
public class PedidoExpressDTO {
    private int idPedido;
    private int folio;
    private double subtotal;
    private double total;
    private String estado;
    private LocalDateTime fechaCreacion;
    private List<DetallePedidoDTO> detalles;

    public PedidoExpressDTO() {}

    public PedidoExpressDTO(int idPedido,
                            int folio,
                            double subtotal,
                            double total,
                            String estado,
                            LocalDateTime fechaCreacion,
                            List<DetallePedidoDTO> detalles) {

        this.idPedido = idPedido;
        this.folio = folio;
        this.subtotal = subtotal;
        this.total = total;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.detalles = detalles;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }
}
