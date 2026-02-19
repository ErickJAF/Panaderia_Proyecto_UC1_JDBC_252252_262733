/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import negocio.DTOs.DetallePedidoDTO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author icoro
 */
public interface IDetallePedidoDAO {
    void insertar(int idPedido,DetallePedidoDTO detalle)
            throws PersistenciaException;
}
