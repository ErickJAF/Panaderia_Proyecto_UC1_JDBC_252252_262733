/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import java.util.List;
import negocio.DTOs.PedidoProgramadoDTO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.PedidoProgramado;

/**
 * Interfaz que define las operaciones de negocio relacionadas
 * con la gestión de pedidos programados.
 * <p>
 * Establece los métodos necesarios para registrar pedidos,
 * actualizar su estado, consultar información, aplicar descuentos,
 * validar límites y gestionar cancelaciones.
 * </p>
 * 
 * @author icoro
 */
public interface IPedidoProgramadoBO {

    /**
     * Registra un nuevo pedido programado en el sistema.
     *
     * @param pedido Objeto PedidoProgramadoDTO con la información del pedido
     * @throws NegocioException Si ocurre un error en las validaciones
     *                          o durante el registro
     */
    void registrarPedido(PedidoProgramadoDTO pedido) throws NegocioException;

    /**
     * Actualiza el estado actual de un pedido programado.
     *
     * @param idPedido Identificador del pedido
     * @param estado Nuevo estado que se asignará al pedido
     * @throws NegocioException Si ocurre un error durante la actualización
     */
    void actualizarEstado(int idPedido, String estado) throws NegocioException;
    
    /**
     * Busca un pedido programado por su identificador.
     *
     * @param idPedido Identificador único del pedido
     * @return Objeto PedidoProgramado correspondiente al ID proporcionado
     * @throws NegocioException Si el pedido no existe o ocurre un error en la consulta
     */
    PedidoProgramado buscarPorId(int idPedido) throws NegocioException;

    /**
     * Busca un pedido programado por su identificador y lo devuelve
     * en formato DTO.
     *
     * @param idPedido Identificador único del pedido
     * @return Objeto PedidoProgramadoDTO correspondiente al ID proporcionado
     * @throws NegocioException Si el pedido no existe o ocurre un error en la consulta
     */
    PedidoProgramadoDTO buscarPorIdDTO(int idPedido) throws NegocioException;

    /**
     * Calcula el descuento aplicable a un pedido según el código de cupón.
     *
     * @param codigoCupon Código del cupón a aplicar
     * @param subtotal Monto subtotal del pedido
     * @return Monto del descuento calculado
     * @throws NegocioException Si el cupón es inválido o ocurre un error en el cálculo
     */
    double calcularDescuento(String codigoCupon, double subtotal) throws NegocioException;

    /**
     * Obtiene el historial de pedidos programados de un cliente específico.
     *
     * @param idCliente Identificador del cliente
     * @return Lista de objetos PedidoProgramadoDTO asociados al cliente
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<PedidoProgramadoDTO> obtenerHistorialCliente(int idCliente) throws NegocioException;

    /**
     * Valida que un cliente no haya excedido el límite permitido
     * de pedidos programados.
     *
     * @param idCliente Identificador del cliente
     * @throws NegocioException Si el cliente supera el límite establecido
     */
    void validarLimitePedidos(int idCliente) throws NegocioException;

    /**
     * Cancela un pedido programado existente.
     *
     * @param idPedido Identificador del pedido a cancelar
     * @throws NegocioException Si ocurre un error durante la cancelación
     */
    void cancelarPedido(int idPedido) throws NegocioException;
}