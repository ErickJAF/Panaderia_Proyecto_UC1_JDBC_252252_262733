package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import persistencia.conexion.IConexionBD;
import persistencia.conexion.ConexionBD;
import persistencia.DAOs.IPedidoDAO;
import persistencia.DAOs.PedidoDAO;
import negocio.BOs.IPedidoBO;
import negocio.BOs.PedidoBO;
import negocio.DTOs.PedidoEntregaDTO;
import negocio.excepciones.NegocioException;

import java.time.LocalDate;
import java.util.List;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.PedidoExpressBO;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.DAOs.PedidoExpressDAO;

public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) {

        IConexionBD conexionBD = new ConexionBD();
        IPedidoDAO pedidoDAO = new PedidoDAO(conexionBD);
        IPedidoBO pedidoBO = new PedidoBO(pedidoDAO);

        try {
            // -------------------------------
            // 1️⃣ Obtener pedidos por estado
            System.out.println("=== Pedidos Pendientes ===");
            List<PedidoEntregaDTO> pendientes = pedidoBO.obtenerPorEstado("Pendiente");
            for (PedidoEntregaDTO p : pendientes) {
                System.out.println(formatPedido(p));
            }

            // -------------------------------
            // 2️⃣ Buscar pedidos por teléfono
            System.out.println("\n=== Pedidos del teléfono 5551234567 ===");
            List<PedidoEntregaDTO> porTelefono = pedidoBO.buscarPorTelefono("5551234567");
            for (PedidoEntregaDTO p : porTelefono) {
                System.out.println(formatPedido(p));
            }

            // -------------------------------
            // 3️⃣ Buscar pedidos por rango de fechas
            LocalDate inicio = LocalDate.now().minusDays(7);
            LocalDate fin = LocalDate.now();
            System.out.println("\n=== Pedidos de los últimos 7 días ===");
            List<PedidoEntregaDTO> porRango = pedidoBO.buscarPorRangoFechas(inicio, fin);
            for (PedidoEntregaDTO p : porRango) {
                System.out.println(formatPedido(p));
            }

            // -------------------------------
            // 4️⃣ Buscar pedido por folio (solo EXPRESS)
            int folioBuscar = 1; // Cambia el folio según tu prueba
            System.out.println("\n=== Buscar pedido por folio: " + folioBuscar + " ===");
            List<PedidoEntregaDTO> porFolio = pedidoBO.buscarPorFolio(folioBuscar);

            if (porFolio.isEmpty()) {
                System.out.println("No se encontró ningún pedido con el folio " + folioBuscar);
            } else {
                for (PedidoEntregaDTO p : porFolio) {
                    System.out.println(formatPedido(p));
                }
            }

        } catch (NegocioException ex) {
            System.err.println("Error al obtener pedidos: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        System.out.println("\n=== Pruebas de PedidoExpressBO ===");

        try {
            // Crear BO de pedidos express
            IPedidoExpressDAO pedidoExpressDAO = new PedidoExpressDAO(conexionBD);
            IPedidoExpressBO pedidoExpressBO = new PedidoExpressBO(pedidoExpressDAO);

            // --- 1️⃣ Buscar un pedido existente por ID
            int idPedido = 3; // Cambiar según tu BD
            System.out.println("\n--- Buscar pedido por ID " + idPedido + " ---");
            try {
                var pedido = pedidoExpressBO.buscarPorId(idPedido);
                System.out.println("Pedido encontrado: ID=" + pedido.getIdPedido() + ", Estado=" + pedido.getEstado() +
                                   ", FechaListo=" + pedido.getFechaListo());
            } catch (NegocioException ex) {
                System.out.println("Error: " + ex.getMessage());
            }

            // --- 2️⃣ Intentar actualizar estado a Listo
            System.out.println("\n--- Actualizar estado a Listo ---");
            try {
                pedidoExpressBO.actualizarEstado(idPedido, "Listo");
                System.out.println("Estado actualizado a Listo correctamente.");
            } catch (NegocioException ex) {
                System.out.println("Error al actualizar a Listo: " + ex.getMessage());
            }

            // --- 3️⃣ Intentar actualizar estado a Entregado
            System.out.println("\n--- Actualizar estado a Entregado ---");
            try {
                pedidoExpressBO.actualizarEstado(idPedido, "Entregado");
                System.out.println("Estado actualizado a Entregado correctamente.");
            } catch (NegocioException ex) {
                System.out.println("Error al actualizar a Entregado: " + ex.getMessage());
            }

            // --- 4️⃣ Validar tiempo de entrega
            System.out.println("\n--- Validar tiempo de entrega ---");
            try {
                var pedido = pedidoExpressBO.buscarPorId(idPedido);
                pedidoExpressBO.validarTiempoEntrega(pedido);
                System.out.println("Tiempo de entrega dentro de lo permitido.");
            } catch (NegocioException ex) {
                System.out.println("Validación de tiempo: " + ex.getMessage());
            }

            // --- 5️⃣ Intentar actualizar estado a Cancelado (según reglas)
            System.out.println("\n--- Intentar cancelar pedido ---");
            try {
                pedidoExpressBO.actualizarEstado(idPedido, "Cancelado");
                System.out.println("Pedido cancelado correctamente.");
            } catch (NegocioException ex) {
                System.out.println("No se pudo cancelar: " + ex.getMessage());
            }

            // --- 6️⃣ Intentar forzar No Entregado
            System.out.println("\n--- Forzar estado No Entregado ---");
            try {
                pedidoExpressBO.actualizarEstado(idPedido, "No Entregado");
                System.out.println("Pedido marcado como No Entregado correctamente.");
            } catch (NegocioException ex) {
                System.out.println("No se pudo marcar No Entregado: " + ex.getMessage());
            }

        } catch (Exception ex) {
            System.err.println("Error en pruebas PedidoExpressBO: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        System.out.println("\n=== Pruebas de PedidoExpressBO: Cancelar y No Entregado ===");

        try {
            // Crear BO de pedidos express
            IPedidoExpressDAO pedidoExpressDAO = new PedidoExpressDAO(conexionBD);
            IPedidoExpressBO pedidoExpressBO = new PedidoExpressBO(pedidoExpressDAO);

            // --- 1️⃣ Cancelar un pedido pendiente
            int idPedidoCancelar = 4; // Asegúrate de que este pedido esté en estado "Pendiente"
            System.out.println("\n--- Cancelar pedido ID " + idPedidoCancelar + " ---");
            try {
                pedidoExpressBO.actualizarEstado(idPedidoCancelar, "Cancelado");
                System.out.println("Pedido cancelado correctamente.");
            } catch (NegocioException ex) {
                System.out.println("No se pudo cancelar: " + ex.getMessage());
            }

            // --- 2️⃣ Marcar un pedido como No Entregado
            int idPedidoNoEntregado = 5; // Asegúrate de que este pedido esté en estado "Listo"
            System.out.println("\n--- Marcar pedido ID " + idPedidoNoEntregado + " como No Entregado ---");
            try {
                pedidoExpressBO.actualizarEstado(idPedidoNoEntregado, "No Entregado");
                System.out.println("Pedido marcado como No Entregado correctamente.");
            } catch (NegocioException ex) {
                System.out.println("No se pudo marcar No Entregado: " + ex.getMessage());
            }

        } catch (Exception ex) {
            System.err.println("Error en pruebas PedidoExpressBO: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static String formatPedido(PedidoEntregaDTO p) {
        return "ID: " + p.getIdPedido()
                + " | Cliente: " + p.getNombreCliente()
                + " | Teléfono: " + p.getTelefono()
                + " | Estado: " + p.getEstado()
                + " | Total: $" + p.getTotal()
                + " | Folio: " + p.getFolio()
                + " | Fecha: " + p.getFechaCreacion();
    }
}