package negocio.BOs;

import negocio.DTOs.PedidoExpressDTO;
import negocio.excepciones.NegocioException;

/**
 * Interfaz que define las operaciones de negocio relacionadas
 * con la gestión de pedidos en modalidad express.
 * <p>
 * Establece los métodos necesarios para registrar pedidos express,
 * validar tiempos de entrega, actualizar su estado, consultar
 * información específica y verificar el PIN de confirmación.
 * </p>
 * 
 * @author Isaias
 */
public interface IPedidoExpressBO {

    /**
     * Registra un nuevo pedido en modalidad express.
     *
     * @param pedido Objeto PedidoExpressDTO con la información del pedido
     * @return PedidoExpressDTO registrado con la información actualizada
     * @throws NegocioException Si ocurre un error en las validaciones
     *                          o durante el registro
     */
    PedidoExpressDTO registrarPedido(PedidoExpressDTO pedido)
            throws NegocioException;

    /**
     * Valida que el tiempo de entrega de un pedido express
     * cumpla con las reglas de negocio establecidas.
     *
     * @param idPedido Identificador del pedido
     * @throws NegocioException Si el tiempo de entrega no es válido
     *                          o ocurre un error en la validación
     */
    void validarTiempoEntrega(int idPedido)
            throws NegocioException;

    /**
     * Actualiza el estado actual de un pedido express.
     *
     * @param idPedido Identificador del pedido
     * @param estado Nuevo estado que se asignará al pedido
     * @throws NegocioException Si ocurre un error durante la actualización
     */
    void actualizarEstado(int idPedido,
                          String estado)
            throws NegocioException;

    /**
     * Busca un pedido express por su identificador.
     *
     * @param idPedido Identificador único del pedido
     * @return PedidoExpressDTO correspondiente al ID proporcionado
     * @throws NegocioException Si el pedido no existe o ocurre un error en la consulta
     */
    PedidoExpressDTO buscarPorId(int idPedido)
            throws NegocioException;

    /**
     * Valida el PIN ingresado para confirmar o autorizar
     * una operación sobre un pedido express.
     *
     * @param idPedido Identificador del pedido
     * @param pinIngresado PIN proporcionado para validación
     * @throws NegocioException Si el PIN es incorrecto o ocurre un error
     *                          durante el proceso de validación
     */
    void validarPin(int idPedido,
                    String pinIngresado)
            throws NegocioException;
}