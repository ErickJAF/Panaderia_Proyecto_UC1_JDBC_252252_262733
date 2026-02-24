package negocio.BOs;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.PedidoExpressDTO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.excepciones.PersistenciaException;

/**
 * @author Isaias
 */
public class PedidoExpressBO implements IPedidoExpressBO {

    private final IPedidoExpressDAO pedidoDAO;
    private static final Logger LOG =
            Logger.getLogger(PedidoExpressBO.class.getName());

    public PedidoExpressBO(IPedidoExpressDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    // 🔐 Generar PIN 8 dígitos
    private String generarPin() {
        int numero = (int)(Math.random() * 90000000) + 10000000;
        return String.valueOf(numero);
    }

    // 🧾 Registrar pedido (SIN recibir idEmpleado)
    @Override
    public PedidoExpressDTO registrarPedido(PedidoExpressDTO pedido)
            throws NegocioException {

        if (pedido.getTotal() <= 0) {
            throw new NegocioException(
                    "El total del pedido debe ser mayor a cero.");
        }

        try {

            // 🔹 Obtener empleado automáticamente
            int idEmpleado = pedidoDAO.obtenerEmpleadoDisponible();

            if (idEmpleado <= 0) {
                throw new NegocioException(
                        "No hay empleados disponibles.");
            }

            int folio = pedidoDAO.obtenerSiguienteFolio();
            String pin = generarPin();

            pedido.setFolio(folio);
            pedido.setPin(pin);
            pedido.setEstado("Pendiente");
            pedido.setFechaCreacion(LocalDateTime.now());

            pedidoDAO.insertar(pedido, idEmpleado);

            LOG.info("Pedido express registrado. Folio: " + folio);

            return pedido;

        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE,
                    "Error al registrar pedido express", ex);
            throw new NegocioException(
                    "No se pudo registrar el pedido express.", ex);
        }
    }

    // ⏱ Validar 20 minutos usando fechaListo
    @Override
    public void validarTiempoEntrega(int idPedido)
            throws NegocioException {

        try {

            PedidoExpressDTO pedido =
                    pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new NegocioException(
                        "Pedido no encontrado.");
            }

            if (!"Listo".equals(pedido.getEstado())) {
                return;
            }

            if (pedido.getFechaListo() == null) {
                return;
            }

            Duration tiempo =
                    Duration.between(
                            pedido.getFechaListo(),
                            LocalDateTime.now());

            if (tiempo.toMinutes() > 20) {

                pedidoDAO.actualizarEstado(
                        idPedido, "No Entregado");

                throw new NegocioException(
                        "El pedido superó los 20 minutos y fue marcado como No Entregado.");
            }

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al validar tiempo.", ex);
        }
    }

    // 🔄 Actualizar estado con reglas correctas
    @Override
    public void actualizarEstado(int idPedido,
                                 String nuevoEstado)
            throws NegocioException {

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            throw new NegocioException(
                    "El estado no puede ser vacío");
        }

        try {

            PedidoExpressDTO pedido =
                    pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new NegocioException(
                        "No se encontró el pedido.");
            }

            String actual = pedido.getEstado();

            switch (nuevoEstado) {

                case "Listo":
                    if (!"Pendiente".equals(actual)) {
                        throw new NegocioException(
                                "Solo Pendiente puede pasar a Listo");
                    }

                    pedidoDAO.actualizarFechaListo(
                            idPedido, LocalDateTime.now());
                    break;

                case "Entregado":
                    if (!"Listo".equals(actual)) {
                        throw new NegocioException(
                                "Solo Listo puede pasar a Entregado");
                    }
                    break;

                case "Cancelado":
                    if ("Entregado".equals(actual) ||
                        "No Entregado".equals(actual)) {
                        throw new NegocioException(
                                "No se puede cancelar un pedido finalizado.");
                    }
                    break;

                case "No Entregado":
                    if (!"Listo".equals(actual)) {
                        throw new NegocioException(
                                "Solo Listo puede pasar a No Entregado");
                    }
                    break;

                default:
                    throw new NegocioException(
                            "Estado inválido: " + nuevoEstado);
            }

            pedidoDAO.actualizarEstado(idPedido, nuevoEstado);

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al actualizar estado.", ex);
        }
    }

    // 🔍 Buscar
    @Override
    public PedidoExpressDTO buscarPorId(int idPedido)
            throws NegocioException {

        try {

            PedidoExpressDTO pedido =
                    pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new NegocioException(
                        "Pedido no encontrado.");
            }

            return pedido;

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al buscar pedido.", ex);
        }
    }

    // 🔐 Validar PIN
    @Override
    public void validarPin(int idPedido,
                           String pinIngresado)
            throws NegocioException {

        try {

            boolean valido =
                    pedidoDAO.validarPin(
                            idPedido, pinIngresado);

            if (!valido) {
                throw new NegocioException(
                        "PIN incorrecto.");
            }

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al validar PIN.", ex);
        }
    }
}