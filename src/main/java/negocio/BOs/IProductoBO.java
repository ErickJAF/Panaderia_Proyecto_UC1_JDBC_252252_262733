/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.DTOs.ProductoDTO;
import java.util.List;
import negocio.excepciones.NegocioException;

/**
 * Interfaz que define las operaciones de la lógica de negocio para los productos.
 *
 * @author icoro
 */
public interface IProductoBO {
    
    /**
     * Obtiene una lista con todos los productos que se encuentran disponibles.
     * * @return Una lista de objetos {@link ProductoDTO} que representan los productos disponibles.
     * @throws NegocioException Si ocurre un error en la capa de negocio al intentar recuperar la información.
     */
    List<ProductoDTO> obtenerDisponibles() throws NegocioException;
}