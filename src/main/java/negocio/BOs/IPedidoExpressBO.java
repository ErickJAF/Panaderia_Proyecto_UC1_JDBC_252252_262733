/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.excepciones.NegocioException;
import persistencia.dominio.PedidoExpress;

/**
 *
 * @author ERICK
 */
public interface IPedidoExpressBO {
    void registrarPedido(PedidoExpress pedido) throws NegocioException;

    void validarTiempoEntrega(PedidoExpress pedido) throws NegocioException;

    void entregarPedido(PedidoExpress pedido) throws NegocioException;

    void cancelarPedido(int idPedido) throws NegocioException;
}
