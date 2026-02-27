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
 * Interfaz que define las operaciones de negocio relacionadas
 * con la consulta y gestión del historial de estados de los pedidos.
 * <p>
 * Establece los métodos para obtener información histórica de los pedidos
 * mediante distintos criterios de búsqueda como estado, teléfono,
 * rango de fechas y folio.
 * </p>
 * 
 * @author ERICK
 */
public interface IHistorialEstadoBO {

    /**
     * Obtiene el historial completo de cambios de estado
     * asociados a un pedido específico.
     *
     * @param idPedido Identificador único del pedido
     * @return Lista de objetos HistorialEstado relacionados con el pedido
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws NegocioException;
    
    /**
     * Obtiene los pedidos que coinciden con un estado determinado.
     *
     * @param estado Estado por el cual se filtrarán los pedidos
     * @return Lista de objetos HistorialEstadoDTO que cumplen con el estado indicado
     * @throws NegocioException Si el estado es inválido o ocurre un error en la consulta
     */
    List<HistorialEstadoDTO> obtenerPedidosPorEstado(String estado) throws NegocioException;

    /**
     * Busca pedidos asociados a un número de teléfono específico.
     *
     * @param telefono Número de teléfono para realizar la búsqueda
     * @return Lista de objetos HistorialEstadoDTO relacionados con el teléfono
     * @throws NegocioException Si el teléfono es inválido o ocurre un error en la consulta
     */
    List<HistorialEstadoDTO> buscarPorTelefono(String telefono) throws NegocioException;

    /**
     * Busca pedidos dentro de un rango de fechas determinado.
     *
     * @param inicio Fecha inicial del rango de búsqueda
     * @param fin Fecha final del rango de búsqueda
     * @return Lista de objetos HistorialEstadoDTO dentro del rango indicado
     * @throws NegocioException Si las fechas son inválidas o ocurre un error en la consulta
     */
    List<HistorialEstadoDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException;
    
    /**
     * Busca pedidos mediante su número de folio.
     *
     * @param folio Número de folio del pedido
     * @return Lista de objetos HistorialEstadoDTO asociados al folio indicado
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<HistorialEstadoDTO> buscarPorFolio(int folio) throws NegocioException;
}