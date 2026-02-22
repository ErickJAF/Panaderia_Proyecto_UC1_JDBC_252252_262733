/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IPedidoExpressDAO {
    void insertar(PedidoExpress pedido) throws PersistenciaException;
    void actualizarEstado(int idPedido, String estado) throws PersistenciaException;
    PedidoExpress buscarPorId(int idPedido) throws PersistenciaException;
}
