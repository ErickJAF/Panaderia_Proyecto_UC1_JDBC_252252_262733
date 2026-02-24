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

    public Cupon validarCuponPorId(Integer idCupon) throws NegocioException {

    if (idCupon == null) {
        return null;
    }

    Cupon cupon;

    try {
        cupon = cuponDAO.buscarPorId(Long.valueOf(idCupon));
    } catch (PersistenciaException e) {
        throw new NegocioException("Error al validar el cupón.", e);
    }

    if (cupon == null) {
        throw new NegocioException("El cupón no existe.");
    }

    if (!cupon.isActivo()) {
        throw new NegocioException("El cupón no está activo.");
    }

    LocalDate hoy = LocalDate.now();

    if (hoy.isBefore(cupon.getFechaInicio()) || hoy.isAfter(cupon.getFechaFin())) {
        throw new NegocioException("El cupón no está vigente.");
    }

    if (cupon.getUsosActuales() >= cupon.getUsosMaximos()) {
        throw new NegocioException("El cupón ya alcanzó su límite de usos.");
    }

    return cupon;
}

    public void aplicarUso(Long idCupon) throws NegocioException {
        try {
            cuponDAO.incrementarUso(idCupon);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo actualizar el uso del cupón.", e);
        }
    }
}
