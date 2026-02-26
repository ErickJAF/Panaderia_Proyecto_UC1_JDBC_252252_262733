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
 * Interfaz que define las operaciones de negocio relacionadas
 * con la gestión y consulta de pedidos.
 * <p>
 * Establece los métodos para realizar búsquedas de pedidos
 * mediante distintos criterios, así como la generación de pagos
 * asociados a un pedido.
 * </p>
 * 
 * @author ERICK
 */
public interface IPedidoBO {

    /**
     * Busca pedidos asociados a un cliente específico.
     *
     * @param nombreCliente Nombre del cliente para filtrar los pedidos
     * @return Lista de objetos PedidoEntregaDTO relacionados con el cliente
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<PedidoEntregaDTO> buscarPorCliente(String nombreCliente) throws NegocioException;
            
    /**
     * Obtiene los pedidos que se encuentran en un estado determinado.
     *
     * @param Estado Estado por el cual se filtrarán los pedidos
     * @return Lista de objetos PedidoEntregaDTO que coinciden con el estado indicado
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<PedidoEntregaDTO> obtenerPorEstado(String Estado) throws NegocioException;
    
    /**
     * Busca pedidos asociados a un número de teléfono específico.
     *
     * @param telefono Número de teléfono para realizar la búsqueda
     * @return Lista de objetos PedidoEntregaDTO relacionados con el teléfono
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<PedidoEntregaDTO> buscarPorTelefono(String telefono) throws NegocioException;
    
    /**
     * Busca pedidos dentro de un rango de fechas determinado.
     *
     * @param inicio Fecha inicial del rango de búsqueda
     * @param fin Fecha final del rango de búsqueda
     * @return Lista de objetos PedidoEntregaDTO dentro del rango indicado
     * @throws NegocioException Si las fechas son inválidas o ocurre un error en la consulta
     */
    List<PedidoEntregaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException;
    
    /**
     * Busca pedidos mediante su número de folio.
     *
     * @param folio Número de folio del pedido
     * @return Lista de objetos PedidoEntregaDTO asociados al folio indicado
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<PedidoEntregaDTO> buscarPorFolio(int folio) throws NegocioException;
    
    /**
     * Genera el registro de un pago asociado a un pedido específico.
     *
     * @param monto Monto del pago realizado
     * @param metodoPago Método utilizado para realizar el pago
     * @param idPedido Identificador del pedido al que se le asociará el pago
     * @throws NegocioException Si ocurre un error durante el proceso de pago
     */
    void generarPago(double monto, String metodoPago, int idPedido) throws NegocioException;
    
    /**
     * Obtiene todos los pedidos registrados en el sistema.
     *
     * @return Lista completa de objetos PedidoEntregaDTO
     * @throws NegocioException Si ocurre un error durante la consulta
     */
    List<PedidoEntregaDTO> obtenerTodos() throws NegocioException;
}