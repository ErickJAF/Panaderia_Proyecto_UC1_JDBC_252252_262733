/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import java.util.List;
import negocio.DTOs.PedidoProgramadoDTO;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IPedidoProgramadoDAO {
    PedidoProgramado buscarPorId(int idPedido) throws PersistenciaException;
    PedidoProgramadoDTO buscarPorIdDTO(int idPedido) throws PersistenciaException;
    
    void insertar(PedidoProgramado pedido) throws PersistenciaException;

    List<PedidoProgramado> obtenerActivosPorCliente(int idCliente) throws PersistenciaException;

    void actualizarEstado(int idPedido, String estado) throws PersistenciaException;
    
    List<PedidoProgramado> obtenerPendientes() throws PersistenciaException;
}
