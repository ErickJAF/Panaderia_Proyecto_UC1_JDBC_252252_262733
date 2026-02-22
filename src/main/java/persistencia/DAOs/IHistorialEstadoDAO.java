/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.DAOs;

import java.util.List;
import persistencia.dominio.HistorialEstado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public interface IHistorialEstadoDAO {
    List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws PersistenciaException;
}
