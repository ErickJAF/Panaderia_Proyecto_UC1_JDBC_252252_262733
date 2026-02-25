package persistencia.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private int idCliente;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String calle;
    private String colonia;

    private List<String> telefonos;

    public Cliente() {
        this.telefonos = new ArrayList<>();
    }

    public Cliente(String nombreCompleto,
                   LocalDate fechaNacimiento,
                   String calle,
                   String colonia,
                   List<String> telefonos) {

        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.calle = calle;
        this.colonia = colonia;
        this.telefonos = telefonos != null
                ? telefonos
                : new ArrayList<>();
    }

    public Cliente(int idCliente,
                   String nombreCompleto,
                   LocalDate fechaNacimiento,
                   String calle,
                   String colonia,
                   List<String> telefonos) {

        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.calle = calle;
        this.colonia = colonia;
        this.telefonos = telefonos != null
                ? telefonos
                : new ArrayList<>();
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
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

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    // 🔹 Métodos útiles opcionales
    public void agregarTelefono(String telefono) {
        this.telefonos.add(telefono);
    }

    public void eliminarTelefono(String telefono) {
        this.telefonos.remove(telefono);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nombreCompleto=" + nombreCompleto +
                ", fechaNacimiento=" + fechaNacimiento +
                ", calle=" + calle +
                ", colonia=" + colonia +
                ", telefonos=" + telefonos +
                '}';
    }
}