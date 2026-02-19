/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

/**
 *
 * @author ERICK
 */
public class PedidoProgramado {
     private int idPedido;
    private int idCliente;
    private Integer idCupon;

    public PedidoProgramado() {
    }

    public PedidoProgramado(int idPedido, int idCliente, Integer idCupon) {
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.idCupon = idCupon;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public Integer getIdCupon() {
        return idCupon;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    @Override
    public String toString() {
        return "PedidoProgramado{" + "idPedido=" + idPedido + ", idCliente=" + idCliente + ", idCupon=" + idCupon + '}';
    }
}
