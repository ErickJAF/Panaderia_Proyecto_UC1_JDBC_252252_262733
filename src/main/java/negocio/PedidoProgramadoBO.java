/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import dto.PedidoProgramadoDTO;
import persistencia.DAOs.PedidoProgramadoDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;

/**
 *
 * @author icoro
 */
public class PedidoProgramadoBO {
    
    private final PedidoProgramadoDAO pedidoDAO;

    public PedidoProgramadoBO(IConexionBD conexionBD) {
        this.pedidoDAO = new PedidoProgramadoDAO(conexionBD);
    }

    public void crearPedidoProgramado(PedidoProgramadoDTO dto)
            throws PersistenciaException {

        //validaciones
        if (dto.getSubtotal() < 0)
            throw new IllegalArgumentException("El subtotal no puede ser negativo");

        if (dto.getTotal() < 0)
            throw new IllegalArgumentException("El total no puede ser negativo");

        if (dto.getIdCliente() <= 0)
            throw new IllegalArgumentException("Cliente inválido");

        if (dto.getIdEmpleado() <= 0)
            throw new IllegalArgumentException("Empleado inválido");

        // construir entidad
        PedidoProgramado pedido = new PedidoProgramado();
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedido.setSubtotal(dto.getSubtotal());
        pedido.setDescuento((float) dto.getDescuento());
        pedido.setTotal(dto.getTotal());
        pedido.setIdEmpleado(dto.getIdEmpleado());
        pedido.setIdCliente(dto.getIdCliente());
        pedido.setIdCupon(dto.getIdCupon());

        // llamar al DAO
        pedidoDAO.insertar(pedido);
    }
}
