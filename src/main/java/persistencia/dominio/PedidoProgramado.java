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
public class PedidoProgramado extends Pedido{
    private int idCliente;
    private Integer idCupon;

    public PedidoProgramado() {
    }

    public PedidoProgramado(int idCliente, Integer idCupon) {
        this.idCliente = idCliente;
        this.idCupon = idCupon;
    }

    public PedidoProgramado(int idCliente, Integer idCupon, int idPedido, LocalDate fechaCreacion, String estado, double subtotal, float descuento, double total, int idEmpleado) {
        super(idPedido, fechaCreacion, estado, subtotal, descuento, total, idEmpleado);
        this.idCliente = idCliente;
        this.idCupon = idCupon;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public Integer getIdCupon() {
        return idCupon;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    @Override
    public String toString() {
        return "PedidoProgramado{" + ", idCliente=" + idCliente + ", idCupon=" + idCupon + '}';
    }
}
