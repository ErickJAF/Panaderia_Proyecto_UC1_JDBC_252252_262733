/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.ProductoDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IProductoDAO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public class ProductoBO implements IProductoBO{
    
    private final IProductoDAO productoDAO;
    
    public ProductoBO(IProductoDAO productoDAO){
        this.productoDAO = productoDAO;
    }
    
   @Override
public List<ProductoDTO> obtenerDisponibles() throws NegocioException {

    try {
        return productoDAO.obtenerDisponibles();
    } catch (PersistenciaException ex) {
        throw new NegocioException(
                "Error al obtener productos disponibles", ex);
    }
}
    
}
