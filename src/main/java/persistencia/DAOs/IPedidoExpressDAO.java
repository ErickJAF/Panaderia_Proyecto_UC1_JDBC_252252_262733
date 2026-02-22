/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import java.time.LocalDateTime;
import java.util.List;
import negocio.DTOs.PedidoExpressDTO;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IPedidoExpressDAO {
    void insertar(PedidoExpress pedido) throws PersistenciaException;
    
    void actualizarEstado(int idPedido, String estado) throws PersistenciaException;
    
    PedidoExpress buscarPorId(int idPedido) throws PersistenciaException;
    PedidoExpressDTO buscarPorIdDTO(int idPedido) throws PersistenciaException;
    
    List<PedidoExpressDTO> obtenerPendientes() throws PersistenciaException;
    
    void actualizarFechaListo(int idPedido, LocalDateTime fecha) throws PersistenciaException;
}
