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
 *
 * @author icoro
 */
public interface IPedidoProgramadoBO {
    void registrarPedido(PedidoProgramadoDTO pedido) throws NegocioException;

    void actualizarEstado(int idPedido, String estado) throws NegocioException;
    
    PedidoProgramado buscarPorId(int idPedido) throws NegocioException;
    PedidoProgramadoDTO buscarPorIdDTO(int idPedido) throws NegocioException;
    double calcularDescuento(String codigoCupon, double subtotal) throws NegocioException;
    List<PedidoProgramadoDTO> obtenerHistorialCliente(int idCliente) throws NegocioException;
    void validarLimitePedidos(int idCliente) throws NegocioException;
    void cancelarPedido(int idPedido) throws NegocioException;
}
