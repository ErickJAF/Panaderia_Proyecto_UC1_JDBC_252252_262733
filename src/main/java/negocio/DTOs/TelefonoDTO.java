/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.DTOs;

/**
 *
 * @author josed
 */
public class TelefonoDTO {

    private Integer idTelefono;
    private String numero;
    private String tipo; // Casa, Trabajo, Personal

    public TelefonoDTO() {
    }

    public TelefonoDTO(String numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
    }

    public TelefonoDTO(Integer idTelefono, String numero, String tipo) {
        this.idTelefono = idTelefono;
        this.numero = numero;
        this.tipo = tipo;
    }

    public Integer getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(Integer idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}