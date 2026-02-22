/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import negocio.BOs.PedidoExpressBO;
import negocio.excepciones.NegocioException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class PedidoExpressBOTest {
    private IPedidoExpressDAO pedidoDAOMock;
    private PedidoExpressBO pedidoBO;

    @BeforeEach
    void setUp() {
        pedidoDAOMock = mock(IPedidoExpressDAO.class);
        pedidoBO = new PedidoExpressBO(pedidoDAOMock);
    }

    @Test
    void testBuscarPorId_Exitoso() throws NegocioException, PersistenciaException {
        PedidoExpress pedido = new PedidoExpress();
        pedido.setIdPedido(1);
        pedido.setEstado("Pendiente");
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setTotal(100);

        when(pedidoDAOMock.buscarPorId(1)).thenReturn(pedido);

        PedidoExpress result = pedidoBO.buscarPorId(1);

        assertNotNull(result);
        assertEquals(1, result.getIdPedido());
        assertEquals("Pendiente", result.getEstado());
    }

    @Test
    void testBuscarPorId_NoEncontrado() throws PersistenciaException {
        when(pedidoDAOMock.buscarPorId(99)).thenReturn(null);

        NegocioException ex = assertThrows(NegocioException.class, () -> pedidoBO.buscarPorId(99));
        assertTrue(ex.getMessage().contains("No se encontró el pedido"));
    }

    @Test
    void testActualizarEstado_Exitoso() throws PersistenciaException, NegocioException {
        PedidoExpress pedido = new PedidoExpress();
        pedido.setIdPedido(1);
        pedido.setEstado("Pendiente");

        when(pedidoDAOMock.buscarPorId(1)).thenReturn(pedido);

        pedidoBO.actualizarEstado(1, "Listo");

        verify(pedidoDAOMock, times(1)).actualizarEstado(1, "Listo");
    }

    @Test
    void testActualizarEstado_TransicionInvalida() throws PersistenciaException {
        PedidoExpress pedido = new PedidoExpress();
        pedido.setIdPedido(1);
        pedido.setEstado("Entregado"); // ya entregado

        when(pedidoDAOMock.buscarPorId(1)).thenReturn(pedido);

        NegocioException ex = assertThrows(NegocioException.class,
                () -> pedidoBO.actualizarEstado(1, "Listo"));

        assertTrue(ex.getMessage().contains("Solo los pedidos Pendientes pueden marcarse como Listo"));
    }

    @Test
    void testValidarTiempoEntrega_DentroDelLimite() throws PersistenciaException, NegocioException {
        PedidoExpress pedido = new PedidoExpress();
        pedido.setIdPedido(1);
        pedido.setFechaListo(LocalDateTime.now().minusMinutes(10)); // 10 min atrás
        pedido.setEstado("Listo");

        IPedidoExpressDAO pedidoDAOMock = Mockito.mock(IPedidoExpressDAO.class);
        PedidoExpressBO bo = new PedidoExpressBO(pedidoDAOMock);

        // No debe lanzar excepción
        bo.validarTiempoEntrega(pedido);
    }

    @Test
    void testValidarTiempoEntrega_Excede20Min() throws PersistenciaException {
        PedidoExpress pedido = new PedidoExpress();
        pedido.setIdPedido(1);
        pedido.setFechaListo(LocalDateTime.now().minusMinutes(25)); // 25 min atrás
        pedido.setEstado("Listo");

        IPedidoExpressDAO pedidoDAOMock = Mockito.mock(IPedidoExpressDAO.class);

        // Mock para actualizarEstado
        Mockito.doNothing().when(pedidoDAOMock).actualizarEstado(1, "No Entregado");

        PedidoExpressBO bo = new PedidoExpressBO(pedidoDAOMock);

        NegocioException ex = assertThrows(NegocioException.class, () -> bo.validarTiempoEntrega(pedido));
        assertTrue(ex.getMessage().contains("superado el tiempo estimado de entrega"));

        // Verificar que se haya llamado a actualizarEstado
        Mockito.verify(pedidoDAOMock).actualizarEstado(1, "No Entregado");
    }
}
