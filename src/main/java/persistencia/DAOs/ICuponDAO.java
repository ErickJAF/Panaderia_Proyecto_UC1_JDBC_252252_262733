/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author josed
 */
public interface ICuponDAO {
     Cupon buscarPorCodigo(String codigo) throws PersistenciaException;
     
     Cupon buscarPorId(Long idCupon) throws PersistenciaException;
    
     void incrementarUso(Long idCupon) throws PersistenciaException;
}

