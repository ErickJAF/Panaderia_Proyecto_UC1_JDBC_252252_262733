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
 *
 * @author josed
 */
public class CuponBO implements ICuponBO{

    private final ICuponDAO cuponDAO;
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    public CuponBO(ICuponDAO cuponDAO) {
        this.cuponDAO = cuponDAO;
    }

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
