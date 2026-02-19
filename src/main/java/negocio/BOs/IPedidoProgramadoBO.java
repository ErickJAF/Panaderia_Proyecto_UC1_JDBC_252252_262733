/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.PedidoProgramadoDTO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public interface IPedidoProgramadoBO {
    
     void crearPedidoProgramado(PedidoProgramadoDTO dto)
            throws PersistenciaException;
}
