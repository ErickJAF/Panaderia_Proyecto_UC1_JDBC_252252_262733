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
 *
 * @author ERICK
 */
public class PedidoBO implements IPedidoBO{
    
    private final IPedidoDAO pedidoDAO;
    private static final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    public PedidoBO(IPedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }
    
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

    @Override
    public List<PedidoEntregaDTO> buscarPorFolio(int folio) throws NegocioException {
        try {
            return pedidoDAO.buscarPorFolio(folio);
        } catch (PersistenciaException ex) {
            Logger.getLogger(PedidoBO.class.getName()).log(Level.SEVERE, "Error al buscar por folio", ex);
            throw new NegocioException("Error al buscar por folio", ex);
        }
    }

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
