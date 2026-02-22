/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import negocio.BOs.HistorialEstadoBO;
import negocio.DTOs.HistorialEstadoDTO;
import negocio.excepciones.NegocioException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import persistencia.DAOs.IHistorialEstadoDAO;

/**
 *
 * @author ERICK
 */
public class HistorialEstadoBOTest {
    private IHistorialEstadoDAO mockDAO;
    private HistorialEstadoBO bo;

    @BeforeEach
    public void setUp() {
        mockDAO = mock(IHistorialEstadoDAO.class);
        bo = new HistorialEstadoBO(mockDAO);
    }

    @Test
    public void testObtenerPedidosPorEstado_Exitoso() throws Exception {
        HistorialEstadoDTO dto1 = new HistorialEstadoDTO();
        dto1.setIdPedido(1);
        dto1.setEstadoNuevo("Entregado");
        dto1.setFechaCambio(LocalDateTime.now());

        when(mockDAO.obtenerPedidosPorEstado("Entregado")).thenReturn(Arrays.asList(dto1));

        List<HistorialEstadoDTO> result = bo.obtenerPedidosPorEstado("Entregado");
        assertEquals(1, result.size());
        assertEquals("Entregado", result.get(0).getEstadoNuevo());

        verify(mockDAO, times(1)).obtenerPedidosPorEstado("Entregado");
    }

    @Test
    public void testObtenerPedidosPorEstado_ParametroInvalido() {
        assertThrows(NegocioException.class, () -> bo.obtenerPedidosPorEstado(""));
        assertThrows(NegocioException.class, () -> bo.obtenerPedidosPorEstado(null));
    }

    @Test
    public void testBuscarPorTelefono_Exitoso() throws Exception {
        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setIdPedido(2);
        dto.setTelefonoCliente("5551234567");

        when(mockDAO.buscarPorTelefono("5551234567")).thenReturn(Arrays.asList(dto));

        List<HistorialEstadoDTO> result = bo.buscarPorTelefono("5551234567");
        assertEquals(1, result.size());
        assertEquals("5551234567", result.get(0).getTelefonoCliente());

        verify(mockDAO, times(1)).buscarPorTelefono("5551234567");
    }

    @Test
    public void testBuscarPorTelefono_ParametroInvalido() {
        assertThrows(NegocioException.class, () -> bo.buscarPorTelefono(""));
        assertThrows(NegocioException.class, () -> bo.buscarPorTelefono(null));
    }

    @Test
    public void testBuscarPorRangoFechas_Exitoso() throws Exception {
        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setIdPedido(3);
        dto.setFechaCambio(LocalDateTime.now());

        LocalDate inicio = LocalDate.now().minusDays(5);
        LocalDate fin = LocalDate.now();

        when(mockDAO.buscarPorRangoFechas(inicio, fin)).thenReturn(Arrays.asList(dto));

        List<HistorialEstadoDTO> result = bo.buscarPorRangoFechas(inicio, fin);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getFechaCambio());

        verify(mockDAO, times(1)).buscarPorRangoFechas(inicio, fin);
    }

    @Test
    public void testBuscarPorRangoFechas_ParametrosInvalidos() {
        LocalDate inicio = LocalDate.now();
        LocalDate fin = LocalDate.now().minusDays(1);
        assertThrows(NegocioException.class, () -> bo.buscarPorRangoFechas(inicio, fin));
        assertThrows(NegocioException.class, () -> bo.buscarPorRangoFechas(null, fin));
        assertThrows(NegocioException.class, () -> bo.buscarPorRangoFechas(inicio, null));
    }

    @Test
    public void testBuscarPorFolio_Exitoso() throws Exception {
        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setIdPedido(4);
        dto.setFolio(123);

        when(mockDAO.buscarPorFolio(123)).thenReturn(Arrays.asList(dto));

        List<HistorialEstadoDTO> result = bo.buscarPorFolio(123);
        assertEquals(1, result.size());
        assertEquals(123, result.get(0).getFolio());

        verify(mockDAO, times(1)).buscarPorFolio(123);
    }
}