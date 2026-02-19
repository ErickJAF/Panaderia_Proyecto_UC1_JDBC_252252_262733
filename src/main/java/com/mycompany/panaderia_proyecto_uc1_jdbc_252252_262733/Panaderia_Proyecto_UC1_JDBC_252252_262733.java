/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import java.time.LocalDate;
import java.util.List;
import persistencia.DAOs.PedidoProgramadoDAO;
import persistencia.conexion.ConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author ERICK
 */
public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) {
        try {

            ConexionBD conexion = new ConexionBD();

            PedidoProgramadoDAO dao = new PedidoProgramadoDAO(conexion);

            PedidoProgramado pedido = new PedidoProgramado();

            pedido.setFechaCreacion(LocalDate.now());
            pedido.setEstado("Pendiente");
            pedido.setSubtotal(500);
            pedido.setDescuento(50);
            pedido.setTotal(450);
            pedido.setIdEmpleado(1);
            pedido.setIdCliente(3);
            pedido.setIdCupon(null);

            dao.insertar(pedido);

            System.out.println("Insertado correctamente");
            System.out.println("ID generado: " + pedido.getIdPedido());
            
            int ultimo = dao.obtenerUltimoNumeroPedido();
            System.out.println("Ultimo ID registrado: " + ultimo);
            
            List<PedidoProgramado> lista = dao.obtenerActivosPorCliente(3);

            for (PedidoProgramado p : lista) {
                System.out.println("ID: " + p.getIdPedido() + " Estado: " + p.getEstado() + " Total: " + p.getTotal());
            }
            
            List<PedidoProgramado> lista1 = dao.obtenerPorTelefono("5551234567");
            
            for (PedidoProgramado p : lista1) {
                System.out.println("ID: " + p.getIdPedido() + " Cliente: " + p.getIdCliente() + " Estado: " + p.getEstado());
            }
            
            dao.actualizarEstado(4, "Cancelado");
            System.out.println("Estado actualizado");

        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }
}
