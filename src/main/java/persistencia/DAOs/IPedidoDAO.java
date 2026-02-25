/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import java.time.LocalDate;
import java.util.List;
import negocio.DTOs.PedidoEntregaDTO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IPedidoDAO {
    List<PedidoEntregaDTO> buscarPedidosPorCliente(String nombreCliente) throws PersistenciaException;
    
    List<PedidoEntregaDTO> obtenerPedidosPorEstado(String estado) throws PersistenciaException;

    List<PedidoEntregaDTO> buscarPorTelefono(String telefono) throws PersistenciaException;

    List<PedidoEntregaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws PersistenciaException;
    
    List<PedidoEntregaDTO> buscarPorFolio(int folio) throws PersistenciaException;
    
    void generarPago(double monto, String metodoPago, int id) throws PersistenciaException;
    
    PedidoEntregaDTO buscarPorId(int idPedido) throws PersistenciaException;
    
    List<PedidoEntregaDTO> obtenerTodos() throws PersistenciaException;
}
