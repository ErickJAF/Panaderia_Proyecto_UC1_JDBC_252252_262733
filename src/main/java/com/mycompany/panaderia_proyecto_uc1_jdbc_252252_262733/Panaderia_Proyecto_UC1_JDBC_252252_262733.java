package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import javax.swing.SwingUtilities;

import negocio.BOs.*;
import persistencia.DAOs.*;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import presentacion.FrmInicio;

public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) {

        IConexionBD conexion = new ConexionBD();

        // USUARIO
        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        IUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);

        // PRODUCTO
        IProductoDAO productoDAO = new ProductoDAO(conexion);
        IProductoBO productoBO = new ProductoBO(productoDAO);

        // PEDIDO EXPRESS
        IPedidoExpressDAO pedidoExpressDAO =
                new PedidoExpressDAO(conexion);
        IPedidoExpressBO pedidoExpressBO =
                new PedidoExpressBO(pedidoExpressDAO);

        // PEDIDO PROGRAMADO
        IPedidoProgramadoDAO pedidoProgramadoDAO =
                new PedidoProgramadoDAO(conexion);

        IDetallePedidoDAO detallePedidoDAO =
                new DetallePedidoDAO(conexion);

        ICuponDAO cuponDAO =
                new CuponDAO(conexion);

        IPedidoProgramadoBO pedidoProgramadoBO =
                new PedidoProgramadoBO(
                        pedidoProgramadoDAO,
                        detallePedidoDAO,
                        cuponDAO
                );

    
        // INICIO APP
        SwingUtilities.invokeLater(() -> {
            new FrmInicio(
                    usuarioBO,
                    productoBO,
                    pedidoExpressBO,
                    pedidoProgramadoBO
            ).setVisible(true);
        });
    }
}