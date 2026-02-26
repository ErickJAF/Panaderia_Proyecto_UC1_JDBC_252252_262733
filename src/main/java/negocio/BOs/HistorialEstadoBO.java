/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.HistorialEstadoDTO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IHistorialEstadoDAO;
import persistencia.dominio.HistorialEstado;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase de lógica de negocio encargada de gestionar las operaciones
 * relacionadas con el historial de estados de los pedidos.
 * <p>
 * Permite consultar información histórica de los pedidos aplicando
 * distintos filtros como estado, teléfono, rango de fechas o folio.
 * Además, controla validaciones básicas antes de delegar la operación
 * a la capa de persistencia.
 * </p>
 * 
 * @author ERICK
 */
public class HistorialEstadoBO implements IHistorialEstadoBO{

    /**
     * DAO encargado de las operaciones de persistencia del historial de estados.
     */
    private final IHistorialEstadoDAO historialDAO;

    /**
     * Logger para el registro de eventos y errores relacionados
     * con las operaciones del historial de estados.
     */
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    /**
     * Constructor que recibe la dependencia necesaria para la gestión
     * del historial de estados.
     *
     * @param historialDAO DAO para operaciones del historial de estados
     */
    public HistorialEstadoBO(IHistorialEstadoDAO historialDAO) {
        this.historialDAO = historialDAO;
    }

    /**
     * Obtiene el historial de cambios de estado de un pedido específico.
     *
     * @param idPedido ID del pedido
     * @return Lista de objetos HistorialEstado asociados al pedido
     * @throws NegocioException Si ocurre un error en la capa de persistencia
     */
    @Override
    public List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws NegocioException{
        try {
            return historialDAO.obtenerHistorialPorPedido(idPedido);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener historial del pedido", ex);
        }
    }
    
    /**
     * Obtiene los pedidos que se encuentran en un estado específico.
     *
     * @param estado Estado por el cual se filtrarán los pedidos
     * @return Lista de objetos HistorialEstadoDTO que coinciden con el estado
     * @throws NegocioException Si el estado es inválido o ocurre un error
     *                          en la capa de persistencia
     */
    @Override
    public List<HistorialEstadoDTO> obtenerPedidosPorEstado(String estado) throws NegocioException {
        if (estado == null || estado.isBlank()) {
            throw new NegocioException("El estado no puede estar vacío");
        }

        try {
            return historialDAO.obtenerPedidosPorEstado(estado);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en obtenerPedidosPorEstado", ex);
            throw new NegocioException("No se pudieron obtener los pedidos por estado", ex);
        }
    }

    /**
     * Busca pedidos asociados a un número de teléfono específico.
     *
     * @param telefono Número de teléfono para filtrar los pedidos
     * @return Lista de objetos HistorialEstadoDTO relacionados con el teléfono
     * @throws NegocioException Si el teléfono es inválido o ocurre un error
     *                          en la capa de persistencia
     */
    @Override
    public List<HistorialEstadoDTO> buscarPorTelefono(String telefono) throws NegocioException {
        if (telefono == null || telefono.isBlank()) {
            throw new NegocioException("El teléfono no puede estar vacío");
        }

        try {
            return historialDAO.buscarPorTelefono(telefono);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en buscarPorTelefono", ex);
            throw new NegocioException("No se pudieron obtener los pedidos por teléfono", ex);
        }
    }

    /**
     * Busca pedidos dentro de un rango de fechas específico.
     *
     * @param inicio Fecha inicial del rango
     * @param fin Fecha final del rango
     * @return Lista de objetos HistorialEstadoDTO dentro del rango indicado
     * @throws NegocioException Si las fechas son inválidas o ocurre un error
     *                          en la capa de persistencia
     */
    @Override
    public List<HistorialEstadoDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException {
        if (inicio == null || fin == null) {
            throw new NegocioException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (inicio.isAfter(fin)) {
            throw new NegocioException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        try {
            return historialDAO.buscarPorRangoFechas(inicio, fin);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en buscarPorRangoFechas", ex);
            throw new NegocioException("No se pudieron obtener los pedidos por rango de fechas", ex);
        }
    }

    /**
     * Busca pedidos mediante su folio identificador.
     *
     * @param folio Número de folio del pedido
     * @return Lista de objetos HistorialEstadoDTO asociados al folio
     * @throws NegocioException Si ocurre un error en la capa de persistencia
     */
    @Override
    public List<HistorialEstadoDTO> buscarPorFolio(int folio) throws NegocioException {
        try {
            return historialDAO.buscarPorFolio(folio);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al buscar por folio", ex);
            throw new NegocioException("Error al buscar por folio", ex);
        }
    }
}