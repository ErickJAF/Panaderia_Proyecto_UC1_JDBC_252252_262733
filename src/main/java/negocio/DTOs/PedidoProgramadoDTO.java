package negocio.DTOs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoProgramadoDTO {

    
    private int idPedido;
    private LocalDate fechaCreacion;
    private String estado;


    private int idCliente;
    private int idEmpleado;
    private double subtotal;
    private double descuento;
    private double total;
    private List<DetallePedidoDTO> detalles = new ArrayList<>();

    private Integer idCupon;
    private String codigoCupon;

  
    public PedidoProgramadoDTO() {
    }

  
    public PedidoProgramadoDTO(
            int idCliente,
            int idEmpleado,
            double subtotal,
            double descuento,
            double total,
            String codigoCupon,
            List<DetallePedidoDTO> detalles
    ) {
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.codigoCupon = codigoCupon;
        this.detalles = detalles;
    }

  
    public PedidoProgramadoDTO(
            int idCliente,
            int idEmpleado,
            double subtotal,
            double descuento,
            double total,
            Integer idCupon,
            List<DetallePedidoDTO> detalles
    ) {
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.total = total;
        this.idCupon = idCupon;
        this.detalles = detalles;
    }



    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

   

    public int getIdCliente() {
        return idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getTotal() {
        return total;
    }

    public Integer getIdCupon() {
        return idCupon;
    }

    public String getCodigoCupon() {
        return codigoCupon;
    }

    public List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setDetalles(List<DetallePedidoDTO> detalles) {
        this.detalles = detalles;
    }

    public void setIdCupon(Integer idCupon) {
        this.idCupon = idCupon;
    }

    public void setCodigoCupon(String codigoCupon) {
        this.codigoCupon = codigoCupon;
    }
    
}