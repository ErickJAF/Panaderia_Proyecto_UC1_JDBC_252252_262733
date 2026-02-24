package negocio.BOs.Test;

import java.time.LocalDateTime;
import negocio.BOs.PedidoExpressBO;
import negocio.DTOs.PedidoExpressDTO;
import negocio.excepciones.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.excepciones.PersistenciaException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PedidoExpressBOTest {

    private IPedidoExpressDAO pedidoDAOMock;
    private PedidoExpressBO pedidoBO;

    @BeforeEach
    void setUp() {
        pedidoDAOMock = mock(IPedidoExpressDAO.class);
        pedidoBO = new PedidoExpressBO(pedidoDAOMock);
    }

    @Test
    void testBuscarPorId_Exitoso() throws PersistenciaException, NegocioException {

        PedidoExpressDTO pedido = new PedidoExpressDTO();
        pedido.setIdPedido(1);
        pedido.setEstado("Pendiente");

        when(pedidoDAOMock.buscarPorId(1)).thenReturn(pedido);

        PedidoExpressDTO result = pedidoBO.buscarPorId(1);

        assertNotNull(result);
        assertEquals(1, result.getIdPedido());
        assertEquals("Pendiente", result.getEstado());
    }

    @Test
    void testBuscarPorId_NoEncontrado() throws PersistenciaException {

        when(pedidoDAOMock.buscarPorId(99)).thenReturn(null);

        NegocioException ex =
                assertThrows(NegocioException.class,
                        () -> pedidoBO.buscarPorId(99));

        assertTrue(ex.getMessage().contains("Pedido no encontrado"));
    }

    @Test
    void testActualizarEstado_Exitoso() throws PersistenciaException, NegocioException {

        PedidoExpressDTO pedido = new PedidoExpressDTO();
        pedido.setIdPedido(1);
        pedido.setEstado("Pendiente");

        when(pedidoDAOMock.buscarPorId(1)).thenReturn(pedido);

        pedidoBO.actualizarEstado(1, "Listo");

        verify(pedidoDAOMock, times(1))
                .actualizarFechaListo(eq(1), any(LocalDateTime.class));

        verify(pedidoDAOMock, times(1))
                .actualizarEstado(1, "Listo");
    }

    @Test
    void testActualizarEstado_TransicionInvalida() throws PersistenciaException {

        PedidoExpressDTO pedido = new PedidoExpressDTO();
        pedido.setIdPedido(1);
        pedido.setEstado("Entregado");

        when(pedidoDAOMock.buscarPorId(1)).thenReturn(pedido);

        NegocioException ex =
                assertThrows(NegocioException.class,
                        () -> pedidoBO.actualizarEstado(1, "Listo"));

        assertTrue(ex.getMessage().contains("Solo Pendiente"));
    }

    @Test
    void testValidarTiempoEntrega_Excede20Min()
            throws PersistenciaException {

        PedidoExpressDTO pedido = new PedidoExpressDTO();
        pedido.setIdPedido(1);
        pedido.setEstado("Listo");
        pedido.setFechaCreacion(
                LocalDateTime.now().minusMinutes(25));

        when(pedidoDAOMock.buscarPorId(1)).thenReturn(pedido);

        doNothing().when(pedidoDAOMock)
                .actualizarEstado(1, "No Entregado");

        NegocioException ex =
                assertThrows(NegocioException.class,
                        () -> pedidoBO.validarTiempoEntrega(1));

        assertTrue(ex.getMessage()
                .contains("superó los 20 minutos"));

        verify(pedidoDAOMock)
                .actualizarEstado(1, "No Entregado");
    }
}