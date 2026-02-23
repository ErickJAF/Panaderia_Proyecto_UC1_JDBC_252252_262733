package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;

import negocio.BOs.IPedidoProgramadoBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.PedidoProgramadoBO;
import negocio.BOs.ProductoBO;
import negocio.excepciones.NegocioException;
import persistencia.DAOs.DetallePedidoDAO;
import persistencia.DAOs.IDetallePedidoDAO;
import persistencia.DAOs.IPedidoProgramadoDAO;
import persistencia.DAOs.IProductoDAO;
import persistencia.DAOs.PedidoProgramadoDAO;
import persistencia.DAOs.ProductoDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import presentacion.FrmCrearPedidoProgramado;
import presentacion.FrmGestionDeEntregas;

//import persistencia.conexion.ConexionBD;
//import persistencia.DAOs.IPedidoDAO;
//import persistencia.DAOs.PedidoDAO;
//import negocio.BOs.IPedidoBO;
//import negocio.BOs.PedidoBO;
//import negocio.DTOs.PedidoEntregaDTO;
//import negocio.excepciones.NegocioException;
//
//import java.time.LocalDate;
//import java.util.List;
//import negocio.BOs.HistorialEstadoBO;
//import negocio.BOs.IPedidoExpressBO;
//import negocio.BOs.IPedidoProgramadoBO;
//import negocio.BOs.PedidoExpressBO;
//import negocio.BOs.PedidoProgramadoBO;
//import negocio.DTOs.DetallePedidoDTO;
//import negocio.DTOs.HistorialEstadoDTO;
//import negocio.DTOs.PedidoExpressDTO;
//import negocio.DTOs.PedidoProgramadoDTO;
//import persistencia.DAOs.HistorialEstadoDAO;
//import persistencia.DAOs.IHistorialEstadoDAO;
//import persistencia.DAOs.IPedidoExpressDAO;
//import persistencia.DAOs.IPedidoProgramadoDAO;
//import persistencia.DAOs.PedidoExpressDAO;
//import persistencia.DAOs.PedidoProgramadoDAO;

public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {
public static void main(String[] args) {

    java.awt.EventQueue.invokeLater(() -> {

        try {

            IConexionBD conexion = new ConexionBD();

            IProductoDAO productoDAO =
                    new ProductoDAO(conexion);
            IProductoBO productoBO =
                    new ProductoBO(productoDAO);

            IPedidoProgramadoDAO pedidoDAO =
                    new PedidoProgramadoDAO(conexion);

            IDetallePedidoDAO detalleDAO =
                    new DetallePedidoDAO(conexion);

            IPedidoProgramadoBO pedidoBO =
                    new PedidoProgramadoBO(pedidoDAO, detalleDAO);

            int idEmpleado = 1;
            int idCliente = 1;

            FrmCrearPedidoProgramado frm =
                    new FrmCrearPedidoProgramado(
                            conexion,
                            productoBO,
                            pedidoBO,
                            idEmpleado,
                            idCliente
                    );

            frm.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    });
}
}
/*
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(() -> {
            FrmGestionDeEntregas frame = new FrmGestionDeEntregas();
            frame.setVisible(true);
        });
*/
//        IConexionBD conexionBD = new ConexionBD();
//        IPedidoDAO pedidoDAO = new PedidoDAO(conexionBD);
//        IPedidoBO pedidoBO = new PedidoBO(pedidoDAO);
//
//        try {
//            // -------------------------------
//            // 1️⃣ Obtener pedidos por estado
//            System.out.println("=== Pedidos Pendientes ===");
//            List<PedidoEntregaDTO> pendientes = pedidoBO.obtenerPorEstado("Pendiente");
//            for (PedidoEntregaDTO p : pendientes) {
//                System.out.println(formatPedido(p));
//            }
//
//            // -------------------------------
//            // 2️⃣ Buscar pedidos por teléfono
//            System.out.println("\n=== Pedidos del teléfono 5551234567 ===");
//            List<PedidoEntregaDTO> porTelefono = pedidoBO.buscarPorTelefono("5551234567");
//            for (PedidoEntregaDTO p : porTelefono) {
//                System.out.println(formatPedido(p));
//            }
//
//            // -------------------------------
//            // 3️⃣ Buscar pedidos por rango de fechas
//            LocalDate inicio = LocalDate.now().minusDays(7);
//            LocalDate fin = LocalDate.now();
//            System.out.println("\n=== Pedidos de los últimos 7 días ===");
//            List<PedidoEntregaDTO> porRango = pedidoBO.buscarPorRangoFechas(inicio, fin);
//            for (PedidoEntregaDTO p : porRango) {
//                System.out.println(formatPedido(p));
//            }
//
//            // -------------------------------
//            // 4️⃣ Buscar pedido por folio (solo EXPRESS)
//            int folioBuscar = 1; // Cambia el folio según tu prueba
//            System.out.println("\n=== Buscar pedido por folio: " + folioBuscar + " ===");
//            List<PedidoEntregaDTO> porFolio = pedidoBO.buscarPorFolio(folioBuscar);
//
//            if (porFolio.isEmpty()) {
//                System.out.println("No se encontró ningún pedido con el folio " + folioBuscar);
//            } else {
//                for (PedidoEntregaDTO p : porFolio) {
//                    System.out.println(formatPedido(p));
//                }
//            }
//
//        } catch (NegocioException ex) {
//            System.err.println("Error al obtener pedidos: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//        
//        System.out.println("\n=== Pruebas de PedidoExpressBO ===");
//
//        try {
//            // Crear BO de pedidos express
//            IPedidoExpressDAO pedidoExpressDAO = new PedidoExpressDAO(conexionBD);
//            IPedidoExpressBO pedidoExpressBO = new PedidoExpressBO(pedidoExpressDAO);
//
//            // --- 1️⃣ Buscar un pedido existente por ID
//            int idPedido = 3; // Cambiar según tu BD
//            System.out.println("\n--- Buscar pedido por ID " + idPedido + " ---");
//            try {
//                var pedido = pedidoExpressBO.buscarPorId(idPedido);
//                System.out.println("Pedido encontrado: ID=" + pedido.getIdPedido() + ", Estado=" + pedido.getEstado() +
//                                   ", FechaListo=" + pedido.getFechaListo());
//            } catch (NegocioException ex) {
//                System.out.println("Error: " + ex.getMessage());
//            }
//
//            // --- 2️⃣ Intentar actualizar estado a Listo
//            System.out.println("\n--- Actualizar estado a Listo ---");
//            try {
//                pedidoExpressBO.actualizarEstado(idPedido, "Listo");
//                System.out.println("Estado actualizado a Listo correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("Error al actualizar a Listo: " + ex.getMessage());
//            }
//
//            // --- 3️⃣ Intentar actualizar estado a Entregado
//            System.out.println("\n--- Actualizar estado a Entregado ---");
//            try {
//                pedidoExpressBO.actualizarEstado(idPedido, "Entregado");
//                System.out.println("Estado actualizado a Entregado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("Error al actualizar a Entregado: " + ex.getMessage());
//            }
//
//            // --- 4️⃣ Validar tiempo de entrega
//            System.out.println("\n--- Validar tiempo de entrega ---");
//            try {
//                var pedido = pedidoExpressBO.buscarPorId(idPedido);
//                pedidoExpressBO.validarTiempoEntrega(pedido);
//                System.out.println("Tiempo de entrega dentro de lo permitido.");
//            } catch (NegocioException ex) {
//                System.out.println("Validación de tiempo: " + ex.getMessage());
//            }
//
//            // --- 5️⃣ Intentar actualizar estado a Cancelado (según reglas)
//            System.out.println("\n--- Intentar cancelar pedido ---");
//            try {
//                pedidoExpressBO.actualizarEstado(idPedido, "Cancelado");
//                System.out.println("Pedido cancelado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("No se pudo cancelar: " + ex.getMessage());
//            }
//
//            // --- 6️⃣ Intentar forzar No Entregado
//            System.out.println("\n--- Forzar estado No Entregado ---");
//            try {
//                pedidoExpressBO.actualizarEstado(idPedido, "No Entregado");
//                System.out.println("Pedido marcado como No Entregado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("No se pudo marcar No Entregado: " + ex.getMessage());
//            }
//            
//            PedidoExpressDTO pedido = pedidoExpressBO.buscarPorIdDTO(2);
//
//            System.out.println("===== PEDIDO EXPRESS =====");
//            System.out.println("ID Pedido: " + pedido.getIdPedido());
//            System.out.println("Folio: " + pedido.getFolio());
//            System.out.println("PIN Encriptado: " + pedido.getPinEncriptado());
//            System.out.println("Subtotal: " + pedido.getSubtotal());
//            System.out.println("Total: " + pedido.getTotal());
//            System.out.println("Estado: " + pedido.getEstado());
//            System.out.println("Fecha Creación: " + pedido.getFechaCreacion());
//
//            System.out.println("\n--- DETALLES ---");
//
//            for (DetallePedidoDTO detalle : pedido.getDetalles()) {
//                System.out.println("Producto ID: " + detalle.getIdProducto());
//                System.out.println("Cantidad: " + detalle.getCantidad());
//                System.out.println("Precio Unitario: " + detalle.getPrecioUnitario());
//                System.out.println("Nota: " + detalle.getNota());
//                System.out.println("----------------------------");
//            }
//
//        } catch (Exception ex) {
//            System.err.println("Error en pruebas PedidoExpressBO: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//        
//        System.out.println("\n=== Pruebas de PedidoExpressBO: Cancelar y No Entregado ===");
//
//        try {
//            // Crear BO de pedidos express
//            IPedidoExpressDAO pedidoExpressDAO = new PedidoExpressDAO(conexionBD);
//            IPedidoExpressBO pedidoExpressBO = new PedidoExpressBO(pedidoExpressDAO);
//
//            // --- 1️⃣ Cancelar un pedido pendiente
//            int idPedidoCancelar = 4; // Asegúrate de que este pedido esté en estado "Pendiente"
//            System.out.println("\n--- Cancelar pedido ID " + idPedidoCancelar + " ---");
//            try {
//                pedidoExpressBO.actualizarEstado(idPedidoCancelar, "Cancelado");
//                System.out.println("Pedido cancelado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("No se pudo cancelar: " + ex.getMessage());
//            }
//
//            // --- 2️⃣ Marcar un pedido como No Entregado
//            int idPedidoNoEntregado = 5; // Asegúrate de que este pedido esté en estado "Listo"
//            System.out.println("\n--- Marcar pedido ID " + idPedidoNoEntregado + " como No Entregado ---");
//            try {
//                pedidoExpressBO.actualizarEstado(idPedidoNoEntregado, "No Entregado");
//                System.out.println("Pedido marcado como No Entregado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("No se pudo marcar No Entregado: " + ex.getMessage());
//            }
//
//        } catch (Exception ex) {
//            System.err.println("Error en pruebas PedidoExpressBO: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//        
//        System.out.println("\n=== Pruebas de PedidoProgramadoBO ===");
//
//        try {
//            IPedidoProgramadoDAO pedidoProgramadoDAO = new PedidoProgramadoDAO(conexionBD);
//            IPedidoProgramadoBO pedidoProgramadoBO = new PedidoProgramadoBO(pedidoProgramadoDAO);
//
//            // --- 1️⃣ Buscar un pedido existente por ID
//            int idPedido = 7; // Cambiar según tu BD
//            System.out.println("\n--- Buscar pedido por ID " + idPedido + " ---");
//            try {
//                var pedido = pedidoProgramadoBO.buscarPorId(idPedido);
//                System.out.println("Pedido encontrado: ID=" + pedido.getIdPedido() + ", Estado=" + pedido.getEstado());
//            } catch (NegocioException ex) {
//                System.out.println("Error: " + ex.getMessage());
//            }
//
//            // --- 2️⃣ Intentar actualizar estado a Listo
//            System.out.println("\n--- Actualizar estado a Listo ---");
//            try {
//                pedidoProgramadoBO.actualizarEstado(idPedido, "Listo");
//                System.out.println("Estado actualizado a Listo correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("Error al actualizar a Listo: " + ex.getMessage());
//            }
//
//            // --- 3️⃣ Intentar actualizar estado a Entregado
//            System.out.println("\n--- Actualizar estado a Entregado ---");
//            try {
//                pedidoProgramadoBO.actualizarEstado(idPedido, "Entregado");
//                System.out.println("Estado actualizado a Entregado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("Error al actualizar a Entregado: " + ex.getMessage());
//            }
//
//            // --- 4️⃣ Intentar actualizar estado a Cancelado
//            int idPedidoCancelar = 8; // Asegúrate que esté en estado "Pendiente"
//            System.out.println("\n--- Intentar cancelar pedido ID " + idPedidoCancelar + " ---");
//            try {
//                pedidoProgramadoBO.actualizarEstado(idPedidoCancelar, "Cancelado");
//                System.out.println("Pedido cancelado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("No se pudo cancelar: " + ex.getMessage());
//            }
//
//            // --- 5️⃣ Intentar marcar un pedido como No Entregado
//            int idPedidoNoEntregado = 9; // Asegúrate que esté en estado "Listo"
//            System.out.println("\n--- Marcar pedido ID " + idPedidoNoEntregado + " como No Entregado ---");
//            try {
//                pedidoProgramadoBO.actualizarEstado(idPedidoNoEntregado, "No Entregado");
//                System.out.println("Pedido marcado como No Entregado correctamente.");
//            } catch (NegocioException ex) {
//                System.out.println("No se pudo marcar No Entregado: " + ex.getMessage());
//            }
//
//            PedidoProgramadoDTO pedido = pedidoProgramadoBO.buscarPorIdDTO(1);
//
//            System.out.println("===== PEDIDO PROGRAMADO =====");
//            System.out.println("Cliente ID: " + pedido.getIdCliente());
//            System.out.println("Empleado ID: " + pedido.getIdEmpleado());
//            System.out.println("Subtotal: " + pedido.getSubtotal());
//            System.out.println("Descuento: " + pedido.getDescuento());
//            System.out.println("Total: " + pedido.getTotal());
//            System.out.println("Cupón: " + pedido.getIdCupon());
//
//            System.out.println("\n--- DETALLES ---");
//
//            for (DetallePedidoDTO detalle : pedido.getDetalles()) {
//                System.out.println("Producto ID: " + detalle.getIdProducto());
//                System.out.println("Cantidad: " + detalle.getCantidad());
//                System.out.println("Precio Unitario: " + detalle.getPrecioUnitario());
//                System.out.println("Nota: " + detalle.getNota());
//                System.out.println("----------------------------");
//            }
//
//        } catch (NegocioException ex) {
//            System.err.println("Error en pruebas PedidoProgramadoBO: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//        System.out.println("\n=== Pruebas de HistorialEstadoBO ===");
//
//        try {
//
//            IHistorialEstadoDAO historialDAO = new HistorialEstadoDAO(conexionBD);
//            HistorialEstadoBO historialBO = new HistorialEstadoBO(historialDAO);
//
//            // -------------------------------
//            // 1️⃣ Obtener historial por estado
//            System.out.println("\n=== Historial de pedidos Entregados ===");
//            List<HistorialEstadoDTO> porEstado = historialBO.obtenerPedidosPorEstado("Entregado");
//
//            for (HistorialEstadoDTO h : porEstado) {
//                System.out.println(formatHistorial(h));
//            }
//
//            // -------------------------------
//            // 2️⃣ Buscar historial por teléfono
//            System.out.println("\n=== Historial del teléfono 5551234567 ===");
//            List<HistorialEstadoDTO> porTelefono = historialBO.buscarPorTelefono("5551234567");
//
//            for (HistorialEstadoDTO h : porTelefono) {
//                System.out.println(formatHistorial(h));
//            }
//
//            // -------------------------------
//            // 3️⃣ Buscar historial por rango de fechas
//            LocalDate inicioHist = LocalDate.now().minusDays(7);
//            LocalDate finHist = LocalDate.now();
//
//            System.out.println("\n=== Historial de los últimos 7 días ===");
//            List<HistorialEstadoDTO> porRango = historialBO.buscarPorRangoFechas(inicioHist, finHist);
//
//            for (HistorialEstadoDTO h : porRango) {
//                System.out.println(formatHistorial(h));
//            }
//
//            // -------------------------------
//            // 4️⃣ Buscar historial por folio
//            int folioBuscarHist = 1;
//
//            System.out.println("\n=== Historial por folio: " + folioBuscarHist + " ===");
//            List<HistorialEstadoDTO> porFolio = historialBO.buscarPorFolio(folioBuscarHist);
//
//            if (porFolio.isEmpty()) {
//                System.out.println("No se encontró historial para el folio " + folioBuscarHist);
//            } else {
//                for (HistorialEstadoDTO h : porFolio) {
//                    System.out.println(formatHistorial(h));
//                }
//            }
//
//        } catch (NegocioException ex) {
//            System.err.println("Error en pruebas HistorialEstadoBO: " + ex.getMessage());
//            ex.printStackTrace();
//        }
   // }
//
//    private static String formatPedido(PedidoEntregaDTO p) {
//        return "ID: " + p.getIdPedido()
//                + " | Cliente: " + p.getNombreCliente()
//                + " | Teléfono: " + p.getTelefono()
//                + " | Estado: " + p.getEstado()
//                + " | Total: $" + p.getTotal()
//                + " | Folio: " + p.getFolio()
//                + " | Fecha: " + p.getFechaCreacion();
//    }
//    
//    private static String formatHistorial(HistorialEstadoDTO h) {
//        return "ID Historial: " + h.getIdHistorial()
//                + " | ID Pedido: " + h.getIdPedido()
//                + " | Folio: " + h.getFolio()
//                + " | Cliente: " + h.getNombreCliente()
//                + " | Teléfono: " + h.getTelefonoCliente()
//                + " | Estado Anterior: " + h.getEstadoAnterior()
//                + " | Estado Nuevo: " + h.getEstadoNuevo()
//                + " | Fecha Cambio: " + h.getFechaCambio()
//                + " | Total: $" + h.getTotal();
//    }
//}