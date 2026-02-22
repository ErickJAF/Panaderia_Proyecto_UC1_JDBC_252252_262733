/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.DetallePedidoDTO;
import negocio.DTOs.PedidoProgramadoDTO;
import persistencia.DAOs.IDetallePedidoDAO;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.DAOs.DetallePedidoDAO;
import persistencia.DAOs.PedidoProgramadoDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import java.time.LocalDate;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public class PedidoProgramadoBO implements IPedidoProgramadoBO{
    
    private final IPedidoProgramadoDAO pedidoDAO;
    private final IDetallePedidoDAO detalleDAO;

    private static final Logger LOG =
            Logger.getLogger(PedidoProgramadoBO.class.getName());

    public PedidoProgramadoBO(IConexionBD conexionBD) {
        this.pedidoDAO = new PedidoProgramadoDAO(conexionBD);
        this.detalleDAO = new DetallePedidoDAO(conexionBD);
    }

    @Override
    public void crearPedidoProgramado(PedidoProgramadoDTO dto)
            throws NegocioException {

        validarDTO(dto);

        PedidoProgramado pedido = new PedidoProgramado();

        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedido.setSubtotal(dto.getSubtotal());
        pedido.setDescuento((float) dto.getDescuento());
        pedido.setTotal(dto.getTotal());
        pedido.setIdEmpleado(dto.getIdEmpleado());
        pedido.setIdCliente(dto.getIdCliente());
        pedido.setIdCupon(dto.getIdCupon());

        try {
            // Inserta pedido y obtiene ID generado
            pedidoDAO.insertar(pedido);

            int idPedido = pedido.getIdPedido();

            // Insertar detalles
            for (DetallePedidoDTO d : dto.getDetalles()) {
                detalleDAO.insertar(idPedido, d);
            }

        } catch (PersistenciaException e) {
            LOG.severe("Error al crear pedido programado");
            throw new NegocioException("No se pudo crear el pedido.", e);
        }
    }

    @Override
    public void cambiarEstado(int idPedido, String nuevoEstado)
            throws NegocioException {

        if (idPedido <= 0)
            throw new NegocioException("ID de pedido inválido.");

        if (nuevoEstado == null || nuevoEstado.isBlank())
            throw new NegocioException("Estado inválido.");

        try {
            pedidoDAO.actualizarEstado(idPedido, nuevoEstado);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo cambiar el estado.", e);
        }
    }

    @Override
    public void cancelarPedido(int idPedido) throws NegocioException {

        if (idPedido <= 0)
            throw new NegocioException("ID inválido.");

        try {
            pedidoDAO.actualizarEstado(idPedido, "Cancelado");
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo cancelar el pedido.", e);
        }
    }

    private void validarDTO(PedidoProgramadoDTO dto)
            throws NegocioException {

        if (dto.getSubtotal() < 0)
            throw new NegocioException("Subtotal inválido.");

        if (dto.getTotal() < 0)
            throw new NegocioException("Total inválido.");

        if (dto.getIdCliente() <= 0)
            throw new NegocioException("Cliente inválido.");

        if (dto.getIdEmpleado() <= 0)
            throw new NegocioException("Empleado inválido.");

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty())
            throw new NegocioException("El pedido debe tener al menos un detalle.");
    }
}
