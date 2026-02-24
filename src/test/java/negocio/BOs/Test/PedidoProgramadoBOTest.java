/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.BOs.Test;

import java.time.LocalDate;
import negocio.BOs.PedidoProgramadoBO;
import negocio.excepciones.NegocioException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class PedidoProgramadoBOTest {
//    private IPedidoProgramadoDAO pedidoDAOMock;
//    private PedidoProgramadoBO pedidoBO;
//
//    @BeforeEach
//    void setUp() {
//        pedidoDAOMock = mock(IPedidoProgramadoDAO.class);
//        pedidoBO = new PedidoProgramadoBO(pedidoDAOMock);
//    }
//
//    @Test
//    void testBuscarPorId_Exitoso() throws NegocioException, PersistenciaException {
//        PedidoProgramado pedido = new PedidoProgramado();
//        pedido.setIdPedido(7);
//        pedido.setEstado("Pendiente");
//        pedido.setFechaCreacion(LocalDate.now());
//        pedido.setTotal(250);
//
//        when(pedidoDAOMock.buscarPorId(7)).thenReturn(pedido);
//
//        PedidoProgramado result = pedidoBO.buscarPorId(7);
//
//        assertNotNull(result);
//        assertEquals(7, result.getIdPedido());
//        assertEquals("Pendiente", result.getEstado());
//    }
//
//    @Test
//    void testBuscarPorId_NoEncontrado() throws PersistenciaException {
//        when(pedidoDAOMock.buscarPorId(100)).thenReturn(null);
//
//        NegocioException ex = assertThrows(NegocioException.class,
//                () -> pedidoBO.buscarPorId(100));
//
//        assertTrue(ex.getMessage().contains("No se encontró el pedido"));
//    }
//
//    @Test
//    void testActualizarEstado_Exitoso() throws PersistenciaException, NegocioException {
//        PedidoProgramado pedido = new PedidoProgramado();
//        pedido.setIdPedido(7);
//        pedido.setEstado("Pendiente");
//
//        when(pedidoDAOMock.buscarPorId(7)).thenReturn(pedido);
//
//        pedidoBO.actualizarEstado(7, "Listo");
//
//        verify(pedidoDAOMock, times(1)).actualizarEstado(7, "Listo");
//    }
//
//    @Test
//    void testActualizarEstado_TransicionInvalida() throws PersistenciaException {
//        PedidoProgramado pedido = new PedidoProgramado();
//        pedido.setIdPedido(7);
//        pedido.setEstado("Entregado"); // ya entregado
//
//        when(pedidoDAOMock.buscarPorId(7)).thenReturn(pedido);
//
//        NegocioException ex = assertThrows(NegocioException.class,
//                () -> pedidoBO.actualizarEstado(7, "Listo"));
//
//        assertTrue(ex.getMessage().contains("Solo los pedidos Pendientes pueden marcarse como Listo"));
//    }
//
//    @Test
//    void testCancelarPedido_Exitoso() throws PersistenciaException, NegocioException {
//        PedidoProgramado pedido = new PedidoProgramado();
//        pedido.setIdPedido(8);
//        pedido.setEstado("Pendiente");
//
//        when(pedidoDAOMock.buscarPorId(8)).thenReturn(pedido);
//
//        pedidoBO.actualizarEstado(8, "Cancelado");
//
//        verify(pedidoDAOMock, times(1)).actualizarEstado(8, "Cancelado");
//    }
//
//    @Test
//    void testMarcarNoEntregado_Exitoso() throws PersistenciaException, NegocioException {
//        PedidoProgramado pedido = new PedidoProgramado();
//        pedido.setIdPedido(9);
//        pedido.setEstado("Listo");
//
//        when(pedidoDAOMock.buscarPorId(9)).thenReturn(pedido);
//
//        pedidoBO.actualizarEstado(9, "No Entregado");
//
//        verify(pedidoDAOMock, times(1)).actualizarEstado(9, "No Entregado");
//    }
}
