/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import java.util.List;
import negocio.excepciones.NegocioException;
import persistencia.dominio.HistorialEstado;

/**
 *
 * @author ERICK
 */
public interface IHistorialEstadoBO {
    List<HistorialEstado> obtenerHistorialPorPedido(int idPedido) throws NegocioException;
}
