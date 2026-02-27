/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.excepciones.NegocioException;

/**
 * Interfaz que define las operaciones de negocio relacionadas
 * con la gestión de cupones dentro del sistema.
 * <p>
 * Establece el contrato que deben cumplir las clases encargadas
 * de aplicar el uso de cupones.
 * </p>
 * 
 * @author ERICK
 */
public interface ICuponBO {

    /**
     * Registra el uso de un cupón incrementando su contador.
     *
     * @param idCupon Identificador único del cupón
     * @throws NegocioException Si ocurre un error en la operación
     *                          o en la capa de persistencia
     */
    void aplicarUso(Long idCupon) throws NegocioException;
}