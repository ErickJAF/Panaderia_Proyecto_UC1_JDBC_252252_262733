package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import javax.swing.SwingUtilities;

import negocio.BOs.IUsuarioBO;
import negocio.BOs.UsuarioBO;
import negocio.BOs.IProductoBO;
import negocio.BOs.ProductoBO;
import negocio.BOs.IPedidoExpressBO;
import negocio.BOs.PedidoExpressBO;

import persistencia.DAOs.IUsuarioDAO;
import persistencia.DAOs.UsuarioDAO;
import persistencia.DAOs.IProductoDAO;
import persistencia.DAOs.ProductoDAO;
import persistencia.DAOs.IPedidoExpressDAO;
import persistencia.DAOs.PedidoExpressDAO;

import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;

import presentacion.FrmInicio;

public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) {

        IConexionBD conexion = new ConexionBD();

        //  Usuario
        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        IUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);

        // Producto
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        IProductoBO productoBO = new ProductoBO(productoDAO);

        //  Pedido Express
        IPedidoExpressDAO pedidoExpressDAO =
                new PedidoExpressDAO(conexion);
        IPedidoExpressBO pedidoExpressBO =
                new PedidoExpressBO(pedidoExpressDAO);

        SwingUtilities.invokeLater(() -> {
            new FrmInicio(
                    usuarioBO,
                    productoBO,
                    pedidoExpressBO
            ).setVisible(true);
        });
    }
}