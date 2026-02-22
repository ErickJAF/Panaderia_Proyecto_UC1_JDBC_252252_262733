/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import negocio.BOs.IPedidoProgramadoBO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.IProductoDAO;
import negocio.BOs.ProductoBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.PedidoProgramadoBO;
import negocio.excepciones.NegocioException;
import presentacion.FrmCrearPedidoProgramado;

/**
 *
 * @author ERICK
 */
public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) throws NegocioException {
      
    
// CONEXIÓN
        IConexionBD conexion = new ConexionBD();

        // DAO
        IProductoDAO productoDAO = new ProductoDAO(conexion);

        // BO PRODUCTO
        IProductoBO productoBO = new ProductoBO(productoDAO);

        // BO PEDIDO PROGRAMADO
        IPedidoProgramadoBO pedidoBO = new PedidoProgramadoBO(conexion);

        // Simulación usuario logueado
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
    }
}

