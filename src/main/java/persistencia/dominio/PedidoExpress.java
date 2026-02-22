/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author ERICK
 */
public class PedidoExpress extends Pedido{
    private String folio;
    private String pinEncriptado;
    private LocalDateTime fechaListo;

    public PedidoExpress() {
    }

    public PedidoExpress(String folio,
                         String pinEncriptado,
                         LocalDate fechaCreacion,
                         String estado,
                         double subtotal,
                         float descuento,
                         double total,
                         int idEmpleado) {

        super(0, fechaCreacion, estado, subtotal, descuento, total, idEmpleado);
        this.folio = folio;
        this.pinEncriptado = pinEncriptado;
    }

    public PedidoExpress(int idPedido,
                         String folio,
                         String pinEncriptado,
                         LocalDate fechaCreacion,
                         String estado,
                         double subtotal,
                         float descuento,
                         double total,
                         int idEmpleado,
                         LocalDateTime fechaListo) {

        super(idPedido, fechaCreacion, estado, subtotal, descuento, total, idEmpleado);
        this.folio = folio;
        this.pinEncriptado = pinEncriptado;
        this.fechaListo = fechaListo;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getPinEncriptado() {
        return pinEncriptado;
    }

    public void setPinEncriptado(String pinEncriptado) {
        this.pinEncriptado = pinEncriptado;
    }

    public LocalDateTime getFechaListo() {
        return fechaListo;
    }

    public void setFechaListo(LocalDateTime fechaListo) {
        this.fechaListo = fechaListo;
    }

    @Override
    public String toString() {
        return "PedidoExpress{" + "folio=" + folio + ", pinEncriptado=" + pinEncriptado + ", fechaListo=" + fechaListo + '}';
    }
}
