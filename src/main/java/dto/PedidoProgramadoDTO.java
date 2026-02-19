/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author icoro
 */
public class PedidoProgramadoDTO {
     private int idCliente;
    private int idEmpleado;
    private double subtotal;
    private double descuento;
    private double total;
    private Integer idCupon;

    public PedidoProgramadoDTO(int idCliente, int idEmpleado,
                               double subtotal, double descuento,
                               double total, Integer idCupon) {
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.idCupon = idCupon;
    }

    public int getIdCliente() { return idCliente; }
    public int getIdEmpleado() { return idEmpleado; }
    public double getSubtotal() { return subtotal; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public Integer getIdCupon() { return idCupon; }
}
