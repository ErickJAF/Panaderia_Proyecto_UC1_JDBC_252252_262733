/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import dto.DetallePedidoDTO;
import dto.PedidoProgramadoDTO;
import persistencia.DAOs.IDetallePedidoDAO;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.DAOs.DetallePedidoDAO;
import persistencia.DAOs.PedidoProgramadoDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;

/**
 *
 * @author icoro
 */
public class PedidoProgramadoBO implements IPedidoProgramadoBO{
    
  private final IPedidoProgramadoDAO pedidoDAO;
    private final IDetallePedidoDAO detalleDAO;

    public PedidoProgramadoBO(IConexionBD conexionBD) {

        this.pedidoDAO = new PedidoProgramadoDAO(conexionBD);
        this.detalleDAO = new DetallePedidoDAO(conexionBD);
    }

    @Override
    public void crearPedidoProgramado(PedidoProgramadoDTO dto)
            throws PersistenciaException {

        if (dto.getSubtotal() < 0)
            throw new IllegalArgumentException("Subtotal inválido");

        if (dto.getTotal() < 0)
            throw new IllegalArgumentException("Total inválido");

        if (dto.getIdCliente() <= 0)
            throw new IllegalArgumentException("Cliente inválido");

        if (dto.getIdEmpleado() <= 0)
            throw new IllegalArgumentException("Empleado inválido");

        PedidoProgramado pedido = new PedidoProgramado();

        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedido.setSubtotal(dto.getSubtotal());
        pedido.setDescuento((float) dto.getDescuento());
        pedido.setTotal(dto.getTotal());
        pedido.setIdEmpleado(dto.getIdEmpleado());
        pedido.setIdCliente(dto.getIdCliente());
        pedido.setIdCupon(dto.getIdCupon());

        // INSERTA PEDIDO + PROGRAMADO (SP)
        pedidoDAO.insertar(pedido);

        int idPedido = pedido.getIdPedido();

        //  INSERTA DETALLE_PEDIDO
        for (DetallePedidoDTO d : dto.getDetalles()) {
            detalleDAO.insertar(idPedido, d);
        }
    }
}
