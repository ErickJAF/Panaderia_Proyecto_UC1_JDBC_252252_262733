/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.ICuponDAO;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase de lógica de negocio encargada de gestionar las operaciones
 * relacionadas con los cupones del sistema.
 * <p>
 * Se encarga de coordinar la interacción con la capa de persistencia
 * y manejar posibles errores al aplicar el uso de un cupón.
 * </p>
 * 
 * @author josed
 */
public class CuponBO implements ICuponBO{

    /**
     * DAO encargado de las operaciones de persistencia de cupones.
     */
    private final ICuponDAO cuponDAO;

    /**
     * Logger para el registro de eventos y errores relacionados con cupones.
     */
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    /**
     * Constructor que recibe la dependencia necesaria para la gestión
     * de cupones.
     *
     * @param cuponDAO DAO para operaciones de cupones
     */
    public CuponBO(ICuponDAO cuponDAO) {
        this.cuponDAO = cuponDAO;
    }

    /**
     * Aplica el uso de un cupón incrementando su contador de usos.
     *
     * @param idCupon Identificador del cupón al cual se le registrará el uso
     * @throws NegocioException Si ocurre un error en la capa de persistencia
     */
    @Override
    public void aplicarUso(Long idCupon) throws NegocioException {
        try {
            cuponDAO.incrementarUso(idCupon);
        } catch (PersistenciaException e) {
            LOG.log(Level.SEVERE, "Error en incrementar los usos del pedido", e);
            throw new NegocioException("No se pudo actualizar el uso del cupón.", e);
        }
    }
}