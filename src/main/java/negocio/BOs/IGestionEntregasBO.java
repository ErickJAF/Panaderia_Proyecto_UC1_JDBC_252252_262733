/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import negocio.excepciones.NegocioException;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IGestionEntregasBO {
    public void cambiarEstadoPedido(int idPedido, String nuevoEstado) throws NegocioException;
}
