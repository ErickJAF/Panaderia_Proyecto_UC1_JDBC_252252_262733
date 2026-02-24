package negocio.BOs;

import negocio.DTOs.PedidoExpressDTO;
import negocio.excepciones.NegocioException;

/**
 * @author Isaias
 */
public interface IPedidoExpressBO {

    PedidoExpressDTO registrarPedido(PedidoExpressDTO pedido)
            throws NegocioException;

    void validarTiempoEntrega(int idPedido)
            throws NegocioException;

    void actualizarEstado(int idPedido,
                          String estado)
            throws NegocioException;

    PedidoExpressDTO buscarPorId(int idPedido)
            throws NegocioException;

    void validarPin(int idPedido,
                    String pinIngresado)
            throws NegocioException;
}