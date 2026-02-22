/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import java.time.LocalDate;
import java.util.List;
import negocio.DTOs.HistorialEstadoDTO;
import persistencia.dominio.HistorialEstado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IHistorialEstadoDAO {
    List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws PersistenciaException;
    
    List<HistorialEstadoDTO> obtenerPedidosPorEstado(String estado) throws PersistenciaException;

    List<HistorialEstadoDTO> buscarPorTelefono(String telefono) throws PersistenciaException;

    List<HistorialEstadoDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws PersistenciaException;
    
    List<HistorialEstadoDTO> buscarPorFolio(int folio) throws PersistenciaException;
}
