/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import negocio.BOs.PedidoBO;
import negocio.DTOs.PedidoEntregaDTO;
import negocio.excepciones.NegocioException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import persistencia.DAOs.IPedidoDAO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class ProductoBOTest {
    private IPedidoDAO pedidoDAO;
    private PedidoBO pedidoBO;

    @BeforeEach
    void setUp() {
        pedidoDAO = Mockito.mock(IPedidoDAO.class);
        pedidoBO = new PedidoBO(pedidoDAO);
    }

    @Test
    void testObtenerPorEstado_Exitoso() throws PersistenciaException, NegocioException {
        List<PedidoEntregaDTO> mockList = new ArrayList<>();
        PedidoEntregaDTO dto = new PedidoEntregaDTO();
        dto.setEstado("Pendiente"); // coincide con tu insert
        dto.setIdPedido(1);
        mockList.add(dto);

        when(pedidoDAO.obtenerPedidosPorEstado("Pendiente")).thenReturn(mockList);

        List<PedidoEntregaDTO> result = pedidoBO.obtenerPorEstado("Pendiente");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pendiente", result.get(0).getEstado());
    }

    @Test
    void testBuscarPorTelefono_Exitoso() throws PersistenciaException, NegocioException {
        List<PedidoEntregaDTO> mockList = new ArrayList<>();
        PedidoEntregaDTO dto = new PedidoEntregaDTO();
        dto.setTelefono("5551234567"); // coincide con tu insert
        dto.setIdPedido(1);
        mockList.add(dto);

        when(pedidoDAO.buscarPorTelefono("5551234567")).thenReturn(mockList);

        List<PedidoEntregaDTO> result = pedidoBO.buscarPorTelefono("5551234567");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("5551234567", result.get(0).getTelefono());
    }

    @Test
    void testBuscarPorRangoFechas_Exitoso() throws PersistenciaException, NegocioException {
        List<PedidoEntregaDTO> mockList = new ArrayList<>();
        PedidoEntregaDTO dto = new PedidoEntregaDTO();
        dto.setFechaCreacion(java.time.LocalDateTime.now());
        dto.setIdPedido(1);
        mockList.add(dto);

        LocalDate inicio = LocalDate.of(2026, 2, 1);
        LocalDate fin = LocalDate.of(2026, 2, 22);

        when(pedidoDAO.buscarPorRangoFechas(inicio, fin)).thenReturn(mockList);

        List<PedidoEntregaDTO> result = pedidoBO.buscarPorRangoFechas(inicio, fin);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerPorEstado_ErrorParametro() {
        assertThrows(NegocioException.class, () -> pedidoBO.obtenerPorEstado(""));
    }

    @Test
    void testBuscarPorRangoFechas_ErrorFechas() {
        LocalDate inicio = LocalDate.of(2026, 2, 22);
        LocalDate fin = LocalDate.of(2026, 2, 1);

        assertThrows(NegocioException.class, () -> pedidoBO.buscarPorRangoFechas(inicio, fin));
    }

    @Test
    void testObtenerPorEstado_DAOException() throws PersistenciaException {
        when(pedidoDAO.obtenerPedidosPorEstado("Pendiente")).thenThrow(new PersistenciaException("Error BD"));

        NegocioException ex = assertThrows(NegocioException.class, () -> pedidoBO.obtenerPorEstado("Pendiente"));
        assertTrue(ex.getMessage().contains("No se pudieron obtener los pedidos"));
    }

    @Test
    void testBuscarPorFolio_Exitoso() throws PersistenciaException, NegocioException {
        List<PedidoEntregaDTO> mockList = new ArrayList<>();
        PedidoEntregaDTO dto = new PedidoEntregaDTO();
        dto.setFolio(1); 
        dto.setEstado("Listo");
        dto.setIdPedido(2);
        dto.setNombreCliente("N/A"); // para evitar NPE
        dto.setTelefono("N/A");      // para evitar NPE
        dto.setTotal(50.0);
        dto.setFechaCreacion(java.time.LocalDateTime.now());
        mockList.add(dto);

        when(pedidoDAO.buscarPorFolio(1)).thenReturn(mockList);

        List<PedidoEntregaDTO> result = pedidoBO.buscarPorFolio(1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getFolio());
    }

    @Test
    void testBuscarPorFolio_NoEncontrado() throws PersistenciaException, NegocioException {
        when(pedidoDAO.buscarPorFolio(9999)).thenReturn(new ArrayList<>());

        List<PedidoEntregaDTO> result = pedidoBO.buscarPorFolio(9999);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testBuscarPorFolio_DAOException() throws PersistenciaException {
        when(pedidoDAO.buscarPorFolio(1)).thenThrow(new PersistenciaException("Error BD"));

        NegocioException ex = assertThrows(NegocioException.class, () -> pedidoBO.buscarPorFolio(1));
        assertTrue(ex.getMessage().contains("Error al buscar por folio"));
    }
}
