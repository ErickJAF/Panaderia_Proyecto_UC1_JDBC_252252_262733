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
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.DAOs.PedidoExpressDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class PedidoExpressBO implements IPedidoExpressBO {

    private final IPedidoExpressDAO pedidoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoExpressBO.class.getName());

    public PedidoExpressBO(IConexionBD conexionBD) {
        this.pedidoDAO = new PedidoExpressDAO(conexionBD);
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

        Duration tiempoTranscurrido = Duration.between(pedido.getFechaListo(), LocalDateTime.now());
        if (tiempoTranscurrido.toMinutes() > 30) { // ejemplo: 30 min máximo para entrega
            throw new NegocioException("El pedido ya ha superado el tiempo estimado de entrega.");
        }
        LOG.info("Tiempo de entrega dentro de lo permitido.");
    }

    @Override
    public void entregarPedido(PedidoExpress pedido) throws NegocioException {
        LOG.info("Marcando pedido como entregado...");
        if (pedido.estaFinalizado()) {
            throw new NegocioException("El pedido ya fue finalizado o cancelado.");
        }
        pedido.setEstado("Entregado");
        try {
            pedidoDAO.actualizarEstado(pedido.getIdPedido(), pedido.getEstado());
            LOG.info("Pedido express entregado correctamente.");
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al entregar pedido", ex);
            throw new NegocioException("No se pudo marcar el pedido como entregado.", ex);
        }
    }

    @Override
    public void cancelarPedido(int idPedido) throws NegocioException {
        LOG.info("Cancelando pedido express con ID: " + idPedido);
        try {
            pedidoDAO.actualizarEstado(idPedido, "Cancelado");
            LOG.info("Pedido express cancelado correctamente.");
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al cancelar pedido", ex);
            throw new NegocioException("No se pudo cancelar el pedido express.", ex);
        }
    }
}
