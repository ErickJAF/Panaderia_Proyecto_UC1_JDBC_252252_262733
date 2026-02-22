/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import java.util.List;
import negocio.DTOs.HistorialEstadoDTO;
import negocio.excepciones.NegocioException;
import persistencia.dominio.HistorialEstado;

/**
 *
 * @author ERICK
 */
public interface IHistorialEstadoBO {
    List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws NegocioException;
    
    List<HistorialEstadoDTO> obtenerPedidosPorEstado(String estado) throws NegocioException;

    List<HistorialEstadoDTO> buscarPorTelefono(String telefono) throws NegocioException;

    List<HistorialEstadoDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException;
    
    List<HistorialEstadoDTO> buscarPorFolio(int folio) throws NegocioException;
}
