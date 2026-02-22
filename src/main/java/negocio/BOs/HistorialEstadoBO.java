/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.HistorialEstadoDAO;
import persistencia.dominio.HistorialEstado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class HistorialEstadoBO implements IHistorialEstadoBO{
    private final HistorialEstadoDAO historialDAO;

    public HistorialEstadoBO(HistorialEstadoDAO historialDAO) {
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
}
