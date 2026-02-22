/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.PedidoExpressDTO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class PedidoExpressBO implements IPedidoExpressBO {

    private final IPedidoExpressDAO pedidoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoExpressBO.class.getName());

    public PedidoExpressBO(IPedidoExpressDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public void registrarPedido(PedidoExpress pedido) throws NegocioException {
        LOG.info("Registrando pedido express...");
        if (pedido.getTotal() <= 0) {
            throw new NegocioException("El total del pedido debe ser mayor a cero.");
        }
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstado("Pendiente");
        try {
            pedidoDAO.insertar(pedido);
            LOG.info("Pedido express registrado con éxito. ID: " + pedido.getIdPedido());
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al registrar pedido express", ex);
            throw new NegocioException("No se pudo registrar el pedido express.", ex);
        }
    }

    @Override
    public void validarTiempoEntrega(PedidoExpress pedido) throws NegocioException {
        LOG.info("Validando tiempo de entrega del pedido...");

        if (pedido.getFechaListo() == null) {
            throw new NegocioException("El pedido aún no tiene fecha de listo.");
        }

        if (!pedido.getEstado().equals("Listo")) {
            LOG.info("Pedido no está en estado Listo, no se aplica la validación de tiempo.");
            return;
        }

        Duration tiempoTranscurrido = Duration.between(pedido.getFechaListo(), LocalDateTime.now());
        if (tiempoTranscurrido.toMinutes() > 20) {
            LOG.warning("El pedido ha superado el tiempo estimado de entrega. Marcando como No Entregado...");
            try {
                pedidoDAO.actualizarEstado(pedido.getIdPedido(), "No Entregado");
            } catch (PersistenciaException ex) {
                LOG.log(Level.SEVERE, "Error al actualizar el estado a No Entregado", ex);
                throw new NegocioException("No se pudo actualizar el estado a No Entregado", ex);
            }
            throw new NegocioException("El pedido ha superado el tiempo estimado de entrega y se marcó como No Entregado.");
        }

        LOG.info("Tiempo de entrega dentro de lo permitido.");
    }

    @Override
    public void actualizarEstado(int idPedido, String nuevoEstado) throws NegocioException {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new NegocioException("El estado no puede ser vacío");
        }

        try {
            PedidoExpress pedido = pedidoDAO.buscarPorId(idPedido);
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
                    if (estadoActual.equals("Entregado") || estadoActual.equals("No Entregado")) {
                        throw new NegocioException("No se puede cancelar un pedido ya entregado o no entregado");
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
            if (nuevoEstado.equals("Listo")) {
                pedidoDAO.actualizarFechaListo(idPedido, LocalDateTime.now());
            }
            pedidoDAO.actualizarEstado(idPedido, nuevoEstado);
            LOG.info("Estado actualizado correctamente. ID: " + idPedido + ", Estado final: " + nuevoEstado);
            
        } catch (PersistenciaException ex) {
            LOG.severe("Error al actualizar el estado del pedido express. ID: " + idPedido + ", Error: " + ex.getMessage());
            throw new NegocioException("Error al actualizar el estado del pedido", ex);
        }
    }

    @Override
    public PedidoExpress buscarPorId(int idPedido) throws NegocioException {
        try {
            PedidoExpress pedido = pedidoDAO.buscarPorId(idPedido);
            if (pedido == null) {
                throw new NegocioException("No se encontró el pedido con ID " + idPedido);
            }
            return pedido;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar el pedido por ID", ex);
        }
    }

    @Override
    public PedidoExpressDTO buscarPorIdDTO(int idPedido) throws NegocioException {

        if (idPedido <= 0) {
            throw new NegocioException("El id del pedido es inválido.");
        }

        try {

            PedidoExpressDTO pedido = pedidoDAO.buscarPorIdDTO(idPedido);

            if (pedido == null) {
                throw new NegocioException("No se encontró el pedido express con id: " + idPedido);
            }

            return pedido;

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar el pedido express.", ex);
        }
    }
}
