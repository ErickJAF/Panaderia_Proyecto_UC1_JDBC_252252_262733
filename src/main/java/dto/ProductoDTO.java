/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author icoro
 */
public class ProductoDTO {
     private int idProducto;
    private String nombre;
    private double precio;

    public ProductoDTO(int idProducto, String nombre, double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
}
