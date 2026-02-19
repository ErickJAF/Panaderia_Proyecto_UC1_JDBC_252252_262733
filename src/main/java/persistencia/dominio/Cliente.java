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
public class Cliente extends Usuario {

    private LocalDate fechaNacimiento;
    private String calle;
    private String colonia;
    private int numero;

    public Cliente() {
    }

    public Cliente(LocalDate fechaNacimiento, String calle, String colonia, int numero, int idUsuario, String nombreCompleto, String rol) {
        super(idUsuario, nombreCompleto, rol);
        this.fechaNacimiento = fechaNacimiento;
        this.calle = calle;
        this.colonia = colonia;
        this.numero = numero;
    }

    public Cliente(LocalDate fechaNacimiento, String calle, String colonia, int numero) {
        this.fechaNacimiento = fechaNacimiento;
        this.calle = calle;
        this.colonia = colonia;
        this.numero = numero;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getCalle() {
        return calle;
    }

    public String getColonia() {
        return colonia;
    }

    public int getNumero() {
        return numero;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Cliente{" + "fechaNacimiento=" + fechaNacimiento + ", calle=" + calle + ", colonia=" + colonia + ", numero=" + numero + '}';
    }
}
