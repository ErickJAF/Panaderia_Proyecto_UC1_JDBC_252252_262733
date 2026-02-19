/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.ProductoDTO;
import java.util.List;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public interface IProductoBO {
    List<ProductoDTO> obtenerDisponibles() throws PersistenciaException;
}
