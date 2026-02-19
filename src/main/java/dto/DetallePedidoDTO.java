/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author icoro
 */
public class DetallePedidoDTO {
       private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private String nota;

    public DetallePedidoDTO(int idProducto, int cantidad, double precioUnitario, String nota) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.nota = nota;
    }

    public int getIdProducto() { return idProducto; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public String getNota() { return nota; }
}
