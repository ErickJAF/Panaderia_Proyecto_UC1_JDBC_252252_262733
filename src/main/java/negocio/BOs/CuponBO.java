/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.ICuponDAO;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author josed
 */
public class CuponBO {

    private ICuponDAO cuponDAO;

    public CuponBO(ICuponDAO cuponDAO) {
        this.cuponDAO = cuponDAO;
    }

    public void aplicarUso(Long idCupon) throws NegocioException {
        try {
            cuponDAO.incrementarUso(idCupon);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo actualizar el uso del cupón.", e);
        }
    }
}
