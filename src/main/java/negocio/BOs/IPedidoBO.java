/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import java.util.List;
import negocio.DTOs.PedidoEntregaDTO;
import negocio.excepciones.NegocioException;

/**
 *
 * @author ERICK
 */
public interface IPedidoBO {
    List<PedidoEntregaDTO> buscarPorCliente(String nombreCliente) throws NegocioException;
            
    List<PedidoEntregaDTO> obtenerPorEstado(String Estado) throws NegocioException;
    
    List<PedidoEntregaDTO> buscarPorTelefono(String telefono) throws NegocioException;
    
    List<PedidoEntregaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException;
    
    List<PedidoEntregaDTO> buscarPorFolio(int folio) throws NegocioException;
    
    public void generarPago(double monto, String metodoPago, int idPedido) throws NegocioException;
}
