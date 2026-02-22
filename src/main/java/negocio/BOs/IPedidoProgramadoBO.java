/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

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
}
