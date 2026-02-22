/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.PedidoProgramadoDTO;
import persistencia.DAOs.IPedidoProgramadoDAO;
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

    private static final Logger LOG = Logger.getLogger(PedidoProgramadoBO.class.getName());

    public PedidoProgramadoBO(IPedidoProgramadoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public void registrarPedido(PedidoProgramadoDTO dto)
            throws NegocioException {

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
            pedidoDAO.insertar(pedido);

            int idPedido = pedido.getIdPedido();

//            for (DetallePedidoDTO d : dto.getDetalles()) {
//                detalleDAO.insertar(idPedido, d);
//            }

        } catch (PersistenciaException e) {
            LOG.severe("Error al crear pedido programado");
            throw new NegocioException("No se pudo crear el pedido.", e);
        }
    }

    @Override
    public void actualizarEstado(int idPedido, String nuevoEstado) throws NegocioException {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new NegocioException("El estado no puede ser vacío");
        }

        try {
            PedidoProgramado pedido = pedidoDAO.buscarPorId(idPedido);
            if (pedido == null) {
                throw new NegocioException("No se encontró el pedido con ID " + idPedido);
            }

            String estadoActual = pedido.getEstado();

            switch (nuevoEstado) {
                case "Listo":
                    if (!estadoActual.equals("Pendiente")) {
                        throw new NegocioException("Solo los pedidos Pendientes pueden marcarse como Listo");
                    }
                    break;

                case "Entregado":
                    if (!estadoActual.equals("Listo")) {
                        throw new NegocioException("Solo los pedidos Listos pueden entregarse");
                    }
                    break;

                case "Cancelado":
                    if (estadoActual.equals("Entregado")) {
                        throw new NegocioException("No se puede cancelar un pedido ya entregado");
                    }
                    break;

                case "No Entregado":
                    if (!estadoActual.equals("Listo")) {
                        throw new NegocioException("Solo los pedidos Listos pueden pasar a No Entregado");
                    }
                    break;

                default:
                    throw new NegocioException("Estado no válido: " + nuevoEstado);
            }

            pedidoDAO.actualizarEstado(idPedido, nuevoEstado);
            LOG.info("Estado actualizado correctamente. ID: " + idPedido + ", Estado final: " + nuevoEstado);

        } catch (PersistenciaException ex) {
            LOG.severe("Error al actualizar el estado del pedido programado. ID: " + idPedido + ", Error: " + ex.getMessage());
            throw new NegocioException("Error al actualizar el estado del pedido programado", ex);
        }
    }

    @Override
    public PedidoProgramado buscarPorId(int idPedido) throws NegocioException {
        try {
            PedidoProgramado pedido = pedidoDAO.buscarPorId(idPedido);
            if (pedido == null) {
                throw new NegocioException("No se encontró el pedido con ID " + idPedido);
            }
            return pedido;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar el pedido por ID", ex);
        }
    }

    @Override
    public PedidoProgramadoDTO buscarPorIdDTO(int idPedido) throws NegocioException {

        if (idPedido <= 0) {
            throw new NegocioException("El ID del pedido no es válido");
        }

        try {

            PedidoProgramadoDTO pedido = pedidoDAO.buscarPorIdDTO(idPedido);

            if (pedido == null) {
                throw new NegocioException("No se encontró el pedido programado");
            }

            return pedido;

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar el pedido programado", ex);
        }
    }
}
