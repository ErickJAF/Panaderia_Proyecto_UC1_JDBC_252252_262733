/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.ProductoDTO;
import java.util.List;
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
    
    public List<ProductoDTO> obtenerDisponibles() throws PersistenciaException {
       
        return productoDAO.obtenerDisponibles();
    }
    
}
