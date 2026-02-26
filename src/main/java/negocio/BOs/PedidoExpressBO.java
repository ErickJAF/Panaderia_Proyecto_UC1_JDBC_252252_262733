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
 * Clase de lógica de negocio encargada de gestionar los pedidos
 * en modalidad express.
 * <p>
 * Controla el registro automático del pedido, asignación de empleado,
 * generación de PIN de seguridad, validación de tiempos de entrega
 * y control de transiciones de estado.
 * </p>
 * 
 * @author Isaias
 */
public class PedidoExpressBO implements IPedidoExpressBO {

    /**
     * DAO encargado de las operaciones de persistencia
     * relacionadas con pedidos express.
     */
    private final IPedidoExpressDAO pedidoDAO;

    /**
     * Logger para el registro de eventos y errores
     * en operaciones de pedidos express.
     */
    private static final Logger LOG =
            Logger.getLogger(PedidoExpressBO.class.getName());

    /**
     * Constructor que recibe la dependencia necesaria
     * para la gestión de pedidos express.
     *
     * @param pedidoDAO DAO para operaciones de pedidos express
     */
    public PedidoExpressBO(IPedidoExpressDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    /**
     * Genera un PIN aleatorio de 8 dígitos para
     * la confirmación del pedido.
     *
     * @return Cadena con el PIN generado
     */
    private String generarPin() {
        int numero = (int)(Math.random() * 90000000) + 10000000;
        return String.valueOf(numero);
    }

    /**
     * Registra un nuevo pedido express en el sistema.
     * <p>
     * Valida que el total sea mayor a cero, obtiene un empleado disponible,
     * genera un folio y PIN automáticamente, establece estado inicial
     * y fecha de creación, y finalmente guarda el pedido.
     * </p>
     *
     * @param pedido Objeto PedidoExpressDTO con la información del pedido
     * @return PedidoExpressDTO registrado con datos actualizados
     * @throws NegocioException Si no hay empleados disponibles,
     *                          el total es inválido o ocurre un error en persistencia
     */
    @Override
    public PedidoExpressDTO registrarPedido(PedidoExpressDTO pedido)
            throws NegocioException {

        if (pedido.getTotal() <= 0) {
            throw new NegocioException(
                    "El total del pedido debe ser mayor a cero.");
        }

        try {

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

    /**
     * Valida si el pedido ha superado el tiempo máximo de entrega.
     * <p>
     * Si el pedido está en estado "Listo" y han pasado más de
     * 20 minutos desde que fue marcado como listo, se actualiza
     * automáticamente a "No Entregado".
     * </p>
     *
     * @param idPedido Identificador del pedido
     * @throws NegocioException Si ocurre un error en persistencia
     */
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
            }

        } catch (PersistenciaException ex) {
            throw new NegocioException(
                    "Error al validar tiempo.", ex);
        }
    }

    /**
     * Actualiza el estado de un pedido express validando
     * las transiciones permitidas entre estados.
     *
     * @param idPedido Identificador del pedido
     * @param nuevoEstado Nuevo estado a asignar
     * @throws NegocioException Si la transición es inválida
     *                          o ocurre un error en persistencia
     */
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

    /**
     * Busca un pedido express por su identificador.
     *
     * @param idPedido Identificador del pedido
     * @return PedidoExpressDTO correspondiente
     * @throws NegocioException Si el pedido no existe
     *                          o ocurre un error en persistencia
     */
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

    /**
     * Valida que el PIN ingresado coincida con el registrado
     * para el pedido express.
     *
     * @param idPedido Identificador del pedido
     * @param pinIngresado PIN proporcionado por el usuario
     * @throws NegocioException Si el PIN es incorrecto
     *                          o ocurre un error en persistencia
     */
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