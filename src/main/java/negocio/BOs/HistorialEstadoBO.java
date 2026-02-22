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
 *
 * @author ERICK
 */
public class HistorialEstadoBO implements IHistorialEstadoBO{
    private final IHistorialEstadoDAO historialDAO;
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    public HistorialEstadoBO(IHistorialEstadoDAO historialDAO) {
        this.historialDAO = historialDAO;
    }

    /**
     * Obtiene el historial de cambios de estado de un pedido
     *
     * @param idPedido ID del pedido
     * @return Lista de historial de estados
     * @throws NegocioException si hay un error en la operación
     */
    @Override
    public List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws NegocioException{
        try {
            return historialDAO.obtenerHistorialPorPedido(idPedido);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener historial del pedido", ex);
        }
    }
    
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
