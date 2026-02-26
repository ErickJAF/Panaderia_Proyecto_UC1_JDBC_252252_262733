/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.DTOs.PedidoEntregaDTO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.IPedidoDAO;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase de lógica de negocio encargada de gestionar las operaciones
 * relacionadas con los pedidos del sistema.
 * <p>
 * Permite realizar búsquedas de pedidos mediante distintos filtros,
 * así como generar pagos asociados, aplicando validaciones de negocio
 * antes de interactuar con la capa de persistencia.
 * </p>
 * 
 * @author ERICK
 */
public class PedidoBO implements IPedidoBO{
    
    /**
     * DAO encargado de las operaciones de persistencia de pedidos.
     */
    private final IPedidoDAO pedidoDAO;

    /**
     * Logger para el registro de eventos y errores relacionados con pedidos.
     */
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    /**
     * Constructor que recibe la dependencia necesaria para la gestión
     * de pedidos.
     *
     * @param pedidoDAO DAO para operaciones de pedidos
     */
    public PedidoBO(IPedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }
    
    /**
     * Busca pedidos asociados a un cliente específico.
     *
     * @param nombreCliente Nombre del cliente para realizar la búsqueda
     * @return Lista de objetos PedidoEntregaDTO relacionados con el cliente
     * @throws NegocioException Si el nombre es inválido o ocurre un error
     *                          en la capa de persistencia
     */
    @Override
    public List<PedidoEntregaDTO> buscarPorCliente(String nombreCliente) throws NegocioException {
        if (nombreCliente == null || nombreCliente.isEmpty()) {
            throw new NegocioException("Debe proporcionar un nombre de cliente para la búsqueda.");
        }

        try {
            return pedidoDAO.buscarPedidosPorCliente(nombreCliente);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al buscar pedidos por cliente", ex);
            throw new NegocioException("No se pudieron buscar los pedidos por cliente.", ex);
        }
    }

    /**
     * Obtiene los pedidos que se encuentran en un estado determinado.
     *
     * @param estado Estado por el cual se filtrarán los pedidos
     * @return Lista de objetos PedidoEntregaDTO que coinciden con el estado
     * @throws NegocioException Si el estado es inválido o ocurre un error
     *                          en la capa de persistencia
     */
    @Override
    public List<PedidoEntregaDTO> obtenerPorEstado(String estado) throws NegocioException {
        if (estado == null || estado.isBlank()) {
            throw new NegocioException("El estado no puede estar vacío");
        }

        try {
            return pedidoDAO.obtenerPedidosPorEstado(estado);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en obtenerPorEstado", ex);
            throw new NegocioException("No se pudieron obtener los pedidos por estado", ex);
        }
    }

    /**
     * Busca pedidos asociados a un número de teléfono específico.
     *
     * @param telefono Número de teléfono para realizar la búsqueda
     * @return Lista de objetos PedidoEntregaDTO relacionados con el teléfono
     * @throws NegocioException Si el teléfono es inválido o ocurre un error
     *                          en la capa de persistencia
     */
    @Override
    public List<PedidoEntregaDTO> buscarPorTelefono(String telefono) throws NegocioException {
        if (telefono == null || telefono.isBlank()) {
            throw new NegocioException("El teléfono no puede estar vacío");
        }

        try {
            return pedidoDAO.buscarPorTelefono(telefono);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en buscarPorTelefono", ex);
            throw new NegocioException("No se pudieron obtener los pedidos por teléfono", ex);
        }
    }

    /**
     * Busca pedidos dentro de un rango de fechas determinado.
     *
     * @param inicio Fecha inicial del rango de búsqueda
     * @param fin Fecha final del rango de búsqueda
     * @return Lista de objetos PedidoEntregaDTO dentro del rango indicado
     * @throws NegocioException Si las fechas son inválidas o ocurre un error
     *                          en la capa de persistencia
     */
    @Override
    public List<PedidoEntregaDTO> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) throws NegocioException {
        if (inicio == null || fin == null) {
            throw new NegocioException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (inicio.isAfter(fin)) {
            throw new NegocioException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        try {
            return pedidoDAO.buscarPorRangoFechas(inicio, fin);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en buscarPorRangoFechas", ex);
            throw new NegocioException("No se pudieron obtener los pedidos por rango de fechas", ex);
        }
    }

    /**
     * Busca pedidos mediante su número de folio.
     *
     * @param folio Número de folio del pedido
     * @return Lista de objetos PedidoEntregaDTO asociados al folio
     * @throws NegocioException Si ocurre un error en la capa de persistencia
     */
    @Override
    public List<PedidoEntregaDTO> buscarPorFolio(int folio) throws NegocioException {
        try {
            return pedidoDAO.buscarPorFolio(folio);
        } catch (PersistenciaException ex) {
            Logger.getLogger(PedidoBO.class.getName()).log(Level.SEVERE, "Error al buscar por folio", ex);
            throw new NegocioException("Error al buscar por folio", ex);
        }
    }

    /**
     * Genera el pago de un pedido siempre que cumpla con las reglas de negocio.
     * <p>
     * Solo permite generar el pago si el pedido existe y se encuentra
     * en estado "Listo".
     * </p>
     *
     * @param monto Monto a pagar
     * @param metodoPago Método utilizado para realizar el pago
     * @param idPedido Identificador del pedido
     * @throws NegocioException Si el pedido no existe, no está en estado válido
     *                          o ocurre un error en la capa de persistencia
     */
    @Override
    public void generarPago(double monto, String metodoPago, int idPedido) throws NegocioException {

        try {
            PedidoEntregaDTO pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new NegocioException("El pedido no existe.");
            }

            if (!"Listo".equalsIgnoreCase(pedido.getEstado())) {
                throw new NegocioException(
                    "Solo se puede generar el pago cuando el pedido está en estado 'Listo'."
                );
            }

            pedidoDAO.generarPago(monto, metodoPago, idPedido);

        } catch (PersistenciaException e) {
            throw new NegocioException(
                "No se pudo generar el pago para el pedido " + idPedido, e
            );
        }
    }
    
    /**
     * Obtiene todos los pedidos registrados en el sistema.
     *
     * @return Lista completa de objetos PedidoEntregaDTO
     * @throws NegocioException Si ocurre un error en la capa de persistencia
     */
    @Override
    public List<PedidoEntregaDTO> obtenerTodos() throws NegocioException {
        try {
            return pedidoDAO.obtenerTodos();
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error al obtener todos los pedidos", ex);
            throw new NegocioException("No se pudieron obtener los pedidos", ex);
        }
    }
}