/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import java.util.List;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IPedidoProgramadoDAO {
    int obtenerUltimoNumeroPedido() throws PersistenciaException;

    void insertar(PedidoProgramado pedido) throws PersistenciaException;

    List<PedidoProgramado> obtenerActivosPorCliente(int idCliente) throws PersistenciaException;

    List<PedidoProgramado> obtenerPorTelefono(String telefono) throws PersistenciaException;

    void actualizarEstado(int idPedido, String estado) throws PersistenciaException;
    
    List<PedidoProgramado> obtenerPendientes() throws PersistenciaException;
}
