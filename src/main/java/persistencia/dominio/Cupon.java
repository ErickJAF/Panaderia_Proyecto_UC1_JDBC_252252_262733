/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author ERICK
 */
public class Cupon {
    private Integer idCupon;
    private String codigo;
    private BigDecimal descuento;
    private int usosMaximos;
    private int usosActuales;
    private boolean activo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Cupon() {
    }

    public Cupon(int idCupon, String codigo, BigDecimal descuento, int usosMaximos, int usosActuales, boolean activo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idCupon = idCupon;
        this.codigo = codigo;
        this.descuento = descuento;
        this.usosMaximos = usosMaximos;
        this.usosActuales = usosActuales;
        this.activo = activo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public void setIdCupon(int idCupon) {
        this.idCupon = idCupon;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public void setUsosMaximos(int usosMaximos) {
        this.usosMaximos = usosMaximos;
    }

    public void setUsosActuales(int usosActuales) {
        this.usosActuales = usosActuales;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getIdCupon() {
        return idCupon;
    }

    public String getCodigo() {
        return codigo;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public int getUsosMaximos() {
        return usosMaximos;
    }

    public int getUsosActuales() {
        return usosActuales;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    @Override
    public String toString() {
        return "Cupon{" + "idCupon=" + idCupon + ", codigo=" + codigo + ", descuento=" + descuento + ", usosMaximos=" + usosMaximos + ", usosActuales=" + usosActuales + ", activo=" + activo + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + '}';
    }
}
