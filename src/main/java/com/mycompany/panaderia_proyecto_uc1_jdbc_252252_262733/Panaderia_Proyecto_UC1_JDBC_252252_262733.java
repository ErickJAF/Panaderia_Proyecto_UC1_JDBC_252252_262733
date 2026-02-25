package com.mycompany.panaderia_proyecto_uc1_jdbc_252252_262733;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import negocio.BOs.*;
import persistencia.DAOs.*;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import presentacion.FrmLogin;

public class Panaderia_Proyecto_UC1_JDBC_252252_262733 {

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        IConexionBD conexion = new ConexionBD();

        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        IUsuarioBO usuarioBO = new UsuarioBO(usuarioDAO);

        IProductoDAO productoDAO = new ProductoDAO(conexion);
        IProductoBO productoBO = new ProductoBO(productoDAO);

        IPedidoExpressDAO pedidoExpressDAO =
                new PedidoExpressDAO(conexion);
        IPedidoExpressBO pedidoExpressBO =
                new PedidoExpressBO(pedidoExpressDAO);

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

        SwingUtilities.invokeLater(() -> {
            new FrmLogin(
                    usuarioBO,
                    productoBO,
                    pedidoExpressBO,
                    pedidoProgramadoBO
            ).setVisible(true);
        });
    }
}